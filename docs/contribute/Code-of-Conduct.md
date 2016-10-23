This page is about how to behave and which rules to follow when developing for Scenarioo.

All Scenarioo developers should carefully read this and follow these rules when working on Scenarioo.
These guidelines may also contain some helpful practical hints.

1. **Working with backlog items:**
 * **Product Backlog**: Consider our product backlog of github issues containing user stories and other issues to be solved and help keeping it up to date: https://github.com/scenarioo/scenarioo/issues
 * **Milestones**: Our issues are categorized into milestones. There are four types of milestones:
    * **Backlog Prio 1/2/3**: those milestones are our backlog of ideas, those issues are not yet planned to be implemented in a concrete milestone. This is the place to put new ideas, related to any topic, that you think should be considered to be implemented sometime. From time to time we re-prioritize the issues that are in those milestones.
    * **Mini-Project- or Feature-Milestones**: e.g. "1.1 Object Repository" or "1.1 Stable URLs". Those milestones are used to plan some so called "mini projects" that work on a specific topic of Scenarioo to deliver one or several bigger new features for the next releases of scenarioo. Those milestones always start with the version number of the release in which those features are planned to be released.
    * **Release-Milestones**: e.g. "1.1 Release 1.1", "1.0 Release 1.0" are milestones for a planned release. All issues that need to be solved up to this release are assigned into those release milestones, as long as they do not belong to a Mini-Project- or Feature-Milestone for this same release.
    * **Known-Issues-Milestones**: Each Release has also a corresponding "Known Issues"-Milestone to collect issues that are not solved up to the release date. Those are minor issues, that can be solved after the release date or might even never be solved, because not important enough. Release notes will link to this milestones.
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
    * When working on a issue for longer please place a comment on the issue telling the others what exactly you are currently working on or what the progress is. This is important to keep your colleagues updated about what you are working on and to avoid redundancies in work between developers and even provide possibility for collaboration (helping each other).
    * Also place a short comment when you finished working on something for an issue or you are stuck etc., such that other know they could continue your work (in case the issue is not yet finished).
    * Assign an issue to "done", when you think that an issue is finished. If possible let it review by a colleague (ask them to review by putting a comment on the issue and assign it, the reviewer will then close the issue after the successful review).

2. **Coding Guidelines**: Your code has to fulfill the Scenarioo Coding Guidelines before pushing to the repository: See [Coding-Guidelines](Coding-Guidelines)!

3. **Passing Unit Tests**: Do not push code to the repository before all unit tests and all end to end tests are green: See [Commit rules](Commit rules).

4. **Commit comments:**
   * Place the number of the issue in your commit comments (like this: #nn) followed by the issue title and a short description what you exactly did. This helps to keep track and document what has been done for an issue. See following example for a proper commit comment for an example issue with number 88 and title "improve build import process":  
`#88 improve build import process`  
`put some more details about your checkin on following lines ...`

5. **CI Build green:** 
Check that the continuous integration build for the branch you are working on passes successfully after you push your changes to the git repository (otherwise fix it!): http://ci.scenarioo.org



