# Home Assistant API Documentation

## Introdução

Este documento fornece uma visão geral e guia de uso da API do projeto Home Assistant, um sistema de automação residencial que permite o controle de aparelhos de ar condicionado via internet. O projeto é desenvolvido utilizando Java com Spring Boot e Maven, e utiliza um banco de dados PostgreSQL para persistência de dados.

## Configuração do Ambiente

### Pré-requisitos

- Java JDK 11 ou superior
- Maven 3.6.3 ou superior
- PostgreSQL 12 ou superior
- IDE de sua preferência (recomendado: IntelliJ IDEA)

### Configuração Inicial

1. Clone o repositório do projeto:
  git clone https://github.com/seuusuario/home-assistant.git
2. Entre no diretório do projeto:
  cd home-assistant
3. Execute o Maven para instalar as dependências:
  mvn clean install
4. Configure o arquivo `src/main/resources/application.properties` com as informações do seu banco de dados e chaves de API.
  #Configurações do banco de dados postgress com ddl-auto=update

  # Configuração da chave de API do OpenWeatherMap
  openweathermap.api.key=1308d784517f4abd2a2d904053e00dbe <- chave pra teste
  openweathermap.api.url=http://api.openweathermap.org/data/2.5

  # Configuração da chave de API do ipinfo.io
  ipinfo.api.key=59cb1365734f5a <- chave pra teste

###Licença
  Este projeto está licenciado sob a Licença MIT - veja o arquivo LICENSE para mais detalhes.  
###Contato
  Para mais informações, entre em contato através de mota.natasha.2020@gmail.com.
