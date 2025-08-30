## Consumer API

1. Rode o teste que irá gerar o arquivo `pacts/ParkingApp-ParkingService.json` 

```bash
npm run test
```

2. Instale o Pact CLI (caso ainda não tenha)

Você pode instalar o Pact CLI globalmente usando npm:

```bash
npm install -g @pact-foundation/pact-cli
```

Ou via Homebrew (Mac):

```bash
brew install pactfoundation/pact/pact-ruby-standalone
```

Mais detalhes: https://docs.pact.io/implementation_guides/cli

3. Suba o broker:

```bash
docker compose up pact-broker
```

4. Publique o contrato gerado:

```bash
pact-broker publish pacts \
  --consumer-app-version 1.0.1 \
  --broker-base-url http://localhost:9292
```

> **Dica:**
> - O arquivo gerado em `pacts/` representa o contrato entre consumer e provider.
> - O broker pode ser acessado em http://localhost:9292 para visualizar e gerenciar contratos.
> - Consulte a [documentação oficial do Pact](https://docs.pact.io/) para mais opções e integrações.
