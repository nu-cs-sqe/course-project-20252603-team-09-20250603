Feature: Year of Plenty Development Card Execution

  Scenario Outline: Drawing a random card from the deck and playing it
    Given a new game is initialized with the following players:
      | id | name | color |
      | 1  | John | RED   |
    And the development card deck is rigged to next return a "<cardType>"
    When "John" draws a development card
    And "John" waits for their next turn to activate their cards
    And "John" plays their "YEAR_OF_PLENTY" card picking "WOOD" and "WHEAT"
    Then "John" should have 1 "WOOD" cards and 1 "WHEAT" cards

    Examples:
      | cardType       | Comment                                     |
      | YEAR_OF_PLENTY | Rigging the deck ensures we draw what we want|

  Scenario Outline: Picking combinations of different or identical resources from the bank
    Given a new game is initialized with the following players:
      | id | name  | color |
      | 1  | John  | RED   |
    And "John" has 0 "<choice1>" cards and 0 "<choice2>" cards
    And "John" holds an active "YEAR_OF_PLENTY" card
    When "John" plays their "YEAR_OF_PLENTY" card picking "<choice1>" and "<choice2>"
    Then "John" should have <expected1> "<choice1>" cards
    And "John" should have <expected2> "<choice2>" cards
    And "John" should no longer have the "YEAR_OF_PLENTY" card in hand

    Examples:
      | choice1 | choice2 | expected1 | expected2 | Comment                  |
      | WOOD    | WHEAT   | 1         | 1         | Two different cards      |
      | ORE     | ORE     | 2         | 2         | Double resource checking |
      | BRICK   | SHEEP   | 1         | 1         | Alternate combinations   |