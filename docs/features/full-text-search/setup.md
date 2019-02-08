# Setup Scenarioo Full Text Search

Scenarioo Full Text Search is an optional feature to search for all text in your Scenarioo test documentation.

To enable the feature and make it work you have to run an Elasticsearch server and configure Scenarioo properly to connect to that server.

If Scenarioo can not connect to the Elasticsearch server, the feature is disabled.

## Install Elasticsearch

Scenarioo currently uses Elasticsearch version 5.6.9, so you have to install an Elasticsearch Server that is compatible with that version.

Using docker this is as simple as follows:
```
docker pull docker.elastic.co/elasticsearch/elasticsearch:5.6.9
docker run -d --name elasticsearch5 -p 9200:9200 -p 9300:9300 -e cluster.name=elasticsearch -e xpack.ml.enabled=false -e xpack.security.enabled=false docker.elastic.co/elasticsearch/elasticsearch:5.6.9 
```

Please refer to the official documentation to set up Elasticsearch. 
You can either install it on the same machine where you host Scenarioo or on a separate machine.

## Configure Elasticsearch in Scenarioo

You have to configure the endpoint URL and cluster name of the Elasticsearch cluster.

By default following values are preconfigured for you:
* elasticSearchEndpoint: localhost:9300 (usually the default of elasticsearch installations)
* elasticSearchClusterName: elasticsearch (usually the default of elasticsearch installations)

You can change those values in the `config.xml` file of Scenarioo in your Scenarioo data directory.

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

For making search available for a build you have to let Scenarioo calculate its search index. This is done automatically, when a new build is published & imported to scenarioo.

But for making the search index also available for your old builds, that do not have a search index yet, you have to do following manual steps:

1. Go to: `Manage` / Tab `Builds`
2. Select `Reimport` (icon at the right end of a build row) for reimporting and calculating the index for a build
3. Wait until the import has completed

