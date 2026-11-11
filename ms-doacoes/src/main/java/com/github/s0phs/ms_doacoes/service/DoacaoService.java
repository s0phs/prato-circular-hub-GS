package com.github.s0phs.ms_doacoes.service;

import com.github.s0phs.ms_doacoes.dto.DoacaoRequestDTO;
import com.github.s0phs.ms_doacoes.entities.StatusDoacao;
import java.time.LocalDate;
import com.github.s0phs.ms_doacoes.dto.DoacaoResponseDTO;
import com.github.s0phs.ms_doacoes.entities.Doacao;
import com.github.s0phs.ms_doacoes.exceptions.ResourceNotFoundException;
import com.github.s0phs.ms_doacoes.repositories.DoacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DoacaoService {

    @Autowired
    private DoacaoRepository repository;

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

        doacao.setAlimento(dto.getAlimento());
        doacao.setQuantidadeKg(dto.getQuantidadeKg());
        doacao.setDataValidade(dto.getDataValidade());
        doacao.setEstabelecimentoId(dto.getEstabelecimentoId());

        doacao.setDataCadastro(LocalDate.now());
        doacao.setStatus(StatusDoacao.DISPONIVEL);

        doacao = repository.save(doacao);

        return new DoacaoResponseDTO(doacao);
    }


}