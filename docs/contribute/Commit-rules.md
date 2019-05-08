# Commit Rules

Before you push your commits you always have to perform the following steps:

1. Integrate with others changes: 

     * `git pull`
     * If working on a feature branch or on a fork: also integrate with the base branch (from which you branched off and want to merge back later) by merging it to your branch, using `git merge <base-branch-to-merge-from>`

3. Make sure all the following tests are green:

   * Java unit tests
   * JavaScript unit tests (`npm test`)
   * ESLint checks (`npm run lint`)
   * Protractor end to end tests (see [e2e Testing](Developer-Guide.md#run-e2e-tests))

It is also highly recommended to run these tests regularly and keep them green. Letting tests fail for longer time is not a good idea because it will lead to integration difficulties later on.

As an exception to this rules, it is only allowed to push your commits with failing tests in case you are working on a special feature branch, where nobody else is disturbed by your temporarily breaking changes. But still for finishing your feature, you will later have to get all tests successful again, and since this might become tricky, when not keeping tests green regularly, we do not recommend this.

See also [Code of Conduct](Code-of-Conduct.md)
