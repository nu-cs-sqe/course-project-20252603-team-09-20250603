Feature: Game handleBuild
  As a player
  I want to build roads, settlements, and cities
  So that I can expand my position during the game

  Background:
    Given a game with a current player
    And an initialized board

  Scenario Outline: Player successfully builds a road or settlement with sufficient resources and inventory
    When player chooses to build <buildType>
    And chooses to build at location <locationType> <locationId>
    And the game validates that player has the resources needed to build <buildType>
    And the game validates that player has at least one <buildType> in their inventory
    And the game validates that <locationType> <locationId> is available for building <buildType>
    Then the <locationType> <locationId> should be occupied by the player's <buildType>
    And the player's inventory should decrease by one <buildType>
    And the player's resources should decrease by the cost of building <buildType>

    Examples:
    |buildType  | locationType | locationId |
    |settlement | node         | 1          |

  Scenario Outline: Player successfully upgrades their settlement to a city
    When player chooses to build a city
    And chooses to build at node <locationId>
    And the game validates that player has the resources needed to build city
    And the game validates that player has at least one city in their inventory
    And the game validates that node <locationId> is occupied by the player's settlement
    Then node <locationId> should be occupied by the player's city
    And the player's inventory should decrease by one city
    And the player's resources should decrease by the cost of building a city

    Examples:
    |locationId |
    |1          |
