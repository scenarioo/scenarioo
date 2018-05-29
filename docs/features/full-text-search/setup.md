# Setup Scenarioo Full Text Search

Scenarioo Full Text Search is an optional feature to search for all text in your scenarioo test documentation.

To enable the feature and make it work you have to run an Elasticsearch server and configure scenarioo properly to connect to that server.

If Scenarioo can not connect to the Elasticsearch server, the feature is disabled.

## Install Elasticsearch

Scenarioo currently uses Elasticsearch version 5.x, so you have to install an Elasticsearch Server of that version.

Please refer to the official documentation to set up Elasticsearch. 
You can either install it on the same machine where you host Scenarioo or on a separate machine.

## Configure Elasticsearch

You have to configure the endpoint URL and cluster name of the Elasticsearch cluster.

By default following values are preconfigured for you:
* elasticSearchEndpoint: localhost:9300 (usually the default of elasticsearch installations)
* elasticSearchClusterName: elasticsearch (usually the default of elasticsearch installations)

You can change those values in the `config.xml` file of scenarioo in your scenarioo data directory.

Here's an example configuration for the search feature in the `config.xml`:

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <elasticSearchEndpoint>localhost:9300</elasticSearchEndpoint>
    <elasticSearchClusterName>scenarioo</elasticSearchClusterName>
    <!-- omitted other config tags -->
</configuration>
```

Restart Scenarioo after making manual changes to the config file.

## Check Elasticsearch Configuration and Status in Scenarioo

Go to Manage -> General Settings -> Full Text Search in order to 
check the Elasticsearch configuration and status.

## Calculate Search Indexes 

For making search available for a build you have to let scenarioo calculate its search index. This is done automatically, when a new build is published & imported to scenarioo.

But for making the search index also available for your old builds, that do not have a search index yet, you have to do following manual steps:

1. Go to: `Manage` / Tab `Builds`
2. Select `Reimport` (icon at the right end of a build row) for reimporting and calculating the index for a build
3. Wait until the import has completed

