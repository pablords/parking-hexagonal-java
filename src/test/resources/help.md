# 📌 Tabela Comparativa de Tipos de Testes

| **Critério**            | **Testes Unitários** | **Testes de Componentes** | **Testes de Contrato** | **Testes de Integração** |
|------------------------|-------------------|----------------------|-------------------|--------------------|
| **O que testa?** | Métodos e classes isoladas | Um componente específico (ex: Service, Controller) | Toda a aplicação isolada, mockando dependências externas | Integração entre a aplicação e sistemas externos |
| **Infraestrutura usada?** | ❌ Não | 🔹 Apenas infraestrutura interna (ex: banco H2) | 🔹 Mocka banco e APIs externas | ✅ Banco de dados real e APIs externas |
| **Fronteiras externas mockadas?** | ✅ Sim | ✅ Sim (mas pode testar repositórios reais) | ✅ Sim (banco, APIs externas e mensageria são mockados) | ❌ Não (tudo real) |
| **Velocidade de execução** | ⚡ Muito rápido | ⚡ Rápido | 🚀 Médio | 🐢 Lento |
| **Objetivo** | Validar regras de negócio | Validar funcionalidades isoladas | Validar comunicação e contrato da API | Validar a aplicação em um ambiente real |
| **Exemplo de ferramenta** | JUnit, Mockito | SpringBootTest + MockBean | Cucumber, Pact, MockServer | Testcontainers, WireMock |
| **Maior confiabilidade** | ❌ Baixa (testa apenas partes isoladas) | 🔹 Média (testa componentes, mas mocka partes) | ✅ Alta (testa toda a aplicação, mockando apenas o mundo externo) | ✅ Muito Alta (testa como o sistema funciona em produção) |

---

## 🏆 Quando usar cada tipo de teste?
- **Testes Unitários:** Para validar regras de negócio sem dependências externas.
- **Testes de Componentes:** Para validar componentes internos sem testar APIs externas.
- **Testes de Contrato:** Para garantir que a API se comporta corretamente mesmo mockando o mundo externo.
- **Testes de Integração:** Para validar como a aplicação interage com bancos reais, APIs e serviços externos.

1. Rodando testes por feature

```bash
mvn test -Pcomponent-test -Dcucumber.features=src/test/java/com/pablords/parking/component/CT001/features
```

```bash
mvn test -Pintegration-test -Dcucumber.features=src/test/java/com/pablords/parking/component/CT001/features
```

2. Rodando testes de contrato por tags

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