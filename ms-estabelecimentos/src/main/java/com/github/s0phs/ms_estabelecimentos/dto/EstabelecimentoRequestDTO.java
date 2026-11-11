package com.github.s0phs.ms_estabelecimentos.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EstabelecimentoRequestDTO {

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O CNPJ é obrigatório")
    private String cnpj;

    @NotBlank(message = "O telefone é obrigatório")
    private String telefone;

    @NotBlank(message = "O CEP é obrigatório")
    private String cep;

    @NotBlank(message = "O endereço é obrigatório")
    private String endereco;
}
