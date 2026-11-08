# Treasureverse Git & Collaboration Convention

## 1. Purpose and non-negotiable rules

This convention makes every change traceable from its request to deployment.

- Create a GitHub Issue before starting work.
- One branch and one pull request (PR) address one Issue by default.
- Use the same Issue number in the branch name, every related commit, and the PR title.
- Do not push directly to `develop` or `main`.
- Merge only after self-review and required CI checks pass.
- Do not commit credentials, API keys, `.env` files, production data, or generated build artifacts.

### Exceptions

Trivial corrections such as a typo in an unmerged PR may be included in that PR. Changes unrelated to the PR's Issue require a separate Issue, branch, and PR. For an urgent production incident, create the `hotfix` Issue as soon as service stabilization permits.

## 2. Branch strategy

| Branch | Purpose | Merge destination | Deployment |
| --- | --- | --- | --- |
| `main` | Production-ready release baseline | - | Production (`prod`) after approval |
| `develop` | Integration branch for completed development work | `main` | Development (`dev`) automatically |
| `feature/*` | New product functionality | `develop` | PR validation only |
| `fix/*` | Bug fix for development or unreleased work | `develop` | PR validation only |
| `hotfix/*` | Urgent production incident fix | `main`, then sync to `develop` | Production after approval |
| `refactor/*` | Internal improvement without behavior changes | `develop` | PR validation only |
| `docs/*` | Documentation-only change | `develop` | PR validation only |
| `chore/*` | Tooling, dependency, configuration, or maintenance work | `develop` | PR validation only |

### Branch naming

Format:

```text
<type>/<issue-number>-<short-description>
```

- `type` must be one of `feature`, `fix`, `hotfix`, `refactor`, `docs`, or `chore`.
- `issue-number` is the GitHub Issue number without `#`.
- `short-description` uses English lowercase kebab-case.

Examples:

```text
feature/42-auction-bidding
fix/57-outbid-notification
chore/63-ecs-deployment-pipeline
```

## 3. Issue workflow

### 3.1 Create an Issue first

Create an Issue before creating a branch. An Issue must define the goal, scope, and completion criteria so that a reviewer can decide whether the PR is complete.

### 3.2 Issue title

Format:

```text
[<type>] <clear description>
```

Examples:

```text
[feat] 경매 입찰 API 구현
[fix] 상위 입찰 알림 중복 발송 수정
[ci] Jenkins ECS 배포 파이프라인 추가
```

### 3.3 Required Issue metadata

- Assign an owner using **Assignees**.
- Add one work-type label: `feat`, `fix`, `refactor`, `docs`, `test`, `chore`, or `ci`.
- Add at least one area label when applicable: `backend`, `frontend`, `infra`, `auction`, `notification`, `auth`, `payment`, or `chat`.
- Add one priority label: `priority:high`, `priority:medium`, or `priority:low`.
- Set a milestone when the task belongs to a sprint or release.
- Apply `status:in-progress` when work begins and `status:blocked` if outside help is required.

### 3.4 Issue template

```md
## 작업 목적

<!-- 왜 이 작업이 필요한지 작성합니다. -->

## 작업 내용

- [ ] 세부 작업 1
- [ ] 세부 작업 2

## 완료 조건

- [ ] 요구사항에 맞게 기능 또는 문서가 반영되었다.
- [ ] 관련 테스트, 린트, 빌드가 통과한다.
- [ ] 필요한 문서 및 환경 설정이 갱신되었다.

## 참고 사항

<!-- API 명세, 화면, 연관 Issue/PR, 기술적 제약을 작성합니다. -->
```

## 4. Commit convention

### 4.1 Commit format

```text
<type>(<scope>): #<issue-number> <description>
```

- All commits related to an Issue include that Issue number.
- Write the description in a concise present-tense imperative form.
- Keep the subject line within 72 characters when practical.
- Each commit should contain one logical change and remain buildable when possible.
- Use a body to explain a non-obvious design decision, migration step, or breaking change.

### 4.2 Types

