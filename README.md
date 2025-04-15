# beauty-book

## Descrição
Projeto Spring Boot de um sistema de agendamento de serviços de beleza, realizado para estudos de microserviços, clean architecture, testes e CI.

## Como usar

1. **Clonar o Repositório**

Primeiro, clone o repositório do projeto:

```bash
git clone https://github.com/angelovlima/beauty-book.git
```

2. **Subir o docker compose**

Rode o Docker Compose no diretório raiz do projeto, execute o seguinte comando:

```bash
docker-compose up --build
```

3. **Acessar a Documentação (Swagger)**

Após subir os containers e rodar os serviços, para acessar a documentação Swagger e visualizar os endpoints detalhados, acesse as seguintes URLs:

```bash
customer-management: http://localhost:8081/swagger-ui.html

establishment-management: http://localhost:8082/swagger-ui.html

professional-management: http://localhost:8083/swagger-ui.html

booking: http://localhost:8084/swagger-ui.html
```

4. **Descrição detalhada**

Para um detalhamento mais profundo, leia a documentação.pdf no repositório do projeto.
