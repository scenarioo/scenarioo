# Setup Scenarioo Full Text Search

Scenarioo Full Text Search is an optional feature, that is only
activated if your config.xml file points to a reachable
Elasticsearch instance.

## Install Elasticsearch

Please refer to the official documentation to set up Elasticsearch.
You can either install it on the same machine where you host Scenarioo
or on a separate machine.

## Configure Elasticsearch

By default `config.xml` does not contain an Elasticsearch endpoint
configuration. Whenever the `config.xml` file does not contain such a 
configuration, Scenarioo uses `localhost:9300` to look for 
Elasticsearch.

If you want to set a different host and / or port for Elasticsearch,
add the `elasticSearchEndpoint` tag to the configuration XML file.
Here's an example:

```
<?xml version="1.0" encoding="UTF-8" standalone="yes"?>
<configuration>
    <elasticSearchEndpoint>elasticsearch.internal.mycompany.com:7000</elasticSearchEndpoint>
    <!-- omitted other config tags -->
</configuration>
```

Restart Scenarioo after making manual changes to the config file.

## Check Elasticsearch Configuration and Status in Scenarioo

Go to Manage -> General Settings -> Full Text Search in order to 
check the Elasticsearch configuration and status.