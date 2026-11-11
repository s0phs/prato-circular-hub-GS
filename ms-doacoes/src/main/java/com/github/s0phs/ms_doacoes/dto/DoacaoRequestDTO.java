package com.github.s0phs.ms_doacoes.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DoacaoRequestDTO {

    @NotBlank(message = "O nome do alimento é obrigatório.")
    private String alimento;

    @NotNull(message = "A quantidade em kg é obrigatória.")
    @DecimalMin(value = "0.01", message = "A quantidade deve ser maior que zero.")
    private Double quantidadeKg;

    @NotNull(message = "A data de validade é obrigatória.")
    @FutureOrPresent(message = "A data de validade não pode ser anterior à data atual.")
    private LocalDate dataValidade;

    @NotNull(message = "O ID do estabelecimento é obrigatório.")
    private Long estabelecimentoId;
}