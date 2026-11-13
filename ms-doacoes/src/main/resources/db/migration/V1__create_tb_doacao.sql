CREATE TABLE tb_doacao (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           alimento VARCHAR(255),
                           quantidade_kg DOUBLE,
                           data_validade DATE,
                           data_cadastro DATE,
                           estabelecimento_id BIGINT,
                           status VARCHAR(255),
                           tipo_recebedor VARCHAR(255),
                           nome_recebedor VARCHAR(255),
                           data_recebimento DATE,
                           PRIMARY KEY (id)
);