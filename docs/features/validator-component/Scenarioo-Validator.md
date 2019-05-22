# Scenarioo Validator

The Scenarioo Validator is a command line tool that validates produced Scenarioo documentation folders and files.

It is helpful when developing writer libraries (scenarioo-cs, scenarioo-js) in order to assure that the produced output can be successfully imported by the Scenarioo application.


## Usage

Run this command in the `scenarioo-validator` folder to build the validator:

`$ ./gradlew clean installDist`

From the folder `scenarioo-validator` navigate to `build/install/scenarioo-validator/bin`.

Run the validator by providing the path to your documentation data directory.

Linux:

`$ ./scenarioo-validator <PATH_TO_YOUR_DOCU_DIRECTORY>`

Windows:

`$ ./scenarioo-validator.bat <PATH_TO_YOUR_DOCU_DIRECTORY>`


## Options

```
 scenarioo-validator [-c] <pathToDirectory>
 -c,--clean-derived   If set, derived files will be deleted before
                      validation
```


## Implementation Details

The tool uses already existing functionality from scenarioo-server. It validates the given directory by importing it with *org.scenarioo.business.builds.ScenarioDocuBuildsManager* and *org.scenarioo.business.builds.BuildImporter*.

See *org.scenarioo.validator.ScenariooValidator* for a starting point.
