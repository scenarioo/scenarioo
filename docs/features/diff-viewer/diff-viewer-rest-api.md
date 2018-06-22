# Diff Viewer REST API

The Diff Viewer REST API is intended for people who want to include visual regression testing in their build pipeline. E.g. you could make your build fail if the overall change rate is beyond a certain threshold.

Parameters:

* `branchName` / `buildName` in URL: build to compare with the comparison build. This is the build where the comparison will show up in the selection dropdown.
* `comparisonName`: name under which the comparison will be available for selection
* `banchName` / `buildName` in payload: the build the above build is compared with

## Calculate Configured Comparisons

The comparisons configured in the `config.xml` file are automatically calculated after a build import (see [Publish Scenarioo Documentation Data](../../tutorial/Publish-Documentation-Data.md) and [Configuration of Automatic Comparisons](diff-viewer.md#configuration-of-automatic-comparisons)). You can use a calculation endpoint (see below) to poll for completion of these calculations.

## Import Build and Calculate Comparison Synchronously

This call triggers a build import and comparison calculation and also blocks until both are done. This operation can take a long time for larger builds. Consider using the non-blocking API calls instead. 

`POST /rest/builds/{branchName}/{buildName}/comparisons/{comparisonName}/importAndCompare`

Payload:

`{ "branchName": "{comparisonBranchName}", "buildName": "{comparisonBuildName}" }`


## Request Comparison Calculation

Queues the calculation of a comparison between two builds. The method returns immediately and does not wait for the calculation to complete.

`POST /rest/builds/{branchName}/{buildName}/comparisons/{comparisonName}/calculate`

Payload:

`{ "branchName": "{comparisonBranchName}", "buildName": "{comparisonBuildName}" }`


## Get Calculation Status

Returns the calculation status as a string. This is useful to check whether a queued calculation has already completed.

`GET /rest/builds/{branchName}/{buildName}/comparisons/{comparisonName}/calculationStatus`

The return value is one of these:

* `QUEUED_FOR_PROCESSING`
* `PROCESSING`
* `SKIPPED`
* `SUCCESS`
* `FAILED`


## Get Calculation

Returns the calculation object. This can be used to read the overall change rate (field `changeRate`).

`GET /rest/builds/{branchName}/{buildName}/comparisons/{comparisonName}`
