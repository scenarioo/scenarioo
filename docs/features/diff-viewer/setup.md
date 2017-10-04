#Diff Viewer Setup Guide

This guide describes how you can set up the Diff Viewer feature.
The Diff Viewer feature makes it possible to compare two different builds against each other.
In two easy step, you are ready to compare your own builds:

1. [Add a comparison configuration](#add-a-comparison-configuration)
2. [Start comparison](#start-comparison)

After that you are ready to use the Diff Viewer feature. Check out the [Diff Viewer User Guide](user-guide.md) for further details.

## Add a comparison configuration
In your config.xml file you can edit the comparison configuration.
It is possible to add one or more comparison configurations. On each import, all the configured comparisons get compared.
You can delete a comparisonConfiguration, then it will not be compared in the future. 
The old comparisons will not be deleted automatically. Check out [Delete a comparison](#delete-a-comparison) for further details.

Each comparisonConfiguration has the following settings:
* **name**: Name of the comparison. This is the name that gets displayed in the Viewer to select a comparison result for displaying it. 
   As soon as the comparison is calculated for a build you can select it in the comparison selection menu in the frontend on that build.
* **baseBranchName**: Name or alias of the branch for which to perform such a comparison calculation on each new build being imported for that branch. 
* **comparisonBranchName**: Name or alias of the branch to which to compare (can be the same branch as well).
* **comparisonBuildName**: The name or alias of the build to which to compare to. Some examples:
    * "last successful": alias for the last successful build (as configured in configuration as well)
    * "most recent": alias for the most recent build (as configured in configuration as well)
    * the original build name    

Refer to the glossary for further details: [Diff Viewer Glossary](./glossary.md)

Example Configuration:
   ```xml
    ...
    <branchAliases/>

    <comparisonConfigurations>

        <comparisonConfiguration>
            <name>To last successful</name>
            <baseBranchName>wikipedia-docu-example-dev</baseBranchName>
            <comparisonBranchName>wikipedia-docu-example</comparisonBranchName>
            <comparisonBuildName>last successful</comparisonBuildName>
        </comparisonConfiguration>

        <comparisonConfiguration>
            <name>To most recent</name>
            <baseBranchName>wikipedia-docu-example-dev</baseBranchName>
            <comparisonBranchName>Production</comparisonBranchName>
            <comparisonBuildName>most recent</comparisonBuildName>
        </comparisonConfiguration>

        <comparisonConfiguration>
            <name>To Projectstart</name>
            <baseBranchName>wikipedia-docu-example-dev</baseBranchName>
            <comparisonBranchName>Production</comparisonBranchName>
            <comparisonBuildName>2014-01-20</comparisonBuildName>
        </comparisonConfiguration>

    </comparisonConfigurations>


    <labelConfigurations/>
    ...
   ```

## Start comparison
To start the build comparison you have two options:
* Reimport an existing build:
   * Scenario Viewer > Manage > Click on the reimport symbol next to the preferred build
   * NOTE: The "Import & Update Builds" Button will only start a comparison if a build gets imported for the first time
* Restart the scenarioo-server. The new builds are getting compared on server startup right after the import.

If any problems occur during the comparison, check out the log file for detailed error information.
You can find the log file in the following place:

> scenarioo-application-data\diffViewer\\\<baseBranchName>\\\<baseBuildName>\\\<comparisonName>\comparison.derived.log

In future versions, there might be the possibility to start and configure comparisons in the web client.

## Delete a comparison
All comparisons are stored in the following folder:
>scenarioo-application-data\diffViewer

To delete a comparison you only have to delete the appropriate folder. 
You have the following options:
* Delete all the comparisons at once
* Delete selected comparisons

A filestructure as example:

* [Root Folder]
 * [branchName1]
   * [buildName1]
     * [useCaseName1]
       * [scenarioName1]
         * ...
 * [branchName2]
 * [scenarioo-application-data]
   * [diffViewer] > Delete this folder to remove all comparisons
     * [baseBranchName1] > Delete this folder to remove all comparisons from one branch
       * [baseBuildName1] > Delete this folder to remove all comparisons from one build
         * [comparisonName1] > Delete this folder to remove a specific comparison
 * builds.states.derived.xml
