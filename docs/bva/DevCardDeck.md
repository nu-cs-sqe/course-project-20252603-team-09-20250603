# DevCardDeck BVA

Manages the shuffled draw pile of development cards. A new deck holds 25 cards:
**14 Knight, 5 Victory Point, 2 Road Building, 2 Year of Plenty, 2 Monopoly**.
Because the deck exposes no size or peek accessor, boundaries are verified through `drawCard()`.

## Method under test: `DevCardDeck()` (construction)

| Test Case          | State of the System                       | Expected output / behavior                                                  | Implemented?       |
|--------------------|-------------------------------------------|-----------------------------------------------------------------------------|--------------------|
| TC-DD-01           | A freshly constructed deck                | Exactly 25 cards can be drawn; the 26th draw throws `IllegalStateException`  | :white_check_mark: |
| TC-DD-02           | A freshly constructed deck                | Composition is 14 Knight, 5 Victory Point, 2 Road Building, 2 Year of Plenty, 2 Monopoly | :white_check_mark: |

## Method under test: `drawCard()`

| Test Case | State of the System                  | Expected output / behavior                  | Implemented?       |
|-----------|--------------------------------------|---------------------------------------------|--------------------|
| TC-DD-04  | Deck has at least one card           | Returns a (non-null) `DevCard`              | :white_check_mark: |
| TC-DD-03  | Deck is empty (all 25 cards drawn)   | Throws `IllegalStateException`              | :white_check_mark: |

## Method under test: `setNextCardType(DevCardType type)`

| Test Case | State of the System                                  | Expected output / behavior                                          | Implemented?       |
|-----------|------------------------------------------------------|---------------------------------------------------------------------|--------------------|
| TC-DD-05  | Type rigged on an otherwise standard deck            | The very next `drawCard()` returns that type                        | :white_check_mark: |
| TC-DD-06  | One card rigged onto a full deck                     | Deck size grows by one (26 draws before empty)                      | :white_check_mark: |
| TC-DD-07  | Two types rigged in sequence                         | The most recently set type is drawn first (LIFO at the front)       | :white_check_mark: |

## Method under test: `shuffleDeck()`

| Test Case | State of the System          | Expected output / behavior                                  | Implemented?       |
|-----------|------------------------------|-------------------------------------------------------------|--------------------|
| TC-DD-08  | A full deck is shuffled      | Card count (25) and composition are unchanged after shuffle | :white_check_mark: |
