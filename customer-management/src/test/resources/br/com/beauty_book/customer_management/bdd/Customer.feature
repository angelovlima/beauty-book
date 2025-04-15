Feature: Customer registration and management

  Scenario: Successfully register a new customer
    Given no customer exists with CPF "123.456.789-00"
    When I register a customer with the following data:
      | name         | Carla Menezes        |
      | cpf          | 123.456.789-00       |
      | phoneNumber  | 1199999-0000         |
      | email        | carla@email.com      |
    Then the customer should be created successfully

  Scenario: Should not allow registration with duplicated CPF
    Given a customer already exists with CPF "234.567.890-11"
    When I try to register another customer with the following data:
      | name         | João Mendes           |
      | cpf          | 234.567.890-11        |
      | phoneNumber  | 1199999-0001          |
      | email        | joao@email.com        |
    Then the response status should be 409
    And the response message should be "A customer with CPF 234.567.890-11 already exists."

  Scenario: Should not allow registration with invalid CPF
    When I register a customer with the following data:
      | name         | Felipe Martins        |
      | cpf          | 12345678900           |
      | phoneNumber  | 1198888-0000          |
      | email        | felipe@email.com      |
    Then the response status should be 400
    And the response message should contain "Invalid CPF"

  Scenario: Should not allow registration with invalid email
    When I register a customer with the following data:
      | name         | Ana Paula             |
      | cpf          | 321.654.987-00        |
      | phoneNumber  | 1197777-7777          |
      | email        | email-invalido        |
    Then the response status should be 400
    And the response message should contain "Invalid email format"

  Scenario: Should not allow registration with missing required fields
    When I try to register a customer with the following data:
      | name         |                      |
      | cpf          |                      |
      | phoneNumber  |                      |
      | email        |                      |
    Then the response status should be 400
    And the response message should contain "Invalid CPF"

  Scenario: Should list all registered customers
    Given the following customers are already registered:
      | name          | cpf              | phoneNumber   | email              |
      | Alex Silva    | 555.555.555-55   | 1190000-1111  | alex@email.com     |
      | Bia Souza     | 666.666.666-66   | 1190000-2222  | bia@email.com      |
    When I list all customers
    Then the response status should be 200
    And the response should contain at least 2 customers
    And the response should contain a customer with name "Alex Silva" and CPF "555.555.555-55"
    And the response should contain a customer with name "Bia Souza" and CPF "666.666.666-66"

  Scenario: Should find a customer by ID successfully
    Given a customer is registered with the following data:
      | name         | Diego Ramos          |
      | cpf          | 777.888.999-00       |
      | phoneNumber  | 1191234-5678         |
      | email        | diego@email.com      |
    When I search for the customer by the registered ID
    Then the response status should be 200
    And the response should contain the customer name "Diego Ramos"

  Scenario: Should return 404 when customer ID does not exist
    When I search for the customer by ID 99999
    Then the response status should be 404
    And the response message should be "Customer with ID 99999 not found"

  Scenario: Should update a customer successfully
    Given a customer is registered with the following data:
      | name         | Carla Souza           |
      | cpf          | 888.888.888-88        |
      | phoneNumber  | 1199999-4444          |
      | email        | carla.souza@email.com |
    When I update the customer by registered ID with the following data:
      | name         | Carla Silva           |
      | cpf          | 888.888.888-88        |
      | phoneNumber  | 1199999-5555          |
      | email        | carla.silva@email.com |
    Then the response status should be 200
    And the response should contain the name "Carla Silva" and phoneNumber "1199999-5555"

  Scenario: Should delete a customer successfully
    Given a customer is registered with the following data:
      | name         | Bruno Souza           |
      | cpf          | 999.999.999-00        |
      | phoneNumber  | 1188888-0000          |
      | email        | bruno@email.com       |
    When I delete the customer by registered ID
    Then the response status should be 204