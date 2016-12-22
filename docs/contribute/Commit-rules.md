# Commit Rules

Before you push your commits you always have to perform the following steps:

1. Integrate with others changes: 

     * `git pull`
     * If working on a feature branch or on a fork: also integrate with the base branch (from which you branched off and want to merge back later) by merging it to your branch, using `git merge <base-branch-to-merge-from>`

3. Make sure all the following tests are green:

   * Java unit tests
   * JavaScript unit tests (`gulp test`)
   * ESLint checks (`gulp lint`)
   * Protractor end to end tests (see [e2e Testing](e2eTesting.md))

It is also highly recommended to run this tests regularly and keep them green. Letting tests fails for longer time is not good and will lead to integration problems later.

As an exception to this rules, it is only allowed to push your commits with failing tests in case you are working on a special feature branch, where nobody else is disturbed by your temporarily breaking changes. But still for finishing your feature, you will later have to get all tests successful again, and since this might become tricky, when not keeping tests green regularly, we do not recommend this.

See also [Code of Conduct](Code-of-Conduct.md)
