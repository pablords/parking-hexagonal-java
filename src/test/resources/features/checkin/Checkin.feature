#language: pt

@checkin @CT002
Funcionalidade: Check-in de Carros
  @success
  Cenário: Check-in bem-sucedido de um carro
    Dado que o carro com placa "ABC1234" não está estacionado
    Quando o cliente envia uma solicitação de check-in com "src/test/resources/features/requests/create-checkin-201.json"
    Então o status da resposta do checkin deve ser 201
    Então o slot com id 1 deve ser ocupado
    E a resposta deve conter um timestamp de check-in

  @fail
  Cenário: Falha ao realizar check-in de um carro já estacionado
    Dado que o carro com placa "ABC1234" está estacionado
    Quando o cliente envia uma solicitação de check-in inválida com "src/test/resources/features/requests/create-checkin-201.json"
    Então o status da resposta do checkin deve ser 400

  @fail
  Cenário: Falha ao realizar check-in de um carro com placa inválida
    Dado que o carro com placa "ABC1234" não está estacionado
    Quando o cliente envia uma solicitação de check-in inválida com "src/test/resources/features/requests/create-checkin-422.json"
    Então o status da resposta do checkin deve ser 422