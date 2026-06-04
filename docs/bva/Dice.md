# Dice BVA

Handles rolling the dice

### Method under test: `roll()`

Two dice, each with 6 sides. They are rolled and the result is stored in the die 1 and die 2 fields.

| Test Case   | State of the System          | Expected output                | Implemented? |
|-------------|------------------------------|--------------------------------|--------------|
| Test Case 1 | Die 1 rolls 1, die 2 rolls 1 | Dice.die1 == 1, Dice.die2 == 1 | :x:          |
| Test Case 2 | Die 1 rolls 1, die 2 rolls 2 | Dice.die1 == 1, Dice.die2 == 2 | :x:          |
| Test Case 3 | Die 1 rolls 3, die 2 rolls 4 | Dice.die1 == 3, Dice.die2 == 4 | :x:          |
| Test Case 3 | Die 1 rolls 5, die 2 rolls 6 | Dice.die1 == 5, Dice.die2 == 6 | :x:          |
| Test Case 3 | Die 1 rolls 6, die 2 rolls 6 | Dice.die1 == 6, Dice.die2 == 6 | :x:          |

### Method under test: `getDieSum()`

Returns the sum of die1 and die2.

| Test Case   | State of the System            | Expected output | Implemented? |
|-------------|--------------------------------|-----------------|--------------|
| Test Case 1 | Dice.die1 == 1, Dice.die2 == 1 | Returns 2       | :x:          |
| Test Case 2 | Dice.die1 == 1, Dice.die2 == 2 | Returns 3       | :x:          |
| Test Case 3 | Dice.die1 == 3, Dice.die2 == 4 | Returns 7       | :x:          |
| Test Case 3 | Dice.die1 == 5, Dice.die2 == 6 | Returns 11      | :x:          |
| Test Case 3 | Dice.die1 == 6, Dice.die2 == 6 | Returns 12      | :x:          |