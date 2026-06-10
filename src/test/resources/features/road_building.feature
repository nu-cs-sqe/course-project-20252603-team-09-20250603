Feature: Road Building Development Card Execution

  Scenario: Placing two free roads under normal inventory conditions
    Given a new game is initialized with the following players:
      | id | name  | color |
      | 1  | John  | RED   |
    And "John" has 15 unbuilt roads remaining in their inventory
    And "John" holds an active "ROAD_BUILDING" card
    When "John" plays their "ROAD_BUILDING" card
    Then the board should grant 2 free roads to "John"
    And "John" should have 13 unbuilt roads remaining in their inventory
    And "John" should no longer have the "ROAD_BUILDING" card in hand