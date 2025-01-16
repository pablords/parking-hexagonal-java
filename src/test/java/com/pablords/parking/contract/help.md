### Comparação: Testes de Contrato vs Testes de Componentes

| Característica               | Testes de Contrato | Testes de Componentes |
|------------------------------|-------------------|----------------------|
| Testa chamadas externas      | ✅ Sim           | ❌ Não               |
| Usa banco real (H2)          | ✅ Sim           | ❌ Não               |
| Usa `@MockBean`              | ❌ Não           | ✅ Sim               |
| Testa comportamento isolado  | ❌ Não           | ✅ Sim               |
| Rodam mais rápido            | ❌ Não           | ✅ Sim               |


1. Rodando testes por feature

```bash
mvn test -Pcontract-tests -Dcucumber.features=src/test/java/com/pablords/parking/contract/CT001/features
```

2. Rodando testes por tags

| Comando | O que faz? |
|---------|-----------|
| `mvn test -Pcontract-tests -Dcucumber.filter.tags="@checkin"` | Executa apenas cenários com a tag `@checkin`. |
| `mvn test -Pcontract-tests -Dcucumber.filter.tags="@checkin or @checkout"` | Executa cenários com `@checkin` OU `@checkout`. |
| `mvn test -Pcontract-tests -Dcucumber.filter.tags="@checkin and @success"` | Executa cenários com `@checkin` E `@success`. |
| `mvn test -Pcontract-tests -Dcucumber.filter.tags="@checkin and @fail"` | Executa cenários com `@checkin` E `@fail`. |
| `mvn test -Pcontract-tests -Dcucumber.filter.tags="not @ignore"` | Executa todos os cenários, exceto os que têm `@ignore`. |
| `mvn test -Pcontract-tests -Dcucumber.filter.tags="(@checkin or @checkout) and not @ignore"` | Executa `@checkin` OU `@checkout`, mas exclui `@ignore`. |
