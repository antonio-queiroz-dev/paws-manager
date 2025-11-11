# 🐾 Desafio cadastro de pets

API REST desenvolvida com **Spring Boot 3**, **Java 21** e **MySQL** que realiza o cadastro e gerenciamento de pets.
O projeto inclui integração com o **Swagger UI** para documentação automática e **Docker Compose** para subir o banco de dados.
-------------------------------------------------------------------------------------------------------------------------------

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

---

## ⚙️ Configuração do ambiente

### 🔧 Requisitos

* **JDK 21+**
* **Maven 3+**
* **Docker Desktop**

## ▶️ Executando o Projeto

1. Clone o repositório:

```
git clone https://github.com/Antonio-scripts/desafioCadastro-springboot-migration.git
```

2. Suba o banco de dados com Docker:

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

* Cadastrar pets
* Listar todos os pets
* Deletar pets pelo ID
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

🧠 Regras de negócio

* Validação de dados feita por `PetValidator`

    * Substitui campos vazios por "NÃO_INFORMADO"
    * Lança exceções para:

        * Nome sem sobrenome
        * Caracteres especiais em nome, idade, peso ou raça
        * Idade > 20
        * Peso < 0.5 ou > 60

🧑‍💻 Autor
Projeto desenvolvido por **Antonio Queiroz**
💼 GitHub - [Antonio-scripts](https://github.com/Antonio-scripts)

---