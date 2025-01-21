# parking-hexagonal-java

Controle de entrada e Saida de veículos estacionamento.


## Arquitetura e Design

Este projeto utiliza a arquitetura hexagonal (também conhecida como Ports and Adapters) para organizar o código de forma que seja fácil de manter, testar e escalar. A arquitetura hexagonal promove a separação de preocupações, permitindo que a lógica de negócios seja independente de frameworks, bancos de dados e outros detalhes de implementação.

### Estrutura do Projeto

A estrutura do projeto é organizada da seguinte forma:


```tree
parking
├── ParkingApplication.java
├── adapters
│   ├── inbound
│   │   └── http
│   │       ├── config
│   │       │   └── SwaggerConfig.java
│   │       ├── controllers
│   │       │   ├── CarController.java
│   │       │   ├── CheckinController.java
│   │       │   └── CheckoutController.java
│   │       ├── dtos
│   │       │   ├── CarRequestDTO.java
│   │       │   ├── CarResponseDTO.java
│   │       │   ├── CheckinRequestDTO.java
│   │       │   ├── CheckinResponseDTO.java
│   │       │   ├── CheckoutRequestDTO.java
│   │       │   └── CheckoutResponseDTO.java
│   │       ├── handlers
│   │       │   ├── ApiError.java
│   │       │   └── GlobalExceptionHandler.java
│   │       └── mappers
│   │           ├── CarMapper.java
│   │           └── CheckinMapper.java
│   └── outbound
│       ├── database
│       │   └── jpa
│       │       ├── mappers
│       │       │   ├── CarMapper.java
│       │       │   ├── CheckinMapper.java
│       │       │   ├── CheckoutMapper.java
│       │       │   └── SlotMapper.java
│       │       ├── models
│       │       │   ├── CarModel.java
│       │       │   ├── CheckinModel.java
│       │       │   ├── CheckoutModel.java
│       │       │   └── SlotModel.java
│       │       └── repositories
│       │           ├── CarRepositoryAdapter.java
│       │           ├── CheckinRepositoryAdapter.java
│       │           ├── CheckoutRepositoryAdapter.java
│       │           ├── JpaRepositoryCar.java
│       │           ├── JpaRepositoryCheckin.java
│       │           ├── JpaRepositoryCheckout.java
│       │           ├── JpaRepositorySlot.java
│       │           └── SlotRepositoryAdapter.java
│       └── messaging
│           ├── config
│           │   └── RabbitMQConfig.java
│           └── producers
│               └── CheckoutProducerAdapter.java
├── config
│   └── BeanConfiguration.java
└── core
    ├── entities
    │   ├── Car.java
    │   ├── Checkin.java
    │   ├── Checkout.java
    │   └── Slot.java
    ├── exceptions
    │   ├── CarNotFoundException.java
    │   ├── CheckinNotFoundException.java
    │   ├── CheckinTimeMissingException.java
    │   ├── ErrorMessages.java
    │   ├── ExistPlateException.java
    │   ├── InvalidCheckinException.java
    │   ├── InvalidPlateException.java
    │   ├── ParkingFullException.java
    │   └── SlotOccupiedException.java
    ├── ports
    │   ├── inbound
    │   │   └── services
    │   │       ├── CarServicePort.java
    │   │       ├── CheckinServicePort.java
    │   │       ├── CheckoutServicePort.java
    │   │       └── SlotServicePort.java
    │   └── outbound
    │       └── repositories
    │           ├── CarRepositoryPort.java
    │           ├── CheckinRepositoryPort.java
    │           ├── CheckoutRepositoryPort.java
    │           └── SlotRepositoryPort.java
    ├── services
    │   ├── CarService.java
    │   ├── CheckinService.java
    │   ├── CheckoutService.java
    │   └── SlotService.java
    ├── utils
    │   └── StringUtils.java
    └── valueobjects
        └── Plate.java

```


### Componentes Principais

#### 1. `ParkingApplication.java`

Este é o ponto de entrada da aplicação Spring Boot. Ele configura a aplicação e inicia o contexto do Spring.

#### 2. Adapters

Os adapters são responsáveis por conectar a aplicação com o mundo externo. Eles são divididos em `inbound` e `outbound`.

- **Inbound Adapters**: Recebem as requisições externas e as encaminham para a lógica de negócios.
  - `http.config`: Contém configurações específicas para os endpoints HTTP, como o SwaggerConfig.
  - `http.controllers`: Contém os controladores REST que expõem as APIs HTTP.
  - `http.dtos`: Contém os Data Transfer Objects usados para transferir dados entre a camada de apresentação e a lógica de negócios.
  - `http.handlers`: Contém manipuladores globais de exceções e erros.
  - `http.mappers`: Contém os mapeadores que convertem entre entidades de domínio e DTOs.

- **Outbound Adapters**: Conectam a aplicação a sistemas externos, como bancos de dados e sistemas de mensageria.
  - `database.jpa.mappers`: Converte entre entidades de domínio e entidades de persistência.
  - `database.jpa.models`: Contém os modelos de persistência.
  - `database.jpa.repositories`: Contém os repositórios JPA para acesso ao banco de dados.
  - `messaging.config`: Configurações de mensageria, como RabbitMQConfig.
  - `messaging.producers`: Contém os produtores de mensagens para sistemas de mensageria, como RabbitMQ.

