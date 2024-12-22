# parking-api-hexagonal-java

Api de controler de entrada e saida de veiculos estacionamento

```tree
parking
├── ParkingApplication.java
├── adapters
│   ├── inbound
│   │   └── api
│   │       ├── controllers
│   │       │   └── CarController.java
│   │       ├── dtos
│   │       │   └── CreateCarDTO.java
│   │       ├── handlers
│   │       │   └── GlobalExceptionHandler.java
│   │       └── mappers
│   │           └── CarMapper.java
│   └── outbound
│       ├── mappers
│       │   └── CarMapper.java
│       ├── models
│       │   └── CarModel.java
│       └── repositories
│           ├── CarRepositoryAdapter.java
│           └── JpaRepositoryCar.java
├── config
│   └── BeanConfiguration.java
└── core
    ├── entities
    │   └── Car.java
    ├── exceptions
    │   └── InvalidPlateException.java
    ├── ports
    │   ├── inbound
    │   │   └── services
    │   │       └── CarServicePort.java
    │   └── outbound
    │       └── repository
    │           └── CarRepositoryPort.java
    ├── services
    │   └── CarServiceImpl.java
    └── valuesObjects
        └── Plate.java

```

## Referência
![Hexagonal Architecture](/.github/hexagonal.webp)

