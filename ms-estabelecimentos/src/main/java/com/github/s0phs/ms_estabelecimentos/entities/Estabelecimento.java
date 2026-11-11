package com.github.s0phs.ms_estabelecimentos.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_estabelecimento")
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Estabelecimento {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String cnpj;

    @Column(nullable = false)
    private String telefone;

    @Column(nullable = false)
    private String cep;

    @Column(nullable = false)
    private String endereco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusEstabelecimento status;
}

