#Diff Viewer Setup Guide

This guide describes how you can set up the Diff Viewer feature.
The Diff Viewer feature makes it possible to compare two different builds against each other.
In two easy step, you are ready to compare your own builds:

1. [Add a comparison configuration](#2-add-a-comparison-configuration)
2. [Start comparison](#3-start-comparison)
3. [Delete a comparison](#4-delete-a-comparison)

After that you are ready to use the Diff Viewer feature. Check out the [Diff Viewer User Guide](user-guide.md) for further details.

## 1. Add a comparison configuration
In your config.xml file you can edit the comparison configuration.
It is possible to add one or more comparison configurations. On each import, all the configured comparisons get compared.
You can delete a comparisonConfiguration, then it will not be compared in the feature. 
The old comparisons will not be deleted automatically. Check out [Delete a comparison](#4-delete-a-comparison) for further details.

Each comparisonConfiguration has the following settings:
* **name**: Name of this specific comparison. The name gets displayed in the comparison selection menu.
* **baseBranchName**: Name of the base Branch. The comparison build gets only compared to the builds of this branch. It is possible to use branch aliases. 
* **comparisonBranchName**: Name of the comparison branch.
* **comparisonBuildName**: Name of the comparison build. This build gets compared to all builds in the defined baseBranch.
 * It is possible to compare to the following comparisons:
   * last successful: The last successful build gets compared
    * most recent: The most recent build gets compared
    * the original buildname. For example '2014-01-20'

Refer to the glossary for further details: https://github.com/magitnu/scenarioo/blob/develop/documentation/diff-viewer/glossary.md

Sample configuration:
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

## 2. Start comparison
To start the build comparison you have two options:
* Reimport an existing build:
   * Scenario Viewer > Manage > Click on the reimport symbol next to the preferred build
   * NOTE: The "Import & Update Builds" Button will only start a comparison if a build gets imported for the first time
* Restart the scenarioo-server. The new builds are getting compared on server startup right after the import.

If any problems occur during the comparison, check out the log file for detailed error information.
You can find the log file in the following place:

> scenarioo-application-data\diffViewer\\\<baseBranchName>\\\<baseBuildName>\\\<comparisonName>\comparison.derived.log

In future versions, there might be the possibility to start and configure comparisons in the web client.

## 3. Delete a comparison
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
