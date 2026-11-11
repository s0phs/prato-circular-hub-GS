package com.github.s0phs.ms_doacoes.dto;

import com.github.s0phs.ms_doacoes.entities.Doacao;
import com.github.s0phs.ms_doacoes.entities.StatusDoacao;
import com.github.s0phs.ms_doacoes.entities.TipoRecebedor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DoacaoResponseDTO {

    private Long id;
    private String alimento;
    private Double quantidadeKg;
    private LocalDate dataValidade;
    private LocalDate dataCadastro;
    private Long estabelecimentoId;
    private StatusDoacao status;
    private TipoRecebedor tipoRecebedor;
    private String nomeRecebedor;
    private LocalDate dataRecebimento;

    public DoacaoResponseDTO(Doacao doacao) {
        this.id = doacao.getId();
        this.alimento = doacao.getAlimento();
        this.quantidadeKg = doacao.getQuantidadeKg();
        this.dataValidade = doacao.getDataValidade();
        this.dataCadastro = doacao.getDataCadastro();
        this.estabelecimentoId = doacao.getEstabelecimentoId();
        this.status = doacao.getStatus();
        this.tipoRecebedor = doacao.getTipoRecebedor();
        this.nomeRecebedor = doacao.getNomeRecebedor();
        this.dataRecebimento = doacao.getDataRecebimento();
    }
}