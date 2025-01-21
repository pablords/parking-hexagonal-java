#language: pt 

@car
Funcionalidade: Gerenciamento de Carros
  Como um usuário
  Quero gerenciar carros no estacionamento
  Para que eu possa validar o comportamento da API

  @success
  Cenário: Criar um carro com sucesso
    Dado que estou no endpoint da API "/cars"
    Quando eu crio um carro com os seguintes detalhes: "src/test/java/com/pablords/parking/resources/features/request/createCarSuccess.json"
    Então o status da resposta do carro deve ser 201

  @fail
  Cenário: Falha ao criar um carro com detalhes inválidos
    Quando eu crio um carro com os seguintes detalhes: "src/test/java/com/pablords/parking/resources/features/request/createCarInvalid.json"
    Então o status da resposta do carro deve ser 422
