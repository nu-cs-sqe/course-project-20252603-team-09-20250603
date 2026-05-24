# Player BVA

Tracks a single player's identity, infrastructure inventory, and victory points.

---

### Method under test: `getName()`

|             | State of the System         | Expected output      | Implemented? |
|-------------|-----------------------------|----------------------|--------------|
| Test Case 1 | Player name is `"Bob"`      | Returns `"Bob"`      | :white_check_mark: |
| Test Case 2 | Player name is `"Jane Doe"` | Returns `"Jane Doe"` | :white_check_mark: |

---

### Method under test: `getColor()`

|             | State of the System             | Expected output                  | Implemented? |
|-------------|---------------------------------|----------------------------------|--------------|
| Test Case 3 | Player Bob's color is red       | Returns red for player Bob       | :white_check_mark: |
| Test Case 4 | Player Jane Doe's color is blue | Returns blue for player Jane Doe | :white_check_mark: |

---

### Method under test: `getInventory()`

|              | State of the System | Expected output | Implemented? |
|--------------|---------------------|-----------------|--------------|
| Test Case 5  | New player is created | Inventory contains 15 roads, 5 settlements, and 4 cities | :white_check_mark: |
| Test Case 6  | Player uses one road | Inventory returns 14 roads, 5 settlements, and 4 cities | :x: |
| Test Case 7  | Player uses one settlement | Inventory returns 15 roads, 4 settlements, and 4 cities | :x: |
| Test Case 8  | Player uses one city | Inventory returns 15 roads, 5 settlements, and 3 cities | :x: |
| Test Case 9  | Player uses one road, one settlement, and one city | Inventory returns 14 roads, 4 settlements, and 3 cities | :x: |
| Test Case 10 | Caller modifies the map returned by `getInventory()` | Player's actual inventory does not change | :x: |

---

### Method under test: `useInventoryItem(String item)`

|              | State of the System | Expected output / behavior | Implemented? |
|--------------|---------------------|-----------------------------|--------------|
| Test Case 11 | Player has 15 roads and uses one road | Road inventory decreases from 15 to 14 | :x: |
| Test Case 12 | Player has 5 settlements and uses one settlement | Settlement inventory decreases from 5 to 4 | :x: |
| Test Case 13 | Player has 4 cities and uses one city | City inventory decreases from 4 to 3 | :x: |
| Test Case 14 | Player has exactly 1 item remaining and uses that item | Item inventory decreases from 1 to 0 | :x: |
| Test Case 15 | Player has 0 of an item remaining and tries to use it | Throws `IllegalStateException` and inventory stays at 0 | :x: |
| Test Case 16 | Player tries to use an invalid inventory item, such as `"ships"` | Throws `IllegalStateException` and inventory does not change | :x: |

---

### Method under test: `getVictoryPoints()`

|              | State of the System | Expected output | Implemented? |
|--------------|---------------------|-----------------|--------------|
| Test Case 17 | New player is created | Returns 0 | :white_check_mark: |