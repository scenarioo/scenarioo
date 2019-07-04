# Viewer Configuration

Most of the features that are configurable in Scenarioo can be configured directly through the Configuration pages inside the Scenarioo webapplication. Just use the link "Manage" in the upper right corner. Most settings are on the tab "General Settings" in the "Manage" area. 

There are some additional basic Application Properties and additional Advanced Feature Configuration Options that can not be configured through the Scenarioo configuration pages directly. 

This page explains all these more advanced configuration options and how to configure them.

## Application Properties

Application properties as described in following sections can be configured based on the flexibility of Spring Boot configuration property resolution in different ways.

Choose one of the following options how to configure such application properties for your kind of setup of Scenarioo:

* **application.properties file** (*Suitable for: Running Scenarioo as a standalone application*): simply add the properties to set into an `application.properties` file and place it next to the standalone Scenarioo application file from where you start it. Here's a simple example to set the most important properties:
    ```
    # Data Directory
    scenarioo.data=/absolute/dir/to/your/documentation/directory
    
    # Context path for scenarioo URL
    server.servlet.contextPath=/scenarioo
    
    # REST Endpoint protection username and password
    spring.security.user.name=scenarioo
    spring.security.user.password=<putSomeSecurePasswordHere>
    
    # Access Log Config
    server.tomcat.accesslog.enabled=true
    server.tomcat.accesslog.directory=<absolute path to log-directory>
    ```

* **Environment Variables** (*Suitable for: all kind of deployments and especially for the docker image deployment*): 
Application properties can as well be set by simply setting them as environment variables, in this case you have to uppercase the same property names and use `_` instead of `.` in variable names. Here is an example:
    ```
    SCENARIOO_DATA=/absolute/dir/to/your/documentation/directory
    ```
    For Scenarioo docker image you can set environment variables inside the docker container in the `docker run` command with option `-e`.

* **Servlet Context Parameters** (*Suitable for: Running Scenarioo on a Tomcat web server*): This has the advantage that you can define different properties per Scenarioo instance on the same web server if you have multiple instances of the app running. You can provide the respective properties as servlet context init parameters in the `context.xml` on your server as follows:
    ```xml
    <Context>
        <Parameter name="spring.security.user.name" value="scenarioo" override="true" description="HTTP user for publishing documentation data"/>
        <Parameter name="spring.security.user.password" value="somePassword" override="true" description="HTTP password for publishing documentation data"/>
    </Context>    
    ```

There are many more ways how these properties can be exposed to the application. For a full list, please refer to the [Externalized Configuration](https://docs.spring.io/spring-boot/docs/2.0.2.RELEASE/reference/html/boot-features-external-config.html#boot-features-external-config) section in the official Spring Boot documentation.

In the following sections you can find all the properties, together with an explanation of its effects, that you can configure in this way.

### Access URL

This option is only available if you do not run Scenarioo as a WAR deployment that you deploy to a java web server like Tomcat. If you deploy as a WAR deployment to a web server you need to use the usual server configuration possibilities to adjust the context path in the URL instead.

Simply set the application property `server.servlet.contextPath` e.g. as in following example in `application.properties` file:
```
server.servlet.contextPath=/my-scenarioo-path
```

If this application property is not set the app can be reached under the default context URL ending with `/scenarioo`.

See [Application Properties](#application-properties) for different ways how you can set such properties.         

### Access Log

You can enable Scenarioo to log all requested REST calls in a special access log, by setting following application properties:
```
server.tomcat.accesslog.enabled=true
server.tomcat.accesslog.directory=<absolute path to log-directory>
```
     
See [Application Properties](#application-properties) for different ways how you can set such properties.         
     
### Authentication for Secured REST API

Some REST APIs (e.g. for publishing documentation data) are secured by a password. This password can be configured in several ways depending on your Scenarioo server setup:

By default, Spring Security in a Spring Boot 2 application configures a user `user` with a random password to secure all REST endpoints.
In this case, the default user name has been set to `scenarioo`. The random password is printed in the log output at application startup and looks like this:

```
Using generated security password: 67f584b8-04e6-46b3-af57-90f037c0ca5b
```

You can use the following application properties to set the username and password to different values:
```
spring.security.user.name=scenarioo
spring.security.user.password=somePassword
```

See [Application Properties](#application-properties) for different ways how you can set such properties.         

## Configure Advanced Features

1. Locate the `config.xml` file: It is in your Scenarioo documentation data folder that you configured. If you do not find it there, this means either that you use an older version of Scenarioo or that you never saved the configuration before. Go to the Scenarioo configuration web page and choose to save the settings once --> this will save the configuration to the file.

2. Edit the file, according to the following sections where the configurable features are explained, and save your changes.

3. Restart Scenarioo so that the changes are being loaded.

### Branch Selection List Ordering

The order of the branch entries in the top level navigation branch selection dropdown is configurable.

Through an additional config property `branchSelectionListOrder` the following ordering options can be configured:
* `name-ascending`: the branches are sorted by name alphabetically - default value, if value is not set the behaviour is the same
* `name-descending`: branches are sorted in descending order according their branch name - useful for projects that have names with version or release date or stuff like that inside
* `last-build-date-descending`: branches are sorted by the last build date

Example to add in `config.xml` inside the `<configuration>`-element:
```xml
<configuration>
    ...
    <branchSelectionListOrder>name-descending</branchSelectionListOrder>
    ...
</configuration>
```

### More Advanced Feature Configurations

Please refer to the documentation of advanced features for more information on how to configure those features in `config.xml`. For example the following advanced features:

* [Details & Object Repository for additional object tabs on start page](../features/Details.md)
* [Full Text Search Setup Instructions](../features/full-text-search/setup.md)
* [DiffViewer Setup Instructions](../features/diff-viewer/diff-viewer.md)
