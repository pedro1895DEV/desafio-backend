- Para rodar, acesse a pasta docker utilizando o comando cd /src/main/docker e utilize o comando docker-compose up
- Os endpoints da aplicação são:
   * http://localhost:8080/hello   - para testar se está funcionando
   * http://localhost:8080/user/create   - Cria usuário
   * http://localhost:8080/user/login   - Faz login e recebe o JWT
   * http://localhost:8080/user/all   - Para exibir todos os usuários, caso seja ADM
 
- Exemplo de JSON a ser utilizado no endpoint http://localhost:8080/user/create:

  {
  "name": "Fulano de tal",
  "cpf": "12345678901",
  "emails": [
    {
      "email": "fulano@email.com"
    },
    {
      "email": "fulano1@email.com"
    }
  ],
  "phones": [
    {
      "number": "11987654321",
      "type": "MOBILE"
    },
    {
      "number": "1122334455",
      "type": "COMMERCIAL"
    }
  ],
  "password": "senha123",
  "role": [
    "ROLE_ADMINISTRATOR"
  ],
  "address": {
    "cep": "12345-678",
    "patio": "Rua Azul, 123",
    "neighborhood": "Centro",
    "city": "Brasília",
    "uf": "DF",
    "complement": "Apartamento 45"
  }
}


- Exemplo de JSON para login:

{
    "email": "fulano@email.com",
    "password":"senha123"
}
