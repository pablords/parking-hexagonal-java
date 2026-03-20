# 📌 Tabela Comparativa de Tipos de Testes

| **Critério**            | **Testes Unitários** | **Testes de Componentes** | **Testes de Contrato** | **Testes de Integração** |
|------------------------|-------------------|----------------------|-------------------|--------------------|
| **O que testa?** | Métodos e classes isoladas | Todo o fluxo da aplicação mockando fronteiras externas | Contrato de comunicação entre serviços | Integração entre a aplicação e sistemas externos |
| **Infraestrutura usada?** | ❌ Não | 🔹 Mocka banco e APIs externas | 🔹 Mocka banco e APIs externas | ✅ Banco de dados real e APIs externas |
| **Fronteiras externas mockadas?** | ✅ Sim | ✅ Sim | ✅ Sim | ❌ Não (tudo real) |
| **Velocidade de execução** | ⚡ Muito rápido | 🚀 Médio | 🚀 Médio | 🐢 Lento |
| **Objetivo** | Validar regras de negócio | Validar fluxos de negócio fim a fim em isolamento | Validar comunicação e contrato da API | Validar a aplicação em um ambiente real |
| **Exemplo de ferramenta** | JUnit, Mockito | SpringBootTest, Cucumber, MockBean | Pact, MockServer | Testcontainers, WireMock |
| **Maior confiabilidade** | ❌ Baixa (testa apenas partes isoladas) | ✅ Alta (testa toda a aplicação em isolamento) | 🔹 Média (foca apenas no contrato) | ✅ Muito Alta (testa como o sistema funciona em produção) |

---

## 🏆 Quando usar cada tipo de teste?
- **Testes Unitários:** Para validar regras de negócio sem dependências externas.
- **Testes de Componentes:** Para validar o fluxo completo da aplicação mockando as fronteiras externas.
- **Testes de Contrato:** Para garantir que o contrato entre serviços não seja quebrado.
- **Testes de Integração:** Para validar como a aplicação interage com bancos reais, APIs e serviços externos.

1. Rodando testes por feature

```bash
mvn test -Pcomponent-test -Dcucumber.features=src/test/java/com/pablords/parking/component/CT001/features
```

```bash
mvn test -Pintegration-test -Dcucumber.features=src/test/java/com/pablords/parking/component/CT001/features
```

2. Rodando testes de componente por tags

| Comando | O que faz? |
|---------|-----------|
| `mvn test -Pcomponent-test -Dcucumber.filter.tags="@checkin"` | Executa apenas cenários com a tag `@checkin`. |
| `mvn test -Pcomponent-test -Dcucumber.filter.tags="@checkin or @checkout"` | Executa cenários com `@checkin` OU `@checkout`. |
| `mvn test -Pcomponent-test -Dcucumber.filter.tags="@checkin and @success"` | Executa cenários com `@checkin` E `@success`. |
| `mvn test -Pcomponent-test -Dcucumber.filter.tags="@checkin and @fail"` | Executa cenários com `@checkin` E `@fail`. |
| `mvn test -Pcomponent-test -Dcucumber.filter.tags="not @ignore"` | Executa todos os cenários, exceto os que têm `@ignore`. |
| `mvn test -Pcomponent-test -Dcucumber.filter.tags="(@checkin or @checkout) and not @ignore"` | Executa `@checkin` OU `@checkout`, mas exclui `@ignore`. |

3. Rodando testes de integração por tags

| Comando | O que faz? |
|---------|-----------|
| `mvn test -Pintegration-test -Dcucumber.filter.tags="@checkin"` | Executa apenas cenários com a tag `@checkin`. |
| `mvn test -Pintegration-test -Dcucumber.filter.tags="@checkin or @checkout"` | Executa cenários com `@checkin` OU `@checkout`. |
| `mvn test -Pintegration-test -Dcucumber.filter.tags="@checkin and @success"` | Executa cenários com `@checkin` E `@success`. |
| `mvn test -Pintegration-test -Dcucumber.filter.tags="@checkin and @fail"` | Executa cenários com `@checkin` E `@fail`. |
| `mvn test -Pintegration-test -Dcucumber.filter.tags="not @ignore"` | Executa todos os cenários, exceto os que têm `@ignore`. |
| `mvn test -Pintegration-test -Dcucumber.filter.tags="(@checkin or @checkout) and not @ignore"` | Executa `@checkin` OU `@checkout`, mas exclui `@ignore`. |


4. Rodando testes de contratos com pact-broker:

- Antes é necessário publicar o contrato do consumer:

Vá até `consumer-api` na raiz do repositório e leia as instruções

- Comente a linha no arquivo `ProviderContractTest.java`
```bash
@PactFolder("consumer-api/pacts")
```

- Remova os comentários da linha no arquivo `ProviderContractTest.java`
```bash
@PactBroker(url = "http://localhost:9292", authentication = @PactBrokerAuth(username = "admin", password = "password"))
```

- Rode o teste:
```bash
  mvn test -Pcontract-tests
```
