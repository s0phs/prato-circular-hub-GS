package com.github.s0phs.ms_doacoes.dto;

import com.github.s0phs.ms_doacoes.entities.TipoRecebedor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecebedorRequestDTO {

    @NotNull(message = "O tipo de recebedor é obrigatório.")
    private TipoRecebedor tipoRecebedor;

    @NotBlank(message = "O nome do recebedor é obrigatório.")
    private String nomeRecebedor;
}