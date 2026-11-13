package com.github.s0phs.ms_estabelecimentos.service;

import com.github.s0phs.ms_estabelecimentos.dto.EstabelecimentoRequestDTO;
import com.github.s0phs.ms_estabelecimentos.dto.EstabelecimentoResponseDTO;
import com.github.s0phs.ms_estabelecimentos.entities.Estabelecimento;
import com.github.s0phs.ms_estabelecimentos.entities.StatusEstabelecimento;
import com.github.s0phs.ms_estabelecimentos.exceptions.BusinessException;
import com.github.s0phs.ms_estabelecimentos.exceptions.ResourceNotFoundException;
import com.github.s0phs.ms_estabelecimentos.repositories.EstabelecimentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EstabelecimentoService {

    @Autowired
    private EstabelecimentoRepository estabelecimentoRepository;

    @Transactional(readOnly = true)
    public List<EstabelecimentoResponseDTO> findAllEstabelecimentos() {

        return estabelecimentoRepository.findAll()
                .stream()
                .map(EstabelecimentoResponseDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public EstabelecimentoResponseDTO findEstabelecimentoById(Long id) {

        Estabelecimento estabelecimento = estabelecimentoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Estabelecimento não encontrado. ID: " + id)
        );

        return new EstabelecimentoResponseDTO(estabelecimento);
    }

    @Transactional
    public EstabelecimentoResponseDTO save(EstabelecimentoRequestDTO requestDTO) {

        Estabelecimento estabelecimento = new Estabelecimento();
        mapDtoToEstabelecimento(requestDTO, estabelecimento);
        estabelecimento.setStatus(StatusEstabelecimento.INATIVO);

        estabelecimento = estabelecimentoRepository.save(estabelecimento);

        return new EstabelecimentoResponseDTO(estabelecimento);
    }

    @Transactional
    public EstabelecimentoResponseDTO update(Long id, EstabelecimentoRequestDTO requestDTO) {

        try {
            Estabelecimento estabelecimento = estabelecimentoRepository.getReferenceById(id);

            mapDtoToEstabelecimento(requestDTO, estabelecimento);

            estabelecimento = estabelecimentoRepository.save(estabelecimento);

            return new EstabelecimentoResponseDTO(estabelecimento);

        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Estabelecimento não encontrado. ID: " + id);
        }
    }

    @Transactional
    public void deleteEstabelecimento(Long id) {

        Estabelecimento estabelecimento = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Estabelecimento não encontrado. ID: " + id
                ));

        if (estabelecimento.getStatus() == StatusEstabelecimento.ATIVO) {
            throw new BusinessException(
                    "Não é possível excluir um estabelecimento ativo."
            );
        }

        estabelecimentoRepository.delete(estabelecimento);
    }

    @Transactional
    public EstabelecimentoResponseDTO ativarEstabelecimento(Long id) {

        Estabelecimento estabelecimento = estabelecimentoRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Estabelecimento não encontrado. ID: " + id)
        );

        estabelecimento.setStatus(StatusEstabelecimento.ATIVO);

        estabelecimento = estabelecimentoRepository.save(estabelecimento);

        return new EstabelecimentoResponseDTO(estabelecimento);
    }

    public void desativarEstabelecimento(Long id) {
        Estabelecimento estabelecimento = estabelecimentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estabelecimento não encontrado"));

        estabelecimento.setStatus(StatusEstabelecimento.INATIVO);
        estabelecimentoRepository.save(estabelecimento);
    }

    private void mapDtoToEstabelecimento(
            EstabelecimentoRequestDTO requestDTO,
            Estabelecimento estabelecimento) {

        estabelecimento.setNome(requestDTO.getNome());
        estabelecimento.setCnpj(requestDTO.getCnpj());
        estabelecimento.setTelefone(requestDTO.getTelefone());
        estabelecimento.setCep(requestDTO.getCep());
        estabelecimento.setEndereco(requestDTO.getEndereco());
    }
}