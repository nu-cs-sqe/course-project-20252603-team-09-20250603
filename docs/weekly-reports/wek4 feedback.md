# Week 4 Project Feedback by PM/TA

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

### Software Process Quality
| #   | Item                                                                                                                                                         |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|-----|--------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 1   | GitHub repository branch protection rules are fully set up so that people cannot push into main without a pull request approval. (needed for Letter Grade C) | ✅        | Branch protection rules have been set up correctly | Canvas assignment Project: Setup, Project grading rubrics                         |
| 2   | Continuous Integration (CI) is fully set up from the beginning. (needed for Letter Grade B)                                                                  | ✅        | CI workflow exists in `.github/workflows/main.yml`, build badge is present in `README.md`, and weekly report records CI setup in Week 3. | Canvas assignment Project: Setup, Project grading rubrics                         |
| 3   | The team uses the project management board steadily and frequently, and the description of each task is detailed. (needed for Letter Grade B)                | ⚠️        | Board usage is visible in weekly report links, but quality gaps remain in user-story detail and assignment tracking (see 3.1 and 3.3). | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.1 | Every functionality-related work item on the management board includes a user story, and optionally one or more use cases.                                   | ⚠️        | Needs work: functionality items do not currently include user stories/use-case detail in task descriptions. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.2 | The design is documented somewhere, either in the work item description, or in a separate design document.                                                   | ⚠️        | `docs/design/README.md` is still a placeholder; concrete design artifacts (class relationships/diagrams/architecture notes) are not yet present in repo. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 3.3 | Task assignments are documented clearly in the management board.                                                                                             | ⚠️        | Needs work: board work items currently have no assignees documented. | Week 4 Wednesday Lecture (Lecture 08), Canvas assignment Project: Week 4 Guidance |
| 4   | Each active feature branch has an open draft PR against main.                                                                                                | ❌        | No PRs have been raised for any active branch. | Week 4 Wednesday Lecture (Lecture 08)                                             |
| 5   | The team has a “definition of done” (BVA) fully documented for the part of the system that is done. (needed for Letter Grade D)                              | ❌        | `docs/bva` currently contains instructions/template guidance only; no per-class BVA analysis files are present yet. | Project grading rubrics                                                           |
| 6   | GitHub commit history demonstrates evidence of a TDD/BDD workflow for all the non-UI code. (needed for Letter Grade C)                                       | ✅        | No non-UI implementation or corresponding tests are present and TDD/BDD workflow is followed| Project grading rubrics                                                           |

### Planning & Progress Evaluation
| # | Item                                                                                                                                                         |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|---|--------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 7 | The team documents every week’s planning and progress evaluation professionally. (needed for Letter Grade B)                                                 | ⚠️        | Weekly tracking exists in `docs/weekly-reports/report.md`, but quality is uneven (placeholder/template content still present, one malformed link, and mixed completeness). | Week 4 Wednesday Lecture (Lecture 08), Project grading rubrics                    |

### Progress & Collaboration
| #  | Item                                                                                                                                                             |  Status   | Reviewer Notes      | Source Instructions or Resources                                                  |
|----|------------------------------------------------------------------------------------------------------------------------------------------------------------------|:---------:|---------------------|-----------------------------------------------------------------------------------|
| 8  | README is updated properly, including the project name, contributors, and build status badge. In addition, the team should specify the GUI library if appliable. | ⚠️        | README includes project name, contributors, and build badge, but GUI library choice is not finalized/documented | Canvas assignment Project: Setup                                                  |
| 9  | Overall progress on "Game Setup Phase"                                                                                                                           | ⚠️        | Team setup and project bootstrapping are underway (CI, initial documentation, game choice), but implementation/design artifacts are still at an early stage. | Canvas assignment Project: Week 4 Guidance                                        |
| 10 | Collaboration: Quality of discussion in PR reviews and work item comments on the board.                                                                          | ⚠️       | Lack of detailed and good quality descriptors present for work on project board |                                                                                   |

## Additional Comments
1. Highest-priority fixes before next checkpoint:
   - Add clear user stories to each functionality work item (actor, goal, value).
   - Assign each board item to at least one teammate.
   - Publish an initial design artifact (`docs/design`) with class/module responsibilities and relationships.

2. To improve process quality quickly:
   - Create at least one concrete BVA file for each implemented class/method area in `docs/bva`.
   - Start pairing each non-UI implementation step with test-first commits to build visible TDD evidence.
