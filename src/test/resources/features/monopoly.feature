Feature: Monopoly Development Card Execution

  Scenario: A player plays a Monopoly card to steal a specific resource from all opponents
    Given a new game is initialized with the following players:
      | id | name  | color  |
      | 1  | John  | RED    |
      | 2  | Alice | BLUE   |
      | 3  | Bob   | ORANGE |
    And "Alice" has 3 "ORE" cards and 1 "WOOD" card
    And "Bob" has 2 "ORE" cards
    And "John" holds an active "MONOPOLY" card
    When "John" plays their "MONOPOLY" card targeting "ORE"
    Then "Alice" should have 0 "ORE" cards and 1 "WOOD" card
    And "Bob" should have 0 "ORE" cards
    And "John" should have 5 "ORE" cards
    And "John" should no longer have the "MONOPOLY" card in hand