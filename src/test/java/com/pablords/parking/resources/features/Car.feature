@car
Feature: Car Management
  As a user
  I want to manage cars in the parking lot
  So that I can validate the API behavior

  @success
  Scenario: Successfully create a car
    Given That I am in the api endpoint "/cars"
    When I create a car with the following details: "src/test/java/com/pablords/parking/resources/features/request/createCarSuccess.json"
    Then The response status should be 201
    
  @fail
  Scenario: Fail to create a car with invalid details
    When I create a car with the following details: "src/test/java/com/pablords/parking/resources/features/request/createCarInvalid.json"
    Then The response status should be 422
