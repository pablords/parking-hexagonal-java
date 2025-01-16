Feature: Car Check-in

  Scenario: Successful car check-in
    Given the car with plate "ABC1234" is not checked in
    When the client sends a check-in request with "src/test/java/com/pablords/parking/contract/CT002/features/request/createCheckinSuccess.json"
    Then the response status should be 201
    # And the response should contain the plate "ABC1234"
    # And the response should contain a check-in timestamp

    # Scenario: Fail to check-in a car that is already checked in
    # Given the car with plate "ABC1234" is checked in
    # When the client sends a check-in request with plate "ABC1234"
    # Then the response status should be 500