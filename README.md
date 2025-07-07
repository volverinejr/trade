## 🔧 Status e Qualidade

[![Entrega - OWASP Dependency Check](https://github.com/volverinejr/trade/actions/workflows/CI.yml/badge.svg)](https://github.com/volverinejr/trade/actions/workflows/CI.yml)
[![codecov](https://codecov.io/gh/volverinejr/trade/graph/badge.svg?token=IX01AUR2EG)](https://codecov.io/gh/volverinejr/trade)
![Java](https://img.shields.io/badge/Java-21-blue.svg)
![Build Time](https://img.shields.io/badge/Build~Time-~1~min-brightgreen)



# Trade

Sistema para gerenciamento de campeonatos, equipes e jogos com integração de testes automatizados, segurança e cobertura de código.

## 🧪 Como Executar os Testes

Este projeto possui testes automatizados de integração e API com cobertura, segurança e isolamento via containers.

### ✔️ Pré-requisitos

- Java 21+
- Maven 3.8+
- Docker (necessário para Testcontainers)

> 🔒 Os testes utilizam **Testcontainers** para subir instâncias isoladas de:
> - **MySQL** (banco de dados)
> - **Redis** (cache)
> - E acessam autenticação com token via endpoint `/newapi/token`

---

### ▶️ Rodando os testes localmente

Execute o comando:

```bash
mvn clean verify
```

Esse comando irá:

1. Compilar o projeto
2. Subir os containers necessários via Testcontainers
3. Executar todos os testes com `Rest-Assured` e `JUnit 5`
4. Validar regras de cobertura com **JaCoCo**
5. Gerar relatórios em:
   - `target/site/jacoco/index.html`
   - `target/dependency-check-report.html`

---

### ⚠️ Importante

- Certifique-se de que o Docker está **ativo e funcional**
- Variáveis de ambiente como `TEST_USERNAME`, `TEST_PASSWORD` e `REDIS_HOST` devem estar setadas localmente:

```bash
set TEST_USERNAME=meu-usuario
set TEST_PASSWORD=123456
set REDIS_HOST=localhost
```

Ou definidas via IDE (ex: no STS4/IntelliJ)

---

### 📦 Relatórios gerados

| Relatório          | Local                          |
|--------------------|---------------------------------|
| Cobertura (JaCoCo) | `target/site/jacoco/index.html` |
| Vulnerabilidades   | `target/dependency-check-report.html` |

---

### 🧪 Rodando só os testes (sem cobertura/OWASP):

```bash
mvn test
```