# Branching Strategy

## Git Flow

For the development we use the *GitFlow* branching model as advertised by [Nvie](http://nvie.com/posts/a-successful-git-branching-model/).

Please read and follow the procedure carefully and remember the following picture by heart:

![](http://nvie.com/img/git-model@2x.png)

We follow the proposed naming convention for the names of branches.

* `master` every commit on this branch is a release (production ready code)
* `develop` actual development version (main development branch, integration branch)
* `release/[release-version]` planned new release
* `feature/[feature-name]` new feature (use 'xxx-some-text', where xxx is the issue number!)
* `hotfix/[hotfix-name]` hotfix

## Common Use Cases

### Create a Feature Branch

Make sure your current branch is `develop`.

```
git checkout -b feature/<github-issue-number>-branch-name
```

Example: `git checkout -b feature/668-update-moment`

### Publish your Feature Branch

Make sure your current branch is the feature branch.

```
git push -u origin <branch>
```

### Update Feature Branch with Changes from Develop

Make sure your current branch is the feature branch.
```
git merge origin/develop
```

### Merge Feature Branch to Develop using Pull Request

Don't merge into `develop` on your machine. Instead always use GitHub Pull Requests.

## Git Flow Tooling

You can install the `git flow` tooling that supports you in using the model. We made the experience though that this
is not required and working with plain git commands is totally sufficient. If you still want to install `git flow`, see
 https://github.com/nvie/gitflow and http://danielkummer.github.io/git-flow-cheatsheet/ for a quick overview.

When using 'git flow init' please accept all default branch names.
