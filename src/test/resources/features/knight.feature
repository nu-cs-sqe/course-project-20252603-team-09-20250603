Feature: Knight Development Card Execution

  Scenario: Moving the robber to a new hex
    Given a new game is initialized with the following players:
      | id | name  | color |
      | 1  | John  | RED   |
      | 2  | Alice | BLUE  |
    And the robber is currently located on hex 10
    And "John" holds an active "KNIGHT" card
    When "John" plays their "KNIGHT" card targeting hex 5
    Then the robber should be located on hex 5
    And "John" should have 1 played knight counted in their army pool
    And "John" should no longer have the "KNIGHT" card in hand

