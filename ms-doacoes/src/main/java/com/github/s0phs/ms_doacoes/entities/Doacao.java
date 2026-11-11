package com.github.s0phs.ms_doacoes.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tb_doacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Doacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String alimento;

    private Double quantidadeKg;

    private LocalDate dataValidade;

    private LocalDate dataCadastro;

    private Long estabelecimentoId;

    @Enumerated(EnumType.STRING)
    private StatusDoacao status;

    @Enumerated(EnumType.STRING)
    private TipoRecebedor tipoRecebedor;

    private String nomeRecebedor;

    private LocalDate dataRecebimento;
}