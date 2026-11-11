package com.github.s0phs.ms_doacoes.repositories;

import com.github.s0phs.ms_doacoes.entities.Doacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoacaoRepository extends JpaRepository<Doacao, Long> {
}