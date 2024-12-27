# parking-hexagonal-java

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
│               │   ├── CarModel.java
│               │   ├── CheckinModel.java
│               │   ├── CheckoutModel.java
│               │   └── SlotModel.java
│               └── repositories
│                   ├── CarRepositoryAdapter.java
│                   ├── CheckinRepositoryAdapter.java
│                   ├── CheckoutRepositoryAdapter.java
│                   ├── JpaRepositoryCar.java
│                   ├── JpaRepositoryCheckin.java
│                   ├── JpaRepositoryCheckout.java
│                   ├── JpaRepositorySlot.java
│                   └── SlotRepositoryAdapter.java
├── config
│   ├── BeanConfiguration.java
│   └── SwaggerConfig.java
└── core
    ├── entities
    │   ├── Car.java
    │   ├── Checkin.java
    │   ├── Checkout.java
    │   └── Slot.java
    ├── exceptions
    │   ├── CheckinTimeMissingException.java
    │   ├── ErrorMessages.java
    │   ├── ExistPlateException.java
    │   ├── InvalidPlateException.java
    │   ├── ParkingFullException.java
    │   └── SlotOcupiedException.java
    ├── ports
    │   ├── inbound
    │   │   └── services
    │   │       └── CarServicePort.java
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
    └── valueObjects
        └── Plate.java

```

## Referência
![Hexagonal Architecture](/.github/hexagonal.webp)

