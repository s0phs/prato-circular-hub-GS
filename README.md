<div align="center">

# ♻️ Prato Circular

### Doações de alimentos com microsserviços

**Conectando estabelecimentos que possuem excedentes de alimentos a iniciativas que podem aproveitá-los.**

![Java](https://img.shields.io/badge/Java-25-orange?style=flat-square) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Microsserviços-6DB33F?style=flat-square) ![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square) ![MySQL](https://img.shields.io/badge/MySQL-8.4.6-4479A1?style=flat-square)

</div>

---

## 📖 Sobre o projeto

O **Prato Circular** é um projeto acadêmico que busca contribuir para a redução do desperdício de alimentos. Seu backend foi desenvolvido com uma arquitetura de microsserviços, permitindo gerenciar estabelecimentos e doações de forma independente, mas integrada.

O projeto utiliza **Eureka Server** para descoberta de serviços, **API Gateway** como ponto central de acesso, **OpenFeign** para comunicação entre os microsserviços, **Circuit Breaker** para tratamento de indisponibilidade, **MySQL** para persistência e **Flyway** para criação e preenchimento inicial das tabelas.

## 🧩 Arquitetura

```text
                     Navegador / Swagger
                             |
                             v
                    API Gateway :8082
                       /           \
                      v             v
            MS Estabelecimentos   MS Doações
                      ^             |
                      |--- OpenFeign-|
                      |             |
                      v             v
              Banco estabelecimentos  Banco doacoes
                       \             /
                        MySQL :3306

           Eureka Server :8761
       (descoberta e registro dos serviços)
```

> O Eureka **não recebe todas as requisições da API**. Ele mantém o registro dos serviços para que o Gateway e o OpenFeign possam localizá-los.

| Serviço | Responsabilidade |
|---|---|
| `eureka-server` | Registro e descoberta dos microsserviços |
| `api-gateway` | Entrada única para acessar as APIs |
| `ms-estabelecimentos` | Cadastro, consulta e status dos estabelecimentos |
| `ms-doacoes` | Cadastro e gerenciamento das doações |
| `mysql_prato_circular` | Armazenamento dos dados |

> **Importante:** os quatro serviços Java são executados juntos em um Docker Compose. O MySQL utiliza outro Docker Compose, mas todos compartilham a rede `microservices-net`.

---

## 🚀 Como executar o projeto

### ✅ O que precisa estar instalado?

Apenas o **[Docker Desktop](https://www.docker.com/products/docker-desktop/)**, aberto e em execução, com suporte a containers Linux.

**Não é necessário instalar Java, Maven, MySQL, IntelliJ IDEA ou Git.** As aplicações serão compiladas dentro do Docker.

### 1. Baixar o projeto

1. Abra este repositório no GitHub.
2. Clique no botão verde **Code** e depois em **Download ZIP**.
3. Extraia o arquivo baixado para uma pasta de sua preferência.
4. Abra a pasta principal extraída, que contém `eureka-server`, `api-gateway`, `ms-estabelecimentos`, `ms-doacoes`, `mysql_846` e `microservices-hub-compose`.

A estrutura esperada é:

```text
prato-circular-hub-GS/
├── eureka-server/
│   └── Dockerfile
├── api-gateway/
│   └── Dockerfile
├── ms-estabelecimentos/
│   └── Dockerfile
├── ms-doacoes/
│   └── Dockerfile
├── mysql_846/
│   └── docker-compose.yml
└── microservices-hub-compose/
    └── docker-compose.yml
```

### 2. Abrir o terminal

No **Windows**, abra a pasta principal no Explorador de Arquivos, clique na barra de endereço, digite `powershell` e pressione **Enter**.

> Todos os comandos a seguir devem ser executados **nessa pasta principal**. Copie e execute um comando por vez.

### 3. Criar a rede Docker

```powershell
docker network create microservices-net
```

Se o Docker informar que a rede já existe, pode continuar normalmente.

### 4. Iniciar o MySQL

```powershell
docker compose -f mysql_846/docker-compose.yml up -d
```

Aguarde alguns instantes e confira se o banco terminou de iniciar:

```powershell
docker exec mysql_prato_circular mysqladmin ping -h localhost -u root -proot
```

O resultado esperado é:

```text
mysqld is alive
```

Se ainda não aparecer essa mensagem, aguarde alguns segundos e execute novamente o comando de verificação.

### 5. Conectar o MySQL à rede compartilhada

```powershell
docker network connect microservices-net mysql_prato_circular
```

Se não aparecer nenhuma mensagem, deu certo. Se o Docker disser que o container já está conectado, basta continuar.

### 6. Construir as quatro imagens

Execute **um comando de cada vez** e aguarde a conclusão de cada build:

**Eureka Server**

```powershell
docker build -t eureka-server:latest ./eureka-server
```

**API Gateway**

```powershell
docker build -t api-gateway:latest ./api-gateway
```

**Microsserviço de estabelecimentos**

```powershell
docker build -t ms-estabelecimentos:latest ./ms-estabelecimentos
```

**Microsserviço de doações**

```powershell
docker build -t ms-doacoes:latest ./ms-doacoes
```

> ⏳ Na primeira execução, essa etapa pode levar alguns minutos, pois o Docker precisa baixar as imagens e dependências necessárias.

### 7. Iniciar os microsserviços

```powershell
docker compose -f microservices-hub-compose/docker-compose.yml up -d
```

Aguarde cerca de **30 a 60 segundos** para as aplicações iniciarem e se registrarem no Eureka. Em computadores mais lentos, esse tempo pode ser maior.

### 8. Conferir se está tudo funcionando

```powershell
docker compose -f microservices-hub-compose/docker-compose.yml ps
```

Os quatro serviços Java devem aparecer com status **Up**. Para ver também o MySQL:

```powershell
docker ps
```

Os containers esperados são:

- `eureka-server`
- `api-gateway`
- `ms-estabelecimentos`
- `ms-doacoes`
- `mysql_prato_circular`

---

## 🌐 Acessar e testar

| Recurso | Endereço |
|---|---|
| 🧭 Eureka Server | [http://localhost:8761](http://localhost:8761) |
| 📚 Swagger UI | [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html) |
| 🏪 Listar estabelecimentos | [Abrir API](http://localhost:8082/ms-estabelecimentos/api/v1/estabelecimentos) |
| 🥫 Listar doações | [Abrir API](http://localhost:8082/ms-doacoes/api/v1/doacoes) |

### Verificar o Eureka

Ao abrir `http://localhost:8761`, procure a seção **Instances currently registered with Eureka**. Os serviços `API-GATEWAY`, `MS-ESTABELECIMENTOS` e `MS-DOACOES` devem aparecer como **UP**.

### Utilizar o Swagger

1. Acesse [Swagger UI](http://localhost:8082/swagger-ui/index.html).
2. No seletor de APIs, escolha `ms-estabelecimentos` ou `ms-doacoes`.
3. Abra o endpoint desejado e clique em **Try it out**.
4. Preencha os campos necessários e clique em **Execute**.

### Dados iniciais

O **Flyway** cria as tabelas e insere dados de exemplo automaticamente na primeira inicialização de um banco vazio.

**Estabelecimentos**

| ID | Nome | Status inicial |
|---|---|---|
| 1 | Mercado Central | `ATIVO` |
| 2 | Padaria Bom Pao | `INATIVO` |
| 3 | Restaurante Sabor Caseiro | `INATIVO` |

**Doações**

| ID | Alimento | Quantidade | Estabelecimento |
|---|---|---|---|
| 1 | Arroz | 10 kg | 1 |
| 2 | Feijao | 5 kg | 1 |
| 3 | Macarrao | 8 kg | 1 |

> Os registros podem mudar após os testes feitos pelo Swagger. Como o MySQL utiliza um volume persistente, reiniciar os containers não restaura automaticamente os dados iniciais.

---

## 🔄 Como a API funciona

O Prato Circular foi dividido em microsserviços para separar as responsabilidades. Cada serviço executa sua própria aplicação Spring Boot e gerencia os dados de sua área.

### 🧭 Eureka Server — descoberta dos serviços

O **Eureka Server** funciona como um catálogo dos serviços disponíveis.

Quando o `api-gateway`, o `ms-estabelecimentos` e o `ms-doacoes` iniciam, eles se registram no Eureka. Assim, os outros componentes podem descobrir onde cada aplicação está funcionando sem precisar configurar manualmente seu endereço IP.

O painel do Eureka pode ser acessado em **[http://localhost:8761](http://localhost:8761)**.

### 🚪 API Gateway — entrada única

O **API Gateway** é a porta de entrada das requisições externas. Em vez de acessar cada microsserviço por uma porta diferente, o usuário utiliza a porta `8082`.

O Gateway consulta as informações de descoberta do Eureka e encaminha a requisição para o serviço correspondente.

**Exemplos:**

| Requisição | Destino |
|---|---|
| `GET /ms-estabelecimentos/api/v1/estabelecimentos` | `ms-estabelecimentos` |
| `GET /ms-doacoes/api/v1/doacoes` | `ms-doacoes` |

O prefixo `/ms-estabelecimentos` ou `/ms-doacoes` identifica o serviço; o Gateway utiliza o roteamento dinâmico para encaminhar a requisição ao endpoint correto.

### 🏪 MS Estabelecimentos — gerenciamento dos doadores

O **`ms-estabelecimentos`** é responsável por cadastrar, consultar, atualizar e excluir estabelecimentos.

Cada estabelecimento possui informações como nome, CNPJ, telefone, CEP, endereço e status (`ATIVO` ou `INATIVO`).

Esse serviço também disponibiliza operações de **ativação e desativação**, utilizadas pelo `ms-doacoes` durante as regras de negócio.

Se um estabelecimento estiver `ATIVO`, sua exclusão é bloqueada com a resposta **HTTP 409 Conflict**.

### 🥫 MS Doações — gerenciamento dos alimentos

O **`ms-doacoes`** é responsável por cadastrar e gerenciar as doações de alimentos.

Cada doação registra informações como alimento, quantidade em quilogramas, datas, estabelecimento responsável e status. Os status utilizados são:

- `DISPONIVEL`: doação cadastrada e disponível.
- `RESERVADA`: doação reservada para recebimento.
- `RECEBIDA`: doação recebida.

As doações possuem um campo `estabelecimentoId`, que identifica o estabelecimento cadastrado no outro microsserviço.

### 🔗 Como os dois microsserviços se comunicam

A comunicação entre `ms-doacoes` e `ms-estabelecimentos` acontece por meio do **OpenFeign**, um cliente HTTP declarativo.

O `ms-doacoes` não altera diretamente o banco de dados dos estabelecimentos. Em vez disso, chama a API do `ms-estabelecimentos` para solicitar a mudança de status.

**Exemplo 1 — cadastro de uma doação:**

1. O usuário cadastra uma doação pelo Swagger, utilizando `POST /api/v1/doacoes`.
2. O `ms-doacoes` processa o cadastro e utiliza o OpenFeign para solicitar a ativação do estabelecimento correspondente.
3. O `ms-estabelecimentos` recebe a requisição e altera o status para `ATIVO`.
4. A operação de cadastro retorna **HTTP 201 Created** quando concluída com sucesso.

**Exemplo 2 — exclusão da última doação:**

1. O usuário exclui uma doação pelo endpoint `DELETE /api/v1/doacoes/{id}`.
2. O `ms-doacoes` verifica se ainda existem outras doações vinculadas ao mesmo estabelecimento.
3. Se **não existir nenhuma**, o OpenFeign solicita a desativação ao `ms-estabelecimentos`.
4. O estabelecimento passa para `INATIVO`.

> **Regra importante:** uma doação com status `RECEBIDA` continua contando como doação cadastrada. Portanto, o estabelecimento permanece `ATIVO` enquanto possuir qualquer doação, independentemente do status.

### 🛡️ Circuit Breaker — tratamento de indisponibilidade

O `ms-doacoes` também utiliza **Resilience4j Circuit Breaker** para tratar falhas de comunicação.

Por exemplo, se o `ms-estabelecimentos` estiver indisponível durante uma operação que dependa dele, o Circuit Breaker permite que a aplicação trate a falha por meio do fallback configurado, em vez de deixar a exceção sem tratamento.

Nos testes do projeto, a tentativa de cadastrar uma doação enquanto o serviço de estabelecimentos estava parado retornou **HTTP 503 Service Unavailable**. Após reiniciar o serviço, a comunicação voltou a funcionar.

### 🗄️ MySQL e Flyway — persistência dos dados

Os dois microsserviços utilizam o mesmo servidor MySQL, mas com **bancos de dados independentes**:

| Banco | Utilizado por | Conteúdo |
|---|---|---|
| `estabelecimentos` | `ms-estabelecimentos` | Dados dos estabelecimentos |
| `doacoes` | `ms-doacoes` | Dados das doações |

O **Flyway** executa as migrations SQL na inicialização para criar as tabelas e inserir os registros iniciais em um banco vazio.

Essa separação evita que um microsserviço precise acessar diretamente as tabelas do outro. A integração ocorre pelas APIs.

### 🧪 Exemplo completo de funcionamento

Imagine que a **Padaria Bom Pao (ID 2)** esteja `INATIVO` e não tenha nenhuma doação.

Ao cadastrar uma nova doação de pão para o estabelecimento de ID `2`, o `ms-doacoes` registra o alimento e solicita, via OpenFeign, a ativação da padaria. O `ms-estabelecimentos` atualiza seu status para `ATIVO`.

Quando essa for a última doação da padaria e ela for excluída, o `ms-doacoes` solicita a desativação. A padaria volta para `INATIVO`.

Esse fluxo demonstra o funcionamento integrado de **API Gateway, Eureka, OpenFeign, microsserviços e MySQL**.

---

## ⚙️ Regras de negócio

- **Ativação automática:** ao cadastrar uma doação, o estabelecimento correspondente passa para `ATIVO`.
- **Desativação automática:** ao excluir a última doação, o estabelecimento passa para `INATIVO`.
- **Doações recebidas:** o estabelecimento permanece `ATIVO` enquanto possuir doações cadastradas, inclusive com status `RECEBIDA`.
- **Proteção de exclusão:** estabelecimentos `ATIVO` não podem ser excluídos; a API retorna `409 Conflict`.
- **Comunicação resiliente:** o `ms-doacoes` utiliza OpenFeign e Circuit Breaker para lidar com a comunicação com o `ms-estabelecimentos`.

---

## 🛑 Como parar o projeto

**Parar os quatro serviços Java:**

```powershell
docker compose -f microservices-hub-compose/docker-compose.yml down
```

**Parar também o MySQL:**

```powershell
docker compose -f mysql_846/docker-compose.yml down
```

Esses comandos não apagam os dados persistidos na pasta de volume do MySQL.

**Para executar novamente:** inicie o MySQL (passo 4), conecte-o à rede se necessário (passo 5) e inicie os microsserviços (passo 7). Não é preciso reconstruir as imagens se o código não mudou.

---

## 🛠️ Problemas comuns

| Problema | O que verificar |
|---|---|
| O Docker não responde | Confirme que o Docker Desktop está aberto e totalmente iniciado. |
| Porta ocupada | As portas `3306`, `8761` e `8082` precisam estar livres. |
| Eureka sem serviços | Aguarde a inicialização e atualize a página. |
| Erro `404` no Gateway | Confira se os serviços aparecem como `UP` no Eureka e tente novamente após alguns segundos. |
| Falha de conexão com MySQL | Verifique se `mysql_prato_circular` está em execução e conectado à rede `microservices-net`. |
| Um serviço não inicia | Consulte os logs do container para identificar o erro. |

Para consultar os logs de um container, execute:

```powershell
docker logs api-gateway --tail 100
```

Substitua `api-gateway` pelo nome do container que deseja verificar.

---

<div align="center">

**Prato Circular** · Projeto acadêmico · FIAP · 2026

</div>
