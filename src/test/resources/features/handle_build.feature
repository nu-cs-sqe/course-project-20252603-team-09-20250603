Feature: Build through the player action menu (GUI)
  As a player
  I want to build roads, settlements, and cities from the action menu
  So that I can expand my position during normal play

  Background:
    Given a game with a current player
    And an initialized board

  Scenario Outline: Player builds a road or settlement through the action menu
    Given the player has the resources to build <infraType>
    And the player has at least one <infraType> in their inventory
    When the player clicks Build
    And the player selects <infraType>
    And the player clicks <locationType> <locationId>
    And the player confirms the build
    Then <locationType> <locationId> should be occupied by the player's <infraType>
    And the player's inventory should decrease by one <infraType>
    And the player's resources should decrease by the cost of building <infraType>

    Examples:
      | infraType  | locationType | locationId |
      | road       | edge         | 1          |
      | road       | edge         | 10         |
      | settlement | node         | 1          |
      | settlement | node         | 10         |

  Scenario Outline: Player upgrades their settlement to a city through the action menu
    Given node <locationId> already has the player's settlement
    And the player has the resources to build city
    And the player has at least one city in their inventory
    When the player clicks Build
    And the player selects city
    And the player clicks node <locationId>
    And the player confirms the build
    Then node <locationId> should be occupied by the player's city
    And the player's inventory should decrease by one city
    And the player's resources should decrease by the cost of building city

    Examples:
      | locationId |
      | 1          |
      | 8          |
      | 10         |

  Scenario Outline: Player cannot build without infrastructure in their inventory
    Given the player has the resources to build <infraType>
    And the player has no <infraType> in their inventory
    When the player clicks Build
    And the player selects <infraType>
    And the player clicks <locationType> <locationId>
    And the player confirms the build
    Then the build should be rejected
    And <locationType> <locationId> should not be occupied by the player's <infraType>
    And the player's inventory should remain unchanged
    And the player's resources should remain unchanged

    Examples:
      | infraType  | locationType | locationId |
      | road       | edge         | 1          |
      | road       | edge         | 10         |
      | settlement | node         | 1          |
      | settlement | node         | 10         |

  Scenario Outline: Player cannot build without enough resources
    Given the player lacks the resources to build <infraType>
    And the player has at least one <infraType> in their inventory
    When the player clicks Build
    And the player selects <infraType>
    And the player clicks <locationType> <locationId>
    And the player confirms the build
    Then the build should be rejected
    And <locationType> <locationId> should not be occupied by the player's <infraType>
    And the player's inventory should remain unchanged
    And the player's resources should remain unchanged

    Examples:
      | infraType  | locationType | locationId |
      | road       | edge         | 1          |
      | settlement | node         | 1          |

  Scenario Outline: Player cannot upgrade a settlement to a city without enough resources
    Given node <locationId> already has the player's settlement
    And the player lacks the resources to build city
    And the player has at least one city in their inventory
    When the player clicks Build
    And the player selects city
    And the player clicks node <locationId>
    And the player confirms the build
    Then the build should be rejected
    And node <locationId> should be occupied by the player's settlement
    And the player's inventory should remain unchanged
    And the player's resources should remain unchanged

    Examples:
      | locationId |
      | 1          |
      | 8          |

  Scenario Outline: Player cannot build on a location occupied by another player
    Given the player has the resources to build <infraType>
    And the player has at least one <infraType> in their inventory
    And <locationType> <locationId> is occupied by another player
    When the player clicks Build
    And the player selects <infraType>
    And the player clicks <locationType> <locationId>
    And the player confirms the build
    Then the build should be rejected
    And <locationType> <locationId> should remain occupied by the other player
    And the player's inventory should remain unchanged
    And the player's resources should remain unchanged

    Examples:
      | infraType  | locationType | locationId |
      | road       | edge         | 1          |
      | road       | edge         | 10         |
      | settlement | node         | 1          |
      | settlement | node         | 10         |

  Scenario Outline: Player cannot build a settlement next to an occupied neighboring node
    Given the player has the resources to build settlement
    And the player has at least one settlement in their inventory
    And node <neighborId> is occupied by another player
    When the player clicks Build
    And the player selects settlement
    And the player clicks node <locationId>
    And the player confirms the build
    Then the build should be rejected
    And node <locationId> should not be occupied by the player's settlement
    And node <neighborId> should remain occupied by the other player
    And the player's inventory should remain unchanged
    And the player's resources should remain unchanged

    Examples:
      | locationId | neighborId |
      | 1          | 2          |
      | 2          | 1          |
      | 10         | 11         |

  Scenario Outline: Player cannot upgrade an empty node to a city
    Given the player has the resources to build city
    And the player has at least one city in their inventory
    And node <locationId> is empty
    When the player clicks Build
    And the player selects city
    And the player clicks node <locationId>
    And the player confirms the build
    Then the build should be rejected
    And node <locationId> should remain unoccupied
    And the player's inventory should remain unchanged
    And the player's resources should remain unchanged

    Examples:
      | locationId |
      | 1          |
      | 8          |
      | 10         |

  Scenario Outline: Player cannot upgrade another player's settlement to a city
    Given node <locationId> is occupied by another player
    And the player has the resources to build city
    And the player has at least one city in their inventory
    When the player clicks Build
    And the player selects city
    And the player clicks node <locationId>
    And the player confirms the build
    Then the build should be rejected
    And node <locationId> should remain occupied by the other player
    And the player's inventory should remain unchanged
    And the player's resources should remain unchanged

    Examples:
      | locationId |
      | 1          |
      | 8          |
      | 10         |

  Scenario Outline: Player cannot upgrade an existing city
    Given node <locationId> already has the player's city
    And the player has the resources to build city
    And the player has at least one city in their inventory
    When the player clicks Build
    And the player selects city
    And the player clicks node <locationId>
    And the player confirms the build
    Then the build should be rejected
    And node <locationId> should be occupied by the player's city
    And the player's inventory should remain unchanged
    And the player's resources should remain unchanged

    Examples:
      | locationId |
      | 1          |
      | 8          |
      | 10         |

  Scenario: Player cannot confirm a build without selecting an infrastructure type
    Given the player has the resources to build settlement
    And the player has at least one settlement in their inventory
    When the player clicks Build
    And the player clicks node 1
    And the player confirms the build
    Then the build should be rejected
    And node 1 should not be occupied by the player's settlement
    And the player's inventory should remain unchanged
    And the player's resources should remain unchanged
