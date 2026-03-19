# 🐾 PawsManager

[![CI](https://github.com/antonio-queiroz-dev/paws-manager/actions/workflows/ci.yml/badge.svg)](https://github.com/antonio-queiroz-dev/paws-manager/actions/workflows/ci.yml)

API REST desenvolvida com **Spring Boot 3**, **Java 21** e **MySQL** para o gerenciamento de pets e tutores em clínicas veterinárias.
O projeto adota práticas modernas de desenvolvimento, incluindo **autenticação JWT**, **versionamento de banco com Flyway**, **Dockerização completa**, **Testes de Integração com Testcontainers**, **Cache com Redis** e documentação via **Swagger UI**.

---

## 🚀 Tecnologias Utilizadas

* **Java 21** (LTS)
* **Spring Boot 3.5.6**
    * Spring Web
    * Spring Data JPA
    * Spring Data Redis
    * Spring Security
* **Autenticação**: JWT (JSON Web Token)
* **Banco de Dados**: MySQL 8
* **Versionamento de Schema**: Flyway
* **Cache**: Redis
* **Containerização**: Docker & Docker Compose
* **Testes**: JUnit 5 + Mockito · Testcontainers
* **Documentação**: SpringDoc OpenAPI (Swagger UI)
* **CI**: GitHub Actions + JaCoCo
* **Build**: Maven

---

## ⚙️ Pré-requisitos

* **Docker Desktop**
* **Git**

---

## ▶️ Como Executar

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/antonio-queiroz-dev/paws-manager.git
   cd paws-manager
   ```

2. **Suba a aplicação:**
   ```bash
   docker compose up --build
   ```
   *Aguarde até que todos os containers estejam prontos.*

3. **Acesse a API:**
   `http://localhost:8080`

---

## 🐳 Docker

A aplicação roda em containers orquestrados pelo Docker Compose. O `Dockerfile` utiliza **multi-stage build**, gerando uma imagem final menor baseada apenas no JRE.

Os três containers sobem com **healthcheck** configurado — a API só inicia após MySQL e Redis estarem prontos:

| Container             | Imagem       | Porta |
|-----------------------|--------------|-------|
| `pawsmanager_mysql`   | mysql:8.0    | 3306  |
| `pawsmanager_redis`   | redis:latest | 6379  |
| `pawsmanager_backend` | build local  | 8080  |

![Docker containers](pics/Docker%20imagens.png)

---

## 🔐 Autenticação

A API utiliza **Spring Security com JWT**. Todas as rotas (exceto `/auth/**`) exigem autenticação.

1. Registre um usuário: `POST /auth/register`
2. Faça login e obtenha o token: `POST /auth/login`
3. Envie o token no header: `Authorization: Bearer {token}`

---

## 📚 Documentação da API (Swagger)

Com a aplicação rodando, acesse e teste todos os endpoints:

👉 **[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)**

![Swagger UI](pics/Swagger.png)

---

## 🧠 Funcionalidades

### 🐶 Pets

* CRUD completo
* Busca por nome, sexo, idade e tutor
* Validações: idade máxima de 20 anos, peso entre 0.5kg e 60kg

### 👤 Tutores

* CRUD completo
* Validação de campos obrigatórios e formato de e-mail

---

## ⚡ Cache com Redis

Consultas frequentes são armazenadas em cache com TTL de 30 minutos. O cache é invalidado automaticamente quando os dados são alterados, mantendo as respostas sempre consistentes.

---

## 🗄️ Versionamento de Banco (Flyway)

Todas as alterações no banco de dados são feitas via arquivos SQL versionados, garantindo que qualquer ambiente — local, CI ou produção — sempre suba com o schema correto e atualizado.

---

## 🧪 Testes

O projeto conta com dois níveis de teste:

* **Unitários**: validam a lógica de negócio de forma isolada usando Mockito
* **Integração**: sobem um banco MySQL e Redis reais via Testcontainers, testando a aplicação de ponta a ponta

A cobertura é monitorada via **JaCoCo**, integrado ao pipeline de CI.

![jacoco](pics/jacoco.png)

```bash
mvn test
```

---

## 🏗️ Organização do Projeto

O projeto é organizado por funcionalidade (package-by-feature). Cada domínio como `pet` e `tutor` contém todas as suas classes juntas, facilitando a navegação e manutenção do código.

---

## 🚧 Roadmap

| Funcionalidade                        | Status       |
|---------------------------------------|--------------|
| Testes unitários                      | ✅ Concluído |
| Testes de integração (Testcontainers) | ✅ Concluído |
| Cache com Redis                       | ✅ Concluído |
| Dockerização completa                 | ✅ Concluído |
| CI com GitHub Actions + JaCoCo        | ✅ Concluído |
| Spring Security com JWT               | ✅ Concluído |
| Reorganização package-by-feature      | ✅ Concluído |
| Flyway (versionamento de schema)      | ✅ Concluído |
| CI melhorado (build Docker + GHCR)    | 🔄 Em breve  |
| Deploy na Oracle Cloud                | 🔄 Em breve  |
| Paginação                             | 🔄 Em breve  |
| Novos domínios (Clinic, Veterinarian) | 🔄 Em breve  |
| Mensageria com RabbitMQ               | 🔄 Em breve  |

---

## 🧑‍💻 Autor

Desenvolvido por **Antonio Queiroz**

💼 [LinkedIn](https://www.linkedin.com/in/antonio-queiroz-dev/) | 🐙 [GitHub](https://github.com/antonio-queiroz-dev)