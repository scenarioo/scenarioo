The Scenarioo Validator is a command line tool that validates produced scenarioo documentation folders and files.
It is helpful when developing writer libraries (scenarioo-cs, scenarioo-js) in order to assure that the produced output can be successfully imported by the scenarioo application.

# Usage

After you checked out the scenarioo source-code, you can run the following command in order to build the validator tool:

`$ ./gradlew clean build installApp`

This will produce the folder **scenarioo/scenarioo-validator/build/install/scenarioo-validator** containing all needed libraries as well as start scripts for UNIX and windows.

On UNIX systems run

`$ ./scenarioo-validator/build/install/scenarioo-validator/bin/scenarioo-validator <PATH_TO_YOUR_DOCU_DIRECTORY>`

On Windows run the .bat file

`$ ./scenarioo-validator/build/install/scenarioo-validator/bin/scenarioo-validator.bat <PATH_TO_YOUR_DOCU_DIRECTORY>`

**NOTE**: a prebuilt version of the tool can be found here http://54.88.202.24/jenkins/job/scenarioo-develop/lastSuccessfulBuild/artifact/scenarioo-validator/build/distributions/scenarioo-validator-latest.zip (built during CI on develop branch)

## Options

```
 scenarioo-validator [-c] <pathToDirectory>
 -c,--clean-derived   If set, derived files will be deleted before
                      validation
```

## Implementation Details

The tool uses already existing functionality from scenarioo-server. It validates the given directory by importing it with *org.scenarioo.business.builds.ScenarioDocuBuildsManager* and *org.scenarioo.business.builds.BuildImporter*.

See *org.scenarioo.validator.ScenariooValidator* for a starting point.