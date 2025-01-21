#language: pt

@checkin
Funcionalidade: Check-in de Carros
  @success
  Cenário: Check-in bem-sucedido de um carro
    Dado que o carro com placa "ABC1234" não está estacionado
    Quando o cliente envia uma solicitação de check-in com "src/test/java/com/pablords/parking/resources/features/request/createCheckinSuccess.json"
    Então o status da resposta do checkin deve ser 201
    E a resposta deve conter um timestamp de check-in

  @fail
  Cenário: Falha ao realizar check-in de um carro já estacionado
    Dado que o carro com placa "ABC1234" está estacionado
    Quando o cliente envia uma solicitação de check-in inválida com "src/test/java/com/pablords/parking/resources/features/request/createCheckinSuccess.json"
    Então o status da resposta do checkin deve ser 400
