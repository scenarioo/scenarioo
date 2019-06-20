# Code of Conduct

This page is about how to behave and which rules to follow when developing for Scenarioo.

All Scenarioo developers should carefully read this and follow these rules when working on Scenarioo.

These guidelines may also contain some helpful practical hints.

1. **Working with backlog items:**
 * **Product Backlog**: Consider our product backlog of github issues containing user stories and other issues to be solved and help keeping it up to date: https://github.com/scenarioo/scenarioo/issues
 * **Milestones**: Issues that need to be resolved for the next release will be assigned to the corresponding release milestone (e.g. 5.0 - Frühlingsstiefel - Standalone Runner)
 * **Labels**: Our issues are categorized with Labels. These are the most important Labels:
    * **Prio 1/2/3**: Those Labels are used in our backlog to prioritize the issues. From time to time we re-prioritize the issues.
    * **Complexity: Easy/Medium/Hard/Over-9000**: This gives an indication of the effort needed to complete an issue.  
    * **topic:xyz**: e.g. "topic:client-modernisation", "topic:backend" are Labels used to group similar issues together. An issue can belong to multiple topics.
 * **Organizing Issues using HuBoard**: Use https://huboard.com/scenarioo/scenarioo/ to keep the issues of your milestone(s) you are working on updated and well organized (simply login using your github account!):
    * click on "filter" and double-click (!!) a milestone to only see the issues you are interested in
    * **Issue States**: keep your issues assigned to the appropriate swimlane:
       * **backlog** = ideas, need to be discussed or detailed further
       * **ready** = those issues are considered to be ready to implement
       * **working** = somebody is working on it (please also assign it to the person currently working on it!)
       * **done** = those issues are done and can be reviewed for closing
    * **Prioritize** issues by simply changing the order inside swimlanes (just by draging them around).
 * **Working with Issues (never without!)**:
    * Whenever you implement something or want to contribute something, first ensure that you have a corresponding issue or open a new one, describing what you want to implement or contribute. Never start implementing without a corresponding issue!
    * Before you start working on an issue, assign it to the swimlane "Working" and to yourself (never more than 2 issues per person should be assigned in swimlane "working" per milestone).
    * When working on an issue for longer please place a comment on the issue telling the others what exactly you are currently working on or what the progress is. This is important to keep your colleagues updated about what you are working on and to avoid redundancies in work between developers and even provide possibility for collaboration (helping each other).
    * Also place a short comment when you finished working on something for an issue or you are stuck etc., such that others know they could continue your work (in case the issue is not yet finished).
    * Assign an issue to "done", when you think that an issue is finished. If possible let it review by a colleague (ask them to review by putting a comment on the issue and assign it, the reviewer will then close the issue after the successful review).

2. **Coding Guidelines**: Your code has to fulfill the Scenarioo Coding Guidelines before pushing to the repository: See [Coding-Guidelines](Coding-Guidelines.md)!

3. **Passing Unit Tests**: Do not push code to the repository before all unit tests and all end to end tests are green: See [Commit rules](Commit-rules.md).

4. **Commit comments:**
   * Place the number of the issue in your commit comments (like this: #nn) followed by the issue title and a short description what you exactly did. This helps to keep track and document what has been done for an issue. See following example for a proper commit comment for an example issue with number 88 and title "improve build import process":  
`#88 improve build import process`  
`put some more details about your checkin on following lines ...`

5. **CI Build green:** 
Check that the continuous integration build for the branch you are working on passes successfully after you push your changes to the git repository (otherwise fix it!): http://ci.scenarioo.org