#### 3. Config
  - `BeanConfiguration`: é usada para definir beans personalizados que serão gerenciados pelo Spring. Esta classe foi criada para garantir que os serviços de negócios não dependam diretamente de um framework específico, como o Spring. Isso mantém a lógica de negócios independente e alinhada aos princípios da arquitetura hexagonal.

#### 4. Core

O núcleo da aplicação contém a lógica de negócios e é independente de frameworks e detalhes de implementação.

- **Entities**: Contém as entidades de domínio.
  - `Car.java`, `Checkin.java`, `Checkout.java`, `Slot.java`

- **Services**: Contém os serviços de negócios que implementam a lógica de negócios.
  - `CarService.java`, `CheckinService.java`, `CheckoutService.java`, `SlotService.java`

- **Exceptions**: Contém as exceções de domínio.
  - `CarNotFoundException.java`, `CheckinNotFoundException.java`, `InvalidPlateException.java`

- **Ports**: Define as interfaces para os serviços e repositórios.
  - `inbound.services`: Interfaces para os serviços de negócios.
  - `outbound.repositories`: Interfaces para os repositórios.

- **Utils**: Contém utilitários auxiliares.
  - `StringUtils.java`

- **Value Objects**: Contém os objetos de valor do domínio.
  - `Plate.java`


### Configurações

As configurações da aplicação estão nos arquivos `application-dev.yml` e `application-prod.yml`, que definem as configurações para os ambientes de desenvolvimento e produção, respectivamente.

### Fluxo de Dados

1. **Requisição HTTP**: Uma requisição HTTP é recebida por um controlador REST (`controllers`).
2. **DTO**: O controlador converte a requisição em um DTO (`dtos`).
3. **Mapper**: O DTO é convertido em uma entidade de domínio por um mapper (`mappers`).
4. **Serviço**: A entidade de domínio é passada para um serviço de negócios (`services`), que contém a lógica de negócios.
5. **Repositório**: O serviço interage com os repositórios (`repositories`) para acessar o banco de dados.
6. **Resposta**: O serviço retorna uma entidade de domínio que é convertida em um DTO por um mapper e enviada de volta pelo controlador.

### Mensageria

A aplicação utiliza RabbitMQ para enviar mensagens de checkout. O `CheckoutProducerAdapter` é responsável por serializar a mensagem e enviá-la para a fila RabbitMQ.

### Exceções

As exceções de domínio são lançadas quando ocorrem erros específicos do domínio, como `CarNotFoundException` e `InvalidPlateException`.

### Conclusão

A arquitetura hexagonal utilizada neste projeto promove a separação de preocupações, facilitando a manutenção, testes e escalabilidade da aplicação. A lógica de negócios é mantida independente de frameworks e detalhes de implementação, permitindo que a aplicação seja facilmente adaptável a mudanças.

## Referências

[Netflix Tech Blog](https://netflixtechblog.com/ready-for-changes-with-hexagonal-architecture-b315ec967749)

![Hexagonal Architecture](/.github/hexagonal.webp)


## Como Rodar o Projeto

### Pré-requisitos

Certifique-se de ter os seguintes softwares instalados em sua máquina:

- [Java 17+](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
- [Maven 3.6+](https://maven.apache.org/download.cgi)
- [Docker](https://www.docker.com/products/docker-desktop) (para rodar o RabbitMQ e Mysql)

### Passos para Rodar o Projeto

1. **Clone o repositório**

```bash
  git clone https://github.com/pablords/parking-hexagonal-java.git
  cd parking-hexagonal-java
```

2. **Compile o projeto e baixe as dependências**
```bash
  mvn clean install
```

3. **Suba o container do RabbitMQ**
```bash
  docker compose up -d rabbitmq
```

4. **Execute a aplicação**
```bash
  mvn spring-boot:run
```

5. **Acesse a aplicação**

A aplicação estará disponível em http://localhost:8080.

Documentação da API
- A documentação interativa da API gerada pelo Swagger estará disponível em http://localhost:8080/api/v1/swagger-ui/index.html.

Banco de Dados
- Por padrão o profile habilitado é o `dev` e com isso sobe automaticamente um banco em memoria `h2` e o console dele estará disponível em http://localhost:8080/api/v1/h2-console

Variáveis de Ambiente
- Você pode configurar variáveis de ambiente específicas no arquivo application.properties ou application.yml localizado em resources.

6. **Testes**

Rodando testes unitários:

- Testamos nossos serviços, onde o núcleo da nossa lógica de negócios vive, mas é independente de qualquer tipo de persistência ou transporte

```bash
  mvn test -Punit-test
```

Rodando testes de componentes:

- Testamos toda a pilha da nossa camada de Http/API, pelos serviços e repositórios, validando nosso sistema mockando todos detalhes de infra e sistemas externos.

```bash
  mvn test -Pcomponent-test
```

Rodando testes de integração:

- Testamos toda a pilha da nossa camada de Http/API, pelos serviços, repositórios, fontes de dados e serviços externos atingidos. Essas especificações testam se "conectamos" tudo corretamente.

```bash
  mvn test -Pintegration-test
```
