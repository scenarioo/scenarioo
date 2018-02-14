# Setup Scenarioo Full Text Search

Scenarioo Full Text Search is an optional feature, that is only
activated if your config.xml file points to a reachable
Elasticsearch instance.

## Install Elasticsearch

Scenarioo only works with Elasticsearch version 2.x (see [GitHub issue](https://github.com/scenarioo/scenarioo/issues/616)).

Please refer to the official documentation to set up Elasticsearch.
You can either install it on the same machine where you host Scenarioo
or on a separate machine.

## Configure Elasticsearch

By default `config.xml` does not contain an Elasticsearch endpoint
configuration. That's why the full text search feature is switched off.

In order to enable the full text search feature, add the 
`elasticSearchEndpoint` tag to the config.xml file and set the value
to the host and port of your Elasticsearch instance.

Please make sure to use the port for node communication of Elasticsearch,
i.e. port `9300` in default installations of Elasticsearch.  

Here's an example using the default port of Elasticsearch:

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <elasticSearchEndpoint>localhost:9300</elasticSearchEndpoint>
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

