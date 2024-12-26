# parking-api-hexagonal-java

Api de controler de entrada e saida de veiculos estacionamento

```tree
parking
├── ParkingApplication.java
├── adapters
│   ├── inbound
│   │   └── http
│   │       ├── controllers
│   │       │   └── CarController.java
│   │       ├── dtos
│   │       │   ├── req
│   │       │   │   └── CarRequestDTO.java
│   │       │   └── res
│   │       │       └── CarResponseDTO.java
│   │       ├── handlers
│   │       │   ├── ApiError.java
│   │       │   └── GlobalExceptionHandler.java
│   │       └── mappers
│   │           └── CarMapper.java
│   └── outbound
│       └── database
│           └── jpa
│               ├── mappers
│               │   └── CarMapper.java
│               ├── models
│               │   └── CarModel.java
│               └── repositories
│                   ├── CarRepositoryAdapter.java
│                   └── JpaRepositoryCar.java
├── config
│   ├── BeanConfiguration.java
│   └── SwaggerConfig.java
└── core
    ├── entities
    │   └── Car.java
    ├── exceptions
    │   ├── ExistPlateException.java
    │   └── InvalidPlateException.java
    ├── ports
    │   ├── inbound
    │   │   └── services
    │   │       └── CarServicePort.java
    │   └── outbound
    │       └── repositories
    │           └── CarRepositoryPort.java
    ├── services
    │   └── CarServiceImpl.java
    ├── utils
    │   └── StringUtils.java
    └── valueObjects
        └── Plate.java

```

## Referência
![Hexagonal Architecture](/.github/hexagonal.webp)

