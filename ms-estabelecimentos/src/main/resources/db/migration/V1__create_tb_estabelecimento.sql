CREATE TABLE tb_estabelecimento (
                                    id BIGINT NOT NULL AUTO_INCREMENT,
                                    nome VARCHAR(255) NOT NULL,
                                    cnpj VARCHAR(255) NOT NULL UNIQUE,
                                    telefone VARCHAR(255) NOT NULL,
                                    cep VARCHAR(255) NOT NULL,
                                    endereco VARCHAR(255) NOT NULL,
                                    status VARCHAR(255) NOT NULL,
                                    PRIMARY KEY (id)
);