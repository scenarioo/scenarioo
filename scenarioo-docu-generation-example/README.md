# Scenarioo Demo Data

This Example is the generator for our demo data that shows a sample documentation of wikipedia.

The generated demo data is used for two purposes:
* it is the data that you can see in our online demo of the Scenarioo viewer (choose one of the branches starting with `wikipedia-docu-example`)
* our own e2e ui tests are testing using this demo data
 
Furthermore the example also can be used to see how the Scenarioo Java Writer could be used in tests to write documentation data. 

But this example only pretends to be a real web testing project, because both the application under test as well as the testing toolkit (e.g. selenium) are just faked to generate some example data.

## How to run the Example

Just run all the tests inside of this submodule to generate the sample data:

```
./gradlew clean build
```

See the result in folder `build/scenarioDocuExample` inside this submodule directory.

The example even copies a sample config.xml file to the output directory. Usually this file is not generated but is saved from admin UI in the viewer or by manually editing it. The copied file is the config of our demo and is also used for running our e2e tests.

## For our Developers

Before you run the e2e tests of the viewer you should regenerate the data to be in the correct ebfore state. This will even write the config.xml to be used for e2e testing. Also ensure that you configure your Scenarioo viewer app to use the above mentioned directory as data directory.
