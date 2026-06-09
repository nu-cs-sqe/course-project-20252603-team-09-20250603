Feature: Game initial placement (setup phase)
  As a player
  I want to place my starting settlements and roads in snake order
  So that the game can be set up correctly before normal play begins

  Background:
    Given a setup game with 3 players
    And an initialized setup board

  Scenario Outline: Player successfully places an initial settlement for free through the setup controller
    Given it is the current player's turn to place a settlement
    When the current player places a settlement at node <nodeId>
    Then node <nodeId> should be occupied by the current player's settlement
    And the current player's settlement inventory should decrease by one
    And the current player's resources should remain unchanged
    And the current player's victory points should increase by one

    Examples:
      | nodeId |
      | 0      |
      | 6      |
      | 27     |

  Scenario Outline: Player successfully places an initial road connected to their settlement and the turn advances
    Given the current player has placed a settlement at node <nodeId>
    When the current player places a road on an edge connected to node <nodeId>
    Then the connected edge should be occupied by the current player's road
    And the current player's road inventory should decrease by one
    And the current player's resources should remain unchanged
    And it should become player 2's turn

    Examples:
      | nodeId |
      | 0      |
      | 6      |
      | 27     |

  Scenario: Player cannot place a road before placing a settlement
    Given it is the current player's turn to place a settlement
    When the current player places a road on an edge connected to node 0
    Then the game should prevent the placement
    And the connected edge should not be occupied by the current player's road
    And the current player's road inventory should remain unchanged
    And it should still be player 1's turn

  Scenario Outline: Player cannot place an initial road that is not connected to their settlement
    Given the current player has placed a settlement at node <nodeId>
    When the current player places a road on an edge not connected to node <nodeId>
    Then the game should prevent the placement
    And that disconnected edge should not be occupied by the current player's road
    And the current player's road inventory should remain unchanged

    Examples:
      | nodeId |
      | 0      |
      | 27     |

  Scenario Outline: Player cannot place a settlement on an occupied node
    Given node <nodeId> is already occupied by another player's settlement
    And it is the current player's turn to place a settlement
    When the current player places a settlement at node <nodeId>
    Then the game should prevent the placement
    And node <nodeId> should still be occupied by the other player
    And the current player's settlement inventory should remain unchanged
    And the current player's victory points should remain unchanged

    Examples:
      | nodeId |
      | 1      |
      | 10     |

  Scenario Outline: Player cannot place a settlement adjacent to an existing settlement (distance rule)
    Given node <neighborId> is already occupied by another player's settlement
    And it is the current player's turn to place a settlement
    When the current player places a settlement at node <nodeId>
    Then the game should prevent the placement
    And node <nodeId> should not be occupied by the current player's settlement
    And node <neighborId> should still be occupied by the other player
    And the current player's settlement inventory should remain unchanged

    Examples:
      | nodeId | neighborId |
      | 1      | 0          |
      | 0      | 1          |

  Scenario: Placing an edge is ignored when the player still needs to place a settlement
    Given it is the current player's turn to place a settlement
    When the current player attempts a road at node 0 while a settlement is expected
    Then the placement should be ignored
    And the current player's road inventory should remain unchanged
    And it should still be player 1's turn

  Scenario: Placing a node is ignored when the player still needs to place a road
    Given the current player has placed a settlement at node 0
    When the current player attempts a settlement at node 6 while a road is expected
    Then the placement should be ignored
    And node 6 should not be occupied by the current player's settlement
    And it should still be player 1's turn

  Scenario: Initial placement is ignored once the game leaves the setup phase
    Given the game is no longer in the setup phase
    When the current player places a settlement at node 0
    Then the placement should be ignored
    And node 0 should not be occupied by the current player's settlement

  Scenario: Completing the snake-order setup transitions the game to normal play
    When all players complete their initial settlements and roads in snake order
    Then every player should have two settlements and two roads on the board
    And each player's victory points should equal two
    And the game should advance to the normal play phase

  Scenario: Each player receives starting resources from their second settlement
    When all players complete their initial settlements and roads in snake order
    Then each player should receive the resources adjacent to their second settlement
