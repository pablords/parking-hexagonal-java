## Consumer API

1. Rode o teste que irá gerar o arquivo `pacts/ParkingApp-ParkingService.json` 

```bash
npm run test
```

2. Publique o teste de consumer

- Suba o broker:
```bash
docker compose up pact-broker
```
  - Publique o contrato:

```bash
pact-broker publish pacts \
  --consumer-app-version 1.0.1 \
  --broker-base-url http://localhost:9292
```
