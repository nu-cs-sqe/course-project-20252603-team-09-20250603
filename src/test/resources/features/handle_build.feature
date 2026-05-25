Feature: Game handleBuild
  As a player
  I want to build roads, settlements, and cities
  So that I can expand my position during the game

  Background:
    Given a game with a current player
    And an initialized board

  Scenario Outline: Player successfully builds a road or settlement with sufficient resources and inventory through the controller
    When player chooses build option <optionNumber>
    And enters to build at <locationType> <locationId>
    And the game validates that player has the resources needed to build <buildType>
    And the game validates that player has at least one <buildType> in their inventory
    And the game validates that <locationType> <locationId> is available for building <buildType>
    Then the <locationType> <locationId> should be occupied by the player's <buildType>
    And the player's inventory should decrease by one <buildType>
    And the player's resources should decrease by the cost of building <buildType>

    Examples:
    |optionNumber |buildType   | locationType | locationId |
    |1            | road       | edge         | 1          |
    |1            | road       | edge         | 10         |
    |2            | settlement | node         | 1          |
    |2            | settlement | node         | 10         |

  Scenario Outline: Player successfully upgrades their settlement to a city through the controller
    When player chooses build option 3
    And enters to build at node <locationId>
    And the game validates that player has the resources needed to build city
    And the game validates that player has at least one city in their inventory
    And the game validates that node <locationId> is occupied by the player's settlement
    Then node <locationId> should be occupied by the player's city
    And the player's inventory should decrease by one city
    And the player's resources should decrease by the cost of building city

    Examples:
    |locationId |
    | 1          |
    | 8          |
    | 10         |

  Scenario Outline: Player cannot build without enough infrastructure inventory through the controller
    When player chooses build option <optionNumber>
    And enters to build at <locationType> <locationId>
    And the game validates that player has the resources needed to build <buildType>
    And the game validates that player does not have any <buildType> in their inventory
    And the game validates that <locationType> <locationId> is available for building <buildType>
    Then the game should prevent the player from building
    And <locationType> <locationId> should not be occupied by the player's <buildType>
    And the player's inventory should remain unchanged
    And the player's resources should remain unchanged

    Examples:
    |optionNumber |buildType   | locationType | locationId |
    |1            | road       | edge         | 1          |
    |1            | road       | edge         | 10         |
    |2            | settlement | node         | 1          |
    |2            | settlement | node         | 10         |

  #Scenario Outline: Player cannot build without enough resources through the controller

  Scenario Outline: Player cannot build a settlement or road on an occupied node or edge through the controller
    When player chooses build option <optionNumber>
    And enters to build at <locationType> <locationId>
    And the game validates that player has the resources needed to build <buildType>
    And the game validates that player has at least one <buildType> in their inventory
    And the game validates that <locationType> <locationId> is occupied by another player
    Then the game should prevent the player from building
    And <locationType> <locationId> should remain occupied by the other player
    And the player's inventory should remain unchanged
    And the player's resources should remain unchanged

    Examples:
    |optionNumber |buildType   | locationType | locationId |
    |1            | road       | edge         | 1          |
    |1            | road       | edge         | 10         |
    |2            | settlement | node         | 1          |
    |2            | settlement | node         | 10         |

  Scenario Outline: Player cannot build a settlement next to an occupied neighboring node through the controller
    When player chooses build option 2
    And enters to build at node <locationId>
    And the game validates that player has the resources needed to build settlement
    And the game validates that player has at least one settlement in their inventory
    And node <neighborId> is occupied by another player
    Then the game should prevent the player from building
    And node <locationId> should not be occupied by the player's settlement
    And node <neighborId> should remain occupied by the other player
    And the player's inventory should remain unchanged
    And the player's resources should remain unchanged

    Examples:
    |locationId | neighborId |
    | 1          | 2          |
    | 2          | 1          |
    |10          |11          |

  Scenario Outline: Player cannot upgrade an empty node to a city through the controller
    When player chooses build option 3
    And enters to build at node <locationId>
    And the game validates that player has the resources needed to build city
    And the game validates that player has at least one city in their inventory
    And the game validates that node <locationId> is not occupied by any player's settlement
    Then the game should prevent the player from building
    And node <locationId> should remain unoccupied
    And the player's inventory should remain unchanged
    And the player's resources should remain unchanged

    Examples:
    |locationId |
    | 1          |
    | 8          |
    | 10         |

  Scenario Outline: Player cannot upgrade another player's settlement to a city through the controller
    When player chooses build option 3
    And enters to build at node <locationId>
    And the game validates that player has the resources needed to build city
    And the game validates that player has at least one city in their inventory
    And the game validates that node <locationId> is occupied by another player's settlement
    Then the game should prevent the player from building
    And node <locationId> should remain occupied by the other player
    And the player's inventory should remain unchanged
    And the player's resources should remain unchanged

    Examples:
    |locationId |
    | 1          |
    | 8          |
    | 10         |

  Scenario Outline: Player cannot upgrade an existing city through the controller
    When player chooses build option 3
    And enters to build at node <locationId>
    And the game validates that player has the resources needed to build city
    And the game validates that player has at least one city in their inventory
    And the game validates that node <locationId> is occupied by the player's city
    Then the game should prevent the player from building
    And node <locationId> should remain occupied by the player's city
    And the player's inventory should remain unchanged
    And the player's resources should remain unchanged

    Examples:
    |locationId |
    | 1          |
    | 8          |
    | 10         |

  Scenario Outline: Player cannot choose an invalid build option through the controller
    When player chooses build option <optionNumber>
    And enters to build at node 1
    Then the game should prevent the player from building
    And the player's inventory should remain unchanged
    And the player's resources should remain unchanged

    Examples:
    |optionNumber |
    | 0           |
    | 4           |
    | -1          |

