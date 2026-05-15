# Week 6 Project Feedback by PM/TA

**Dedicated PM/TA**: Vandan Agrawal (vandanagrawal@u.northwestern.edu)

## How to Read This Feedback
> [!NOTE]
> **Purpose.** This feedback focuses on your team's progress and collaboration. It is meant as guidance, not judgement.

> [!IMPORTANT]
> **Scope.** For the BVA and TDD items, the PM/TA evaluates only the `main` branch. Ongoing work in feature branches will be evaluated after it is merged. If you'd like early feedback on work in progress, please reach out to your PM/TA directly.

> [!TIP]
> **Mistakes are expected :).** As the instructor mentioned in class, early mistakes are part of the learning process. As long as your team addresses the issues after you get the feedback, your grade will not suffer from them.

## Checklist
Status:
- ✅: All done/Good job!
- ⚠️: Attention needed
- ❌: Significant issue found
- ➖: No basis to evaluate

### Past Feedback
| # | Item                                                                                                 | Status | Reviewer Notes | Source Instructions or Resources |
|---|------------------------------------------------------------------------------------------------------|:------:|----------------|----------------------------------|
| 0 | The team has closed and merged the past Feedback PR(s), indicating that they have read the feedback. | ⚠️     | Week 5 feedback PR ([#19](https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/pull/19)) is still **open** and not merged into `main`, so the “read and merge feedback” workflow from last week is incomplete. |                                  |

### Software Process Quality
| # | Item                                                                                                                                         | Status | Reviewer Notes | Source Instructions or Resources                                                                              |
|---|----------------------------------------------------------------------------------------------------------------------------------------------|:------:|----------------|---------------------------------------------------------------------------------------------------------------|
| 1 | Checkstyle: Checkstyle is set up or there is a to-do item planned in the Project Management board for this task. (needed for Letter Grade B) | ⚠️     | Still **not** on `main` in `build.gradle.kts`. **In progress:** open PR [#23](https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/pull/23) (“Setup checkstyle and spotbugs”, branch `setup-linters`)—merge when CI passes and add a config file per course lab guidance. | Week 6 Monday lecture (Lecture 11); the build script and config file in the repository for Lab: Code Coverage |
| 2 | SpotBugs: SpotBugs is set up or there is a to-do item planned in the Project Management board for this task. (needed for Letter Grade B)     | ⚠️     | Same as Checkstyle—not on `main`; still covered by open PR [#23](https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/pull/23). After merge, run SpotBugs from Gradle (and optionally CI). | Week 6 Monday lecture (Lecture 11); the build script and config file in the repository for Lab: Code Coverage |

### Planning & Progress Evaluation
| # | Item                                                                                                                                                         |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|---|--------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 4 | The team documents every week’s planning and progress evaluation professionally. (needed for Letter Grade B)                                                 | ⚠️        | `docs/weekly-reports/report.md` on `main` still has incomplete Week 5 bullets and **no Week 6 section**. There is now a **draft** PR for report updates ([#21](https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/pull/21), branch `weekly-report-updating`)—merge it once Week 6 planning is complete so `main` reflects professional weekly documentation. | Week 4 Wednesday Lecture (Lecture 08), Project grading rubrics                    |

### Progress & Collaboration
| # | Item                                                                                                                                                                                   |  Status   | Reviewer Notes      | Source Instructions or Resources                 |
|---|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|--------------------------------------------------|
| 5 | Overall development progress (recall the recommended order is: Game Setup Phase -> One turn of the game -> Multiple turns -> One win condition -> Other win conditions (if applicable) | ⚠️        | `main` still has **no `.java` files** under `src/` (only `.gitkeep`). Several **draft** feature PRs are open (e.g. board generation [#22](https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/pull/22), placement [#18](https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/pull/18), starting resources [#24](https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/pull/24)), plus linters [#23](https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/pull/23)—good parallelization, but **merge** the first slice of game logic + tests to `main` soon. | Canvas assignment Project: Week 4 and 5 Guidance |

### The following items are not checked by the reviewer this week as they were checked in the previous weeks
If your team wants the reviewer to check any of these for any reasons, please contact them or the instructor via either email or tag/mention them in the feedback PR.

| #   | Item                                                                                                                                                         |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|-----|--------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 1   | GitHub repository branch protection rules are fully set up so that people cannot push into main without a pull request approval. (needed for Letter Grade C) | ➖        | Not re-evaluated this week per rubric section. | Canvas assignment Project: Setup, Project grading rubrics                         |
| 2   | Continuous Integration (CI) is fully set up from the beginning. (needed for Letter Grade B)                                                                  | ➖        | Not re-evaluated this week per rubric section. | Canvas assignment Project: Setup, Project grading rubrics                         |
| 3   | The team uses the project management board steadily and frequently, and the description of each task is detailed. (needed for Letter Grade B)                | ➖        | Not re-evaluated this week per rubric section. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.1 | Every functionality-related work item on the management board includes a user story, and optionally one or more use cases.                                   | ➖        | Not re-evaluated this week per rubric section. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.2 | The design is documented somewhere, either in the work item description, or in a separate design document.                                                   | ➖        | Not re-evaluated this week per rubric section. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.3 | Task assignments are documented clearly in the management board.                                                                                             | ➖        | Not re-evaluated this week per rubric section. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 4   | Each active feature branch has an open draft PR against main.                                                                                                | ➖        | Not re-evaluated this week per rubric section. | Week 4 Wednesday Lecture (Lecture 08)                                             |
| 5   | The team has a “definition of done” (BVA) fully documented for the part of the system that is done. (needed for Letter Grade D)                              | ➖        | Not re-evaluated this week per rubric section. | Project grading rubrics                                                           |
| 6   | GitHub commit history demonstrates evidence of a TDD/BDD workflow for all the non-UI code. (needed for Letter Grade C)                                       | ➖        | Not re-evaluated this week per rubric section. | Project grading rubrics                                                           |
| 7   | Collaboration: Quality of discussion in PR reviews and work item comments on the board.                                                                      | ➖        | Not re-evaluated this week per rubric section. |                                                                                   |

## Additional Comments
1. **Close the loop on Week 5 feedback:** merge PR [#19](https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/pull/19) after the team has read it, then delete the feedback branch, so Week 6 “past feedback” is clearly satisfied next time.

2. **Week 6 tooling:** prioritise merging PR [#23](https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/pull/23) (or equivalent) so Checkstyle and SpotBugs are on `main` with config files and documented Gradle tasks; consider adding both to the existing GitHub Actions workflow so every PR is checked automatically.

3. **Evidence limits:** Evaluation uses `main` in this workspace plus the public GitHub REST API (open PRs/branches). Org project-board–only tasks are not visible here; if that is how Checkstyle/SpotBugs are tracked, note it in your feedback PR for the TA.

4. **Related open PR:** [#20](https://github.com/nu-cs-sqe/course-project-20252603-team-09-20250603/pull/20) (Week 6 instructor code review doc) is separate from this TA sheet—ensure the team has read and actioned any instructor items there.
