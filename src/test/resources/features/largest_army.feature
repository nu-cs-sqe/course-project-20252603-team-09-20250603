Feature: Largest Army Milestone Tracking

  Scenario: A player claims the Largest Army milestone after playing their third Knight
    Given a new game is initialized with the following players:
      | id | name  | color |
      | 1  | John  | RED   |
      | 2  | Alice | BLUE  |
    And "John" has played 2 knights
    And "John" holds an active "KNIGHT" card
    When "John" plays their "KNIGHT" card targeting hex 4
    Then "John" should have 3 played knight counted in their army pool
    And "John" should possess the "Largest Army" milestone
    And "John" should have 2 victory points