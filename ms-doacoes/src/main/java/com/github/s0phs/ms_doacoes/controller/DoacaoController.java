package com.github.s0phs.ms_doacoes.controller;

import com.github.s0phs.ms_doacoes.dto.DoacaoResponseDTO;
import com.github.s0phs.ms_doacoes.exceptions.BusinessException;
import com.github.s0phs.ms_doacoes.exceptions.ResourceNotFoundException;
import com.github.s0phs.ms_doacoes.service.DoacaoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.github.s0phs.ms_doacoes.dto.DoacaoRequestDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.github.s0phs.ms_doacoes.dto.RecebedorRequestDTO;
import java.net.URI;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/doacoes")
public class DoacaoController {

    private final DoacaoService doacaoService;

    public DoacaoController(DoacaoService doacaoService) {
        this.doacaoService = doacaoService;
    }

    @GetMapping
    public ResponseEntity<List<DoacaoResponseDTO>> getAllDoacoes() {

        List<DoacaoResponseDTO> doacoes = doacaoService.findAll();

        return ResponseEntity.ok(doacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoacaoResponseDTO> getDoacaoById(@PathVariable Long id) {

        DoacaoResponseDTO dto = doacaoService.findById(id);

        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @CircuitBreaker(name = "ativarEstabelecimento", fallbackMethod = "fallbackCriarDoacao")
    public ResponseEntity<DoacaoResponseDTO> createDoacao(
            @RequestBody @Valid DoacaoRequestDTO dto) {

        DoacaoResponseDTO doacaoDTO = doacaoService.save(dto);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(doacaoDTO.getId())
                .toUri();

        return ResponseEntity.created(uri).body(doacaoDTO);
    }

    public ResponseEntity<DoacaoResponseDTO> fallbackCriarDoacao(
            DoacaoRequestDTO dto, Throwable e) {

        if (e instanceof ResourceNotFoundException) {
            throw (ResourceNotFoundException) e;
        }

        if (e instanceof BusinessException) {
            throw (BusinessException) e;
        }

        log.error("Falha ao cadastrar doação. Erro: {}", e.getMessage());

        return ResponseEntity.status(503).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoacaoResponseDTO> updateDoacao(
            @PathVariable Long id,
            @Valid @RequestBody DoacaoRequestDTO dto) {

        DoacaoResponseDTO doacaoDTO = doacaoService.update(id, dto);

        return ResponseEntity.ok(doacaoDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoacao(@PathVariable Long id) {

        doacaoService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reservar")
    public ResponseEntity<DoacaoResponseDTO> reservarDoacao(
            @PathVariable Long id,
            @Valid @RequestBody RecebedorRequestDTO dto) {

        DoacaoResponseDTO doacaoDTO = doacaoService.reservar(id, dto);

        return ResponseEntity.ok(doacaoDTO);
    }

    @PatchMapping("/{id}/receber")
    public ResponseEntity<DoacaoResponseDTO> receberDoacao(
            @PathVariable Long id,
            @Valid @RequestBody RecebedorRequestDTO dto) {

        DoacaoResponseDTO doacaoDTO = doacaoService.receber(id, dto);

        return ResponseEntity.ok(doacaoDTO);
    }

}