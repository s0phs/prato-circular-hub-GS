package com.github.s0phs.ms_estabelecimentos.dto;

import com.github.s0phs.ms_estabelecimentos.entities.Estabelecimento;
import com.github.s0phs.ms_estabelecimentos.entities.StatusEstabelecimento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class EstabelecimentoResponseDTO {

    private Long id;
    private String nome;
    private String cnpj;
    private String telefone;
    private String cep;
    private String endereco;
    private StatusEstabelecimento status;

    public EstabelecimentoResponseDTO(Estabelecimento estabelecimento) {
        id = estabelecimento.getId();
        nome = estabelecimento.getNome();
        cnpj = estabelecimento.getCnpj();
        telefone = estabelecimento.getTelefone();
        cep = estabelecimento.getCep();
        endereco = estabelecimento.getEndereco();
        status = estabelecimento.getStatus();
    }
}