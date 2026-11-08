# Codex Project Instructions

## Project context

- Treasureverse is currently a single-developer project. The repository owner performs the self-review and merge.
- Use Korean when communicating with the user. Keep branch names and Git metadata in the formats below.
- Treat `CONVENTION.md` as the repository-wide Git and collaboration policy. These instructions define the user-facing Codex commands that operate that policy.

## GitHub workflow commands

### When the user says `깃헙 시작`

Interpret the current conversation's agreed task as the work to start. If no concrete deliverable can be identified, ask one concise clarification question before changing GitHub.

1. Inspect the working tree, current branch, open PRs, and remote `develop`.
2. Do not discard or overwrite existing user changes. Report and stop if they would conflict with the new work.
3. Switch to `develop` and fast-forward it from `origin/develop`.
4. Create a GitHub Issue for the agreed task before coding. Use `[<type>] <description>` as the title and include purpose, work checklist, and acceptance criteria in the body.
5. Create a branch from the updated `develop` using `<type>/<issue-number>-<short-description>`.
6. Tell the user the Issue number, Issue URL, branch name, and starting commit. Then perform the requested implementation unless the user asked only to initialize GitHub work.

Select the branch type by task: `feature` for a new capability, `fix` for a defect, `refactor` for behavior-preserving internal improvement, `docs` for documentation, and `chore` for structure, tooling, or configuration.

### When the user says `깃헙 끝`

Finish the active work branch associated with the current conversation. Do not finish an unrelated branch.

1. Inspect the diff and working tree. Confirm that the active branch follows the Issue-number naming rule.
2. Run the relevant checks available in the repository. At minimum run `git diff --check`; also run project tests, lint, build, or formatting commands when those tools exist.
3. Commit all intentional changes using `<type>(<scope>): #<issue-number> <description>`. Do not commit secrets, local environment files, generated artifacts, or unrelated changes.
4. Push the branch to `origin`.
5. Create a PR targeting `develop` with title `[<type>] #<issue-number> <description>`, a summary, test results, and `Closes #<issue-number>` in the body.
6. Perform a self-review. In this single-developer repository, no external approval is required unless repository protections later require it.
7. Squash-merge the PR into `develop`, delete the remote source branch, then explicitly close the related Issue. GitHub only auto-closes a `Closes #...` Issue when a PR reaches the repository's default branch; ordinary PRs here merge into `develop`.
8. Fetch/prune remote refs, switch to `develop`, and fast-forward it from `origin/develop`. Report the PR URL, squash commit, closed Issue, and final branch state.

If there is no intentional change to commit, report that clearly and do not create an empty commit or PR. If a verification check fails, fix it when the task permits; otherwise stop before merge and report the failure.

## Branch and release policy

- Never push directly to `develop` or `main`.
- Normal work: `develop` -> issue branch -> PR -> squash merge into `develop`.
- Production release: create a release PR from `develop` to `main`; do not merge it unless the user explicitly asks to release.
- Before starting a new task, work from updated `develop`, not from an old feature, documentation, or chore branch.
- Clean up merged local branches when safe. Never delete an unmerged branch or user changes without explicit authorization.

## Current repository layout

```text
backend/   # Java/Spring Boot application, when initialized
frontend/  # Frontend application, when initialized
infra/     # Docker, CI/CD, AWS, and deployment configuration
docs/      # Architecture, API, and operational documentation
```
