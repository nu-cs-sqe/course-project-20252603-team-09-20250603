# Week 3 (04/13/2026-04/19/2026)
**Planning and Progress Tracking**:
1. [done] Chloe: Add GitHub Actions workflow (https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/pull/1)
2. [done] Aria: Add Gradle Build Badge (https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/pull/4)
3. [done] All: Project setup guidelines:
     - Met with teammates
     - Set up communication channels (iMessage group chat)
     - Established weekly meeting time (Monday 2:00 PM)
     - Decided on game (Catan)
  
# Week 4 (04/20/2026-04/27/2026) 
**Planning and Progress Tracking**:
1. [Done] All: Weekly meeting:
     - Met as a team, played Catan, familiarized ourselves with ruleset
     - Obtained reference objects
     - [80% done] Decided on implementing GUI application (considering Java Swing, JavaFX, React??)
3. [80% Done] All: Documenting functional requirements in project board (https://github.com/orgs/nu-cs-sqe/projects/53?pane=issue&itemId=180919536&issue=nu-cs-sqe%7Ccourse-project-20252603-team-09-20250603%7C7](https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/issues/6)

# Week 5 (04/28/2026-XX/XX/2026) 
**Planning and Progress Tracking**:
1. [Done] All: Weekly meeting:
     - Met as a team to go over and address week 4 feedback
     - Cleaned up design doc 
     - Ensured all members knew their task
     - Cleared up branching confusion
2. [90% Done] All: Producing the design
3. [100% Done] All (individually): Splitting tasks and beginning implementation
   
# Week 6 (05/06/2026-05/12/2026) 
**Planning and Progress Tracking**:
1. [Done] All: Weekly meeting:
      - Met as a team to finalize design and game architecture
2. [Done] Crystal: Finished feature/new-game-player-setup for game setup phase to handle player object creation and validation (https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/pull/17)
3. [Done] Aria, Alicia, and Ben: Finished feature/board-generation to create nodes, hexes, edges, and nodeToHex relations (https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/pull/22)
   - Aria: Node and Edge classes
   - Alicia: Board class, Node/Hex class updates, ResourceType
   - Ben: Hex class
4. [Done] Chloe: Finished feature/initial-placement-rules for snake drafting system and road/settlement validation (https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/pull/18)
5. [Done] All: Select GUI library (JavaFX) and update README
6. [Done] All: Set up Checkstyle and SpotBugs
7. [10%] Ben: Start building GUI

# Week 7 (05/12/2026-05/18/2026) 
1. [Done] All: weekly meeting:
   - Assigned PR reviewers for all week 6 PRs
   - Distributed tasks for week 7 -> finishing game setup phase and beginning first turn phase
2. [80%] All: pull linters branch and fix any problems
3. [20%] Aria and Crystal: create Main class to call GameInitializer and Game, create Game class to execute initial placement rules
4. [20%] Alicia: continue working on Player class, finish resource distribution by updating Player class to have a method to update the resource hand
5. [30%] Ben: continue building GUI
6. [20%] Chloe: devCards

# Week 8 (05/19/2026-05/25/2026) 
1. No weekly meeting - continue working on week 7 tasks
2. [80%] All: finish merging pre-week 7 branches
3. [80%] All: pull linters branch and fix any problems
4. [Done] Aria and Crystal: create Main class to call GameInitializer and Game, create Game class to execute initial placement rules
5. [60%] Alicia: continue working on Player class, finish resource distribution by updating Player class to have a method to update the resource hand
6. [90%] Ben: continue building GUI
7. [50%] Chloe: devCards
8. [done] Crystal: setup Jacoco and Pitest for code coverage and mutation testing (https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/pull/31)

# Week 9 (05/26/2026-06/01/2026)
1. [Done] All: weekly meeting:
   - Distributed/refreshed tasks for week 9 
2. [Done] Aria: create internationalization files
3. [90%] Crystal: updatePlayerPoints, moveRobber
4. [Done] Alicia: finish Player class
5. [67%] Ben: continue building GUI, getPlayerAction, handleInitialPlacement
6. [90%] Chloe: handleBuyDevCard, handleUseDevCard in game class

# Week 10 (06/02/2026-06/08/2026)
1. [Done] All: weekly meeting:
2. [20%] Alicia: handleDistributeResources on roll - by Saturday
3. [0%] Ben: integration testing for handleInitialPlacement - by Saturday
4. [0%] Crystal: review handleInitialPlacement PR and merge to main - by Saturday
5. [80%] Ben/Aria: UI v2 PR merged into get-player-action, then merge get-player-action to main - by Saturday
6. [0%] Ben: merge devCard PR - by Saturday
7. [0%] Aria: game.play
8. [0%] Aria: end of week - finish i18n once UI finalized
13. [0%] Ben: update build integration testing to be GUI instead of scanner
14. [0%] Chloe/Crystal: mutation testing and code coverage
17. [30%] Alicia: finish all robber logic - stealing random card, cutting resources in half when >=7, updating Crystal moveRobber function
18. [100%] Ben: fix PlacementValidator, verify TurnManager accuracy
15. [100%] Ben: handleInitialPlacement written
16. [100%] Aria: getPlayerAction
17. [100%] Chloe: points for largestArmy (# knights)
18. [100%] Crystal: updatePlayerPoints for longestRoad
