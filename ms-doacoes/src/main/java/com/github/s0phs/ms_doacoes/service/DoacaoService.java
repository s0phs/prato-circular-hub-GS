package com.github.s0phs.ms_doacoes.service;

import com.github.s0phs.ms_doacoes.dto.DoacaoRequestDTO;
import com.github.s0phs.ms_doacoes.entities.StatusDoacao;
import java.time.LocalDate;
import com.github.s0phs.ms_doacoes.dto.DoacaoResponseDTO;
import com.github.s0phs.ms_doacoes.entities.Doacao;
import com.github.s0phs.ms_doacoes.exceptions.BusinessException;
import com.github.s0phs.ms_doacoes.exceptions.ResourceNotFoundException;
import com.github.s0phs.ms_doacoes.repositories.DoacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.github.s0phs.ms_doacoes.clients.EstabelecimentoClient;
import feign.FeignException;
import com.github.s0phs.ms_doacoes.dto.RecebedorRequestDTO;
import com.github.s0phs.ms_doacoes.exceptions.BusinessException;
import java.util.List;

@Service
public class DoacaoService {

    @Autowired
    private DoacaoRepository repository;

    @Autowired
    private EstabelecimentoClient estabelecimentoClient;

    @Transactional(readOnly = true)
    public List<DoacaoResponseDTO> findAll() {
        List<Doacao> doacoes = repository.findAll();

        return doacoes.stream()
                .map(DoacaoResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public DoacaoResponseDTO findById(Long id) {
        Doacao doacao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doação não encontrada. ID: " + id
                ));

        return new DoacaoResponseDTO(doacao);
    }

    @Transactional
    public DoacaoResponseDTO save(DoacaoRequestDTO dto) {

        Doacao doacao = new Doacao();

        mapDtoToDoacao(dto, doacao);
        doacao.setEstabelecimentoId(dto.getEstabelecimentoId());

        doacao.setDataCadastro(LocalDate.now());
        doacao.setStatus(StatusDoacao.DISPONIVEL);

        doacao = repository.save(doacao);

        try {
            estabelecimentoClient.ativarEstabelecimento(
                    doacao.getEstabelecimentoId()
            );
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException(
                    "Estabelecimento não encontrado. ID: " + dto.getEstabelecimentoId()
            );
        } catch (FeignException e) {
            throw new RuntimeException(
                    "Falha ao se comunicar com ms-estabelecimentos"
            );
        }

        return new DoacaoResponseDTO(doacao);
    }

    @Transactional
    public DoacaoResponseDTO update(Long id, DoacaoRequestDTO dto) {

        Doacao doacao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doação não encontrada. ID: " + id
                ));

        if (doacao.getStatus() == StatusDoacao.RECEBIDA) {
            throw new BusinessException(
                    "Doação ID " + id + " já foi recebida e não pode ser alterada"
            );
        }

        mapDtoToDoacao(dto, doacao);

        doacao = repository.save(doacao);

        return new DoacaoResponseDTO(doacao);
    }

    @Transactional
    public void delete(Long id) {

        Doacao doacao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doação não encontrada. ID: " + id
                ));

        Long estabelecimentoId = doacao.getEstabelecimentoId();

        repository.delete(doacao);
        repository.flush();

        if (!repository.existsByEstabelecimentoId(estabelecimentoId)) {
            estabelecimentoClient.desativarEstabelecimento(estabelecimentoId);
        }
    }

    @Transactional
    public DoacaoResponseDTO reservar(Long id, RecebedorRequestDTO dto) {

        Doacao doacao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doação não encontrada. ID: " + id
                ));

        if (doacao.getStatus() != StatusDoacao.DISPONIVEL) {
            throw new BusinessException(
                    "Doação ID " + id + " não está disponível para reserva"
            );
        }

        doacao.setStatus(StatusDoacao.RESERVADA);
        doacao.setTipoRecebedor(dto.getTipoRecebedor());
        doacao.setNomeRecebedor(dto.getNomeRecebedor());

        doacao = repository.save(doacao);

        return new DoacaoResponseDTO(doacao);
    }

    @Transactional
    public DoacaoResponseDTO receber(Long id, RecebedorRequestDTO dto) {

        Doacao doacao = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doação não encontrada. ID: " + id
                ));

        if (doacao.getStatus() == StatusDoacao.RECEBIDA) {
            throw new BusinessException(
                    "Doação ID " + id + " já foi recebida"
            );
        }

        if (doacao.getStatus() == StatusDoacao.RESERVADA) {

            if (doacao.getTipoRecebedor() != dto.getTipoRecebedor()
                    || !doacao.getNomeRecebedor().equals(dto.getNomeRecebedor())) {
                throw new BusinessException(
                        "Esta doação está reservada para outro recebedor"
                );
            }
        }

        doacao.setTipoRecebedor(dto.getTipoRecebedor());
        doacao.setNomeRecebedor(dto.getNomeRecebedor());
        doacao.setStatus(StatusDoacao.RECEBIDA);
        doacao.setDataRecebimento(LocalDate.now());

        doacao = repository.save(doacao);

        return new DoacaoResponseDTO(doacao);
    }

    private void mapDtoToDoacao(DoacaoRequestDTO dto, Doacao doacao) {

        doacao.setAlimento(dto.getAlimento());
        doacao.setQuantidadeKg(dto.getQuantidadeKg());
        doacao.setDataValidade(dto.getDataValidade());
        doacao.setEstabelecimentoId(dto.getEstabelecimentoId());
    }

}