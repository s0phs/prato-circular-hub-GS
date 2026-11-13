package com.github.s0phs.ms_doacoes.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "ms-estabelecimentos")
public interface EstabelecimentoClient {

    @PutMapping("/api/v1/estabelecimentos/{id}/ativar")
    void ativarEstabelecimento(@PathVariable("id") Long id);

    @PutMapping("/api/v1/estabelecimentos/{id}/desativar")
    void desativarEstabelecimento(@PathVariable("id") Long id);
}