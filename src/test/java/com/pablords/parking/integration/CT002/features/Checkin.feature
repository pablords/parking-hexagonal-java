@checkin
Feature: Car Check-in
    @success
    Scenario: Successful car check-in
        Given the car with plate "ABC1234" is not checked in
        When the client sends a check-in request with "src/test/java/com/pablords/parking/integration/CT002/features/request/createCheckinSuccess.json"
        And the response status should be 201
        And the response should contain a check-in timestamp

    @fail
    Scenario: Fail to check-in a car that is already checked in
        Given the car with plate "ABC1234" is checked in
        When the client sends a check-in request with "src/test/java/com/pablords/parking/integration/CT002/features/request/createCheckinSuccess.json"
        Then the response status should be 500