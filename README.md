# 🐾 Desafio cadastro

API REST desenvolvida com **Spring Boot 3**, **Java 21** e **MySQL** que realiza o cadastro e gerenciamento de pets e tutores.
O projeto inclui integração com o **Swagger UI** para documentação automática e **Docker Compose** para subir o banco de dados e o Redis(cache).
---------------------------------------

## 🚀 Tecnologias utilizadas

* **Java 21**
* **Spring Boot 3.5.6**
    * **Spring Web**
    * **Spring Data JPA**
* **MySQL 8**
* **Docker Compose**
* **SpringDoc OpenAPI (Swagger UI)**
* **JUnit 5** + **Mockito**
* **Maven**
* **Redis** (cache)

---

## ⚙️ Configuração do ambiente

### 🔧 Requisitos

* **JDK 21+**
* **Maven 3+**
* **Docker Desktop**

## ▶️ Executando o Projeto

1. Clone o repositório:

```
git clone https://github.com/Antonio-scripts/desafioCadastro.git

```

2. Suba o banco de dados e o cache Redis com Docker:

```
docker-compose up
```

3. Execute a aplicação:

```
mvn spring-boot:run
```

4. Acesse a documentação Swagger:

```
http://localhost:8080/swagger-ui/index.html
```

## 🧠 Funcionalidades

### 🐶 Pets
* Cadastrar pets
* Listar todos os pets
* Buscar pet por nome
* Buscar pet por sexo
* Buscar pet por idade
* Deletar pet pelo ID

### 👤 Tutores

* Listar todos os tutores
* Buscar tutor por nome
* Buscar tutor por email
* Deletar tutor pelo ID

##


* Documentação com Swagger
* Testes unitários

## 🧪 Testes

Os testes utilizam o **JUnit 5** e **Mockito**, garantindo validação das regras de negócio.

Exemplos:

* Cadastro de pet com sucesso
* Listagem de pets
* Validação de campos vazios
* Exceções para entradas inválidas

## 🐬 Subindo banco de dados com Docker

O projeto possui um banco de dados **Docker**, localizado em `docker-compose.yml`.
Para subir o banco de dados:

```
docker-compose up
```

## 📚 Documentação Swagger

Acesse:
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

🧠 Regras de negócio e validações

### 🐶 Pets
* A validação de pets é feita de forma manual através da classe `PetValidator`,
  isso devido a regras de negócio específicas da entidade.

Regras da entidade
- Substituição de campos vazios por `"NÃO_INFORMADO"`
- Validação de nome (nome e sobrenome)
- Bloqueio de caracteres especiais
- Idade máxima de 20 anos
- Peso mínimo de 0.5kg e máximo de 60kg


### 👤 Tutores
A validação de tutores utiliza **Bean Validation** diretamente na entidade,
por se tratar de regras simples e estruturais.
Anotações utilizadas:
- `@NotBlank` para validação do nome
- `@Email` para validação do e-mail


## ⚡ Cache com Redis

O projeto utiliza **Spring Cache** com **Redis** para otimizar consultas frequentes,
como a listagem de pets e tutores.

Anotações utilizadas:
- `@Cacheable`
- `@CacheEvict`

Isso reduz acessos repetidos ao banco de dados e melhora a performance da API.

## 🏗️ Arquitetura

O projeto segue o padrão de arquitetura em camadas:

- Controller
- Service
- Repository
- DTOs

## 🚧 Próximos passos (roadmap)

> Este roadmap representa ideias de evolução do projeto e pode ser ajustado conforme o desenvolvimento.

- Criar testes de integração
- Automatizações com GitHub Actions (CI)
- Dockerizar completamente a aplicação
- Publicar a aplicação em ambiente cloud (AWS)



🧑‍💻 Autor

Projeto desenvolvido por **Antonio Queiroz**

💼 GitHub - [Antonio-scripts](https://github.com/Antonio-scripts)

💼 Linkdin - [Antonio Queiroz](https://www.linkedin.com/in/antonio-queiroz-2983491a2/)

---