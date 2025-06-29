# Home Assistant API Documentation

## Introdução

Este documento fornece uma visão geral e guia de uso da API do projeto Home Assistant, um sistema de automação residencial que permite o controle de aparelhos de ar condicionado via internet. O projeto é desenvolvido utilizando Java com Spring Boot, Maven, e segue os princípios da Arquitetura Hexagonal (Ports & Adapters) para garantir modularidade e testabilidade.

O sistema também utiliza filas de mensagens com MQTT para comunicação assíncrona entre componentes e integra serviços de geolocalização para personalização baseada na localização do usuário.

## Configuração do Ambiente

### Pré-requisitos

- Java JDK 11+
- Maven 3.6.3+
- PostgreSQL 12+
- Broker MQTT (ex: Mosquitto, EMQX)
- IDE (recomendado: IntelliJ IDEA ou VS Code)

### Configuração Inicial

1. Clone o repositório do projeto:
  git clone https://github.com/seuusuario/home-assistant.git
2. Entre no diretório do projeto:
  cd home-assistant
3. Execute o Maven para instalar as dependências:
  mvn clean install
4. Configure o arquivo `src/main/resources/application.properties` com as informações do seu banco de dados e chaves de API.
  - Configurações do banco de dados postgress com ddl-auto=update
  - Configuração da chave de API do OpenWeatherMap
   -   openweathermap.api.key=1308d784517f4abd2a2d904053e00dbe <- chave pra teste
   -  openweathermap.api.url=http://api.openweathermap.org/data/2.5
  - Configuração da chave de API do ipinfo.io
   -   ipinfo.api.key=59cb1365734f5a <- chave pra teste

### Arquitetura Hexagonal (Ports & Adapters)
O projeto segue a Arquitetura Hexagonal, separando a lógica de negócios (core) dos detalhes de infraestrutura (adapters).

## Estrutura Principal:
- Core (Domínio):
  Contém as entidades, regras de negócio e interfaces (Ports).
  Ex: AirConditionerService (caso de uso), DeviceRepository (porta de saída).

-Adapters:
  Driven (Saída): Banco de dados (PostgreSQL), APIs externas (OpenWeatherMap).
  Driving (Entrada): REST API (Spring MVC), MQTT (mensageria).

Benefícios:
✔ Baixo acoplamento
✔ Fácil substituição de tecnologias
✔ Testabilidade aprimorada

##  Comunicação Assíncrona com MQTT
O sistema utiliza MQTT para:

- Publicar eventos (ex: mudança de temperatura).
- Assinar tópicos (ex: home/airconditioner/control).

##  Geolocalização
Integração com IPInfo API para obter:

- Localização do usuário (cidade, país).
- Temperatura local (via OpenWeatherMap).

## Testes com JUnit e Mockito
- O projeto inclui testes unitários e de integração

### Licença
  Este projeto está licenciado sob a Licença MIT - veja o arquivo LICENSE para mais detalhes.  
### Contato
  Para mais informações, entre em contato através de mota.natasha.2020@gmail.com.
