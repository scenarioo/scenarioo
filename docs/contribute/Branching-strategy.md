For the development we following the branching model as advertised by Nvie on http://nvie.com/posts/a-successful-git-branching-model/.

Please read and follow the procedure carefully and remember the following picture by heart:
![](http://nvie.com/img/git-model@2x.png)

We follow the proposed naming conventions for the names of branches.

* `master` every commit on this branch is a release (production ready code)
* `develop` actual development version (main development branch, integration branch)
* `release/[release-version]` planned new release
* `feature/[feature-name]` new feature (use 'xxx-some-text', where xxx is the issue number!)
* `hotfix/[hotfix-name]` hotfix

## Using git flow

Git flow is a set of tools aimed to facilitate working with the above branching model. However in the beginning it might be best to learn the branching model with your bare hands in order to get a full understanding.

**Setup**

See https://github.com/nvie/gitflow and http://danielkummer.github.io/git-flow-cheatsheet/ for a quick overview.

When using 'git flow init' please accept all default branch names.

## Cheatsheet for working with feature branches, git and git flow

1. Creating new feature branch

  `git flow feature start MYFEATURE` (MYFEATURE = xxx-some-text-decribing-your-feature, where xxx is the issue number)

2. Publish your feature branch (if you want to share with others)

  `git flow feature publish MYFEATURE `
    and then use usual "git pull" and "git push" (to share changes on that feature branch).

3. Keeping your feature branch integrated with development branch (get latest):

  `git merge origin/develop`

4. Merge back your feature branch and close it:

  `git flow feature finish MYFEATURE `
