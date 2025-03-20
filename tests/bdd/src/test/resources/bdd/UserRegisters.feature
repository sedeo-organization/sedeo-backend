Feature: User registers and logins

  Scenario: User registers and logins
    Given users email 'michal.dabkowski@gmail.com', first name 'Michał', last name 'Dąbkowski'
    And phone number '512678126' and password 'StrongPassword123#'
    When user registers
    Then user should be created successfully
    When user tries to log in with email 'michal.dabkowski@gmail.com' and password 'StrongPassword123#'
    Then user should be logged in successfully
    When user tries to view their profile
    Then user should see their email, first name, last name and phone number