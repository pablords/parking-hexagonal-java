# parking-hexagonal-java

Controle de entrada e Saida de veículos estacionamento


## Swagger

http://localhost:8080/api/v1/swagger-ui/index.html

## h2-db

http://localhost:8080/api/v1/h2-console

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

## Referências

[Netflix Tech Blog](https://netflixtechblog.com/ready-for-changes-with-hexagonal-architecture-b315ec967749)

![Hexagonal Architecture](/.github/hexagonal.webp)

