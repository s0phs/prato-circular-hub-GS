package com.github.s0phs.ms_estabelecimentos.controller;

import com.github.s0phs.ms_estabelecimentos.dto.EstabelecimentoRequestDTO;
import com.github.s0phs.ms_estabelecimentos.dto.EstabelecimentoResponseDTO;
import com.github.s0phs.ms_estabelecimentos.service.EstabelecimentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/estabelecimentos")
public class EstabelecimentoController {

    @Autowired
    private EstabelecimentoService estabelecimentoService;

    @GetMapping
    public ResponseEntity<List<EstabelecimentoResponseDTO>> getAll() {

        List<EstabelecimentoResponseDTO> list = estabelecimentoService.findAllEstabelecimentos();

        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponseDTO> getOne(@PathVariable Long id) {

        EstabelecimentoResponseDTO responseDTO = estabelecimentoService.findEstabelecimentoById(id);

        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping
    public ResponseEntity<EstabelecimentoResponseDTO> save(
            @RequestBody @Valid EstabelecimentoRequestDTO requestDTO) {

        EstabelecimentoResponseDTO responseDTO = estabelecimentoService.save(requestDTO);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(responseDTO.getId())
                .toUri();

        return ResponseEntity.created(uri).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstabelecimentoResponseDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody EstabelecimentoRequestDTO requestDTO) {

        EstabelecimentoResponseDTO responseDTO = estabelecimentoService.update(id, requestDTO);

        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}/ativar")
    public ResponseEntity<EstabelecimentoResponseDTO> ativar(@PathVariable Long id) {

        EstabelecimentoResponseDTO responseDTO = estabelecimentoService.ativarEstabelecimento(id);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        estabelecimentoService.deleteEstabelecimento(id);

        return ResponseEntity.noContent().build();
    }
}