| Type | Use |
| --- | --- |
| `feat` | New user-facing capability |
| `fix` | Defect correction |
| `refactor` | Internal improvement without intended behavior change |
| `test` | Test addition or modification |
| `docs` | Documentation change |
| `style` | Formatting-only change |
| `chore` | Dependency, configuration, file movement, or maintenance |
| `ci` | CI/CD, Docker, Jenkins, or workflow change |
| `perf` | Performance improvement |
| `revert` | Reversal of a previous change |

### 4.3 Scopes

Use the affected domain or layer when it adds clarity:

```text
auth, user, product, auction, bid, notification, payment,
chat, frontend, backend, infra, database
```

Examples:

```text
feat(auction): #42 입찰 검증을 추가한다
feat(notification): #42 상위 입찰 알림을 저장한다
fix(bid): #57 동일 금액 입찰을 차단한다
ci(infra): #63 ECR 이미지 태그에 커밋 SHA를 사용한다
docs(git): #1 협업 컨벤션을 추가한다
```

## 5. Pull request workflow

### 5.1 PR title and link

Format:

```text
[<type>] #<issue-number> <description>
```

The PR body must include `Closes #<issue-number>`. GitHub automatically closes the Issue only when that PR is merged into the repository's default branch (`main`). Because ordinary work merges into `develop`, explicitly close the Issue after its PR is merged.

### 5.2 PR template

```md
## 관련 Issue

Closes #<issue-number>

## 변경 사항

- 변경 사항 1
- 변경 사항 2

## 주요 구현 결정

<!-- 트랜잭션, 락, API 호환성, 보안, 배포 영향 등을 작성합니다. -->

## 테스트

- [ ] 단위 테스트
- [ ] 통합 테스트
- [ ] 린트 및 빌드
- [ ] 로컬 수동 확인

## 리뷰 포인트

<!-- 특히 검토가 필요한 설계 또는 코드를 작성합니다. -->

## 스크린샷 / API 결과

<!-- UI 또는 API 변경 시 첨부합니다. -->
```

### 5.3 Before requesting review

- [ ] The branch includes the latest target-branch changes and has no unresolved conflicts.
- [ ] Relevant tests, lint, and build have passed.
- [ ] No secret, debug code, unnecessary files, or unrelated changes are included.
- [ ] Issue acceptance criteria are complete or explicitly explained.
- [ ] The author has performed a self-review.

## 6. Review and merge

- In this single-developer project, the author performs a self-review; external approval is not required.
- All required CI checks must pass; do not merge a failing or conflicted PR.
- Resolve all review conversations before merge, unless the reviewer explicitly accepts deferral.
- New substantive commits after approval require review again when repository settings enforce dismissal; otherwise request re-review as appropriate.
- Use **Squash and merge** as the default to keep `develop` and `main` history concise.
- Set the squash commit message to the PR title format, including the Issue number.
- Delete the source branch after merge.
- After merge, confirm that the linked Issue is closed, labels and milestone are correct, and deployment status is healthy.

## 7. Deployment and rollback

```text
feature/*, fix/*  -> lint / test / build on PR
develop merge     -> automatic deployment to dev
main merge        -> approved deployment to prod
```

- Tag container images with the Git commit SHA or CI build number; never depend only on `latest`.
- Keep the deployed image tag visible in CI/CD logs and release notes.
- Verify application health checks and the affected core flow after deployment.
- On a failed deployment, redeploy the most recent known-good image tag and create a follow-up Issue.
- Production deployment requires the designated approval step before ECS deployment.
- AWS credentials must come from an IAM role where possible. If a credential store is unavoidable, keep secrets out of logs and source control.

## 8. Standard lifecycle

```text
1. Create Issue #42
2. Create feature/42-auction-bidding from develop
3. Commit: feat(auction): #42 입찰 검증을 추가한다
4. Push branch and open [feat] #42 경매 입찰 구현 PR
5. Add Closes #42 and request review
6. Pass CI and receive approval
7. Squash merge into develop
8. Confirm Issue #42 is closed and dev deployment is healthy
```

## 9. Initial repository exception

Until the `develop` branch is created and protected, the first repository-initialization PR may target `main`. Create and protect `develop` immediately after that initialization is merged. Thereafter, ordinary development PRs target `develop`.
