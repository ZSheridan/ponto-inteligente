# Ponto Inteligente

Esse projeto é uma API Restful para sistema de controle de ponto (em construção).

### Ferramentas:
* Spring Boot 2
* Kotlin 1.3.72
* No SQL Mongo DB
* Spring Security

### Utilização:
*Não Requer Autorização:*
* Registrar PJ. Exemplo:
 ```javascript
HTTP POST para "localhost:8080/api/register/pj" com body:
{
    "name": "Admin Name",
    "email": "admin@email.com",
    "password": "pass",
    "cpf": "123456789-04",
    "cnpj": "12312341230000-00",
    "companyName": "Company Name"
}
 ```
* Registrar PF. Exemplo:
 ```javascript
HTTP POST para "localhost:8080/api/register/pf" com body:
{
    "name": "User Name",
    "email": "user@email.com",
    "password": "pass",
    "cpf": "123456789-00",
    "cnpj": "12312341230000-00"
}
 ```
*Requer Autorização Específica (ADMIN):*
* Deletar uma Entry.

*Requer Autorização geral (Tanto ADMIN quanto USER):*
* Todas as outras requisições