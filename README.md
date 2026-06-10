![Gradle Build](https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/actions/workflows/main.yml/badge.svg)
[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=23614691)
# Catan

## Contributors
- Chloe Lu
- Aria Shi
- Crystal Chen
- Alicia Li
- Benjamin Wong-Fodor

## Dependencies
- JDK 11
- JUnit 5.10
- Gradle 8.10
 
## Acknowledgements
REFERENCES, SOURCE OF HELP ETC


## Special Decisions or Exceptions
**Code coverage**: Falls just short of 100% (98%???) because (as discussed with Prof. Yiji in office hours) simple getters and setters are assumed to function correctly and do not require unit tests. For example, certain functions in Game.java.

**Missing Game Functionality**
- No ports: we did not include ports (ports change the bank trading rate from 4:1 to 3:1 or 2:1) for time reasons, but bank trading is otherwise implemented, so the logic for implementing port functionality is mostly there already
- No board randomization: we do not set up hexes and number tokens randomly; relations between hexes, numbers, nodes, edges, etc. are hardcoded (since this was not required for Catan)
- No turn / color randomization: we determine player order / color deterministically (based on the order in which player names are input)
  
  
