#language: pt

@checkout @CT003
Funcionalidade: Checkout de Carros
  @success
  Cenário: Checkout bem-sucedido de um carro
    Dado que o carro com placa "ABC1234" está estacionado
    Quando o cliente envia uma solicitação de checkout com a placa "src/test/resources/features/requests/create-checkout-201.json"
    Então o status da resposta do checkout deve ser 201
    E o slot com id 1 deve ser liberado
    E a resposta deve conter um timestamp de checkout

  @fail
  Cenário: Falha ao realizar checkout de um carro não estacionado
    Dado que o carro com placa "ABC1234" não está estacionado
    Quando o cliente envia uma solicitação de checkout inválida com "src/test/resources/features/requests/create-checkout-201.json"
    Então o status da resposta do checkout deve ser 404