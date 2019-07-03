# Viewer Configuration

Most of the features that are configurable in Scenarioo can be configured directly through the Configuration pages inside the Scenarioo webapplication. Just use the link "Manage" in the upper right corner.

Most settings are on tab "General Settings" in the "Manage" area. 

There are some advanced configuration options for some advanced features that can only be configured eithe rin applicaiton properties (spring configuration file `application.properties`) or through the viewer's `config.xml` file and can not be configured through the Scenarioo configuration pages directly. 

This page explains all these more advanced configuraiton options.

## Configure Major Application Properties

Following features can be configured using spring properties, those properties can be set in `application.properties` file that you have to create next to your WAR file.

After changing values you have to restart the application server.

### Access URL (Standalone runner only)

This option is only available if you run scenarioo as standalone app and do not deploy the WAR to a java webserver like tomcat. If you deploy using a server you need to use usual server configuration possibilities to adjust the context path in the URL.

Configure the access URL under which the app should be deployed by creating the file `application.properties` next to the downloaded standalone runnable war, with the following entry
    ```
    server.servlet.contextPath=/my-scenarioo-path
    ```
If this file or property in the file is missing, the app can be reached under the default context URL ending with `/scenarioo`.

### Access Log

You can enable scenarioo to log all requested REST calls in a special access log, if you wish so in `application.properties`:
     ```
     server.tomcat.accesslog.enabled=true
     server.tomcat.accesslog.directory=<absolute path to log-directory>
     ```
     
### Authentication for Secured REST API

Some REST APIs (e.g. for publishing documentation data) are secured by a password. This password can be configured in several ways depending on your scenarioo server setup:

By default, Spring Security in a Spring Boot 2 application configures a user `user` with a random password to secure all REST endpoints.
In this case, the default user name has been set to `scenarioo`. The random password is printed in the log output at application startup and looks like this:

```
Using generated security password: 67f584b8-04e6-46b3-af57-90f037c0ca5b
```

Based on the flexibility of Spring Boot configuration property resolution, there are various ways how the user/password combination can be overridden.

#### Provide an application.properties file

*Suitable for: Running Scenarioo as standalone WAR*.

```
spring.security.user.name=scenarioo
spring.security.user.password=somePassword
```

For more information on where the application.properties file should be placed and what other configuration options it offers see [Setup of Scenarioo Viewer Web App](Scenarioo-Viewer-Web-Application-Setup.md#running-scenarioo-as-standalone-application). 

#### Provide as environment variables

*Suitable for: Running Scenarioo as Docker image or standalone WAR*

Because of the underlying configuration property resolution, it is possible to define them as environment variables like this:

```
SPRING_SECURITY_USER_NAME=scenarioo
SPRING_SECURITY_USER_PASSWORD=somePassword
```

#### Provide as servlet context parameters

*Suitable for: Running Scenarioo on a separate Tomcat web server*

To configure user and password on a Tomcat, you can provide the respective properties as servlet context init parameters in the `context.xml`.

```
<Context>
    <Parameter name="spring.security.user.name" value="scenarioo" override="true" description="HTTP user for publishing documentation data"/>
    <Parameter name="spring.security.user.password" value="somePassword" override="true" description="HTTP password for publishing documentation data"/>
</Context>    
```

There are many more ways how these properties can be exposed to the application. For a full list, please refer to the [Externalized Configuration](https://docs.spring.io/spring-boot/docs/2.0.2.RELEASE/reference/html/boot-features-external-config.html#boot-features-external-config) 
section in the official Spring Boot documentation.

## Configure Advanced Features

1. Locate the `config.xml` file: it is in your scenarioo documentation data folder that you configured. If you do not find it there: It means either that you use an older version of scenarioo or that you never saved the configuration before. Go to the Scenarioo configuraiton web page and choose to save the settings once --> this will save the configuration to the file.

2. Edit the file - according to following sections where the configurable features are explained - and save your changes.

3. Restart scenarioo such that the changes are being loaded.

### Branch Selection List Ordering

The order of the branch entries in the top level navigation branch selection dropdown is configurable.

Through an additional config property `branchSelectionListOrder` the following ordering options can be configured:
* `name-ascending`: the branches are sorted by name alphabetically - default value, if value not set the behaviour is the same
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

Please refer to the documentation of advanced features for more information on how to configure those features in `config.xml`. Like for example the following advanced features:

* [Details & Object Repository for additional object tabs on start page](../features/Details.md)
* [Full Text Search Setup Instructions](../features/full-text-search/setup.md)
* [DiffViewer Setup Instructions](../features/diff-viewer/setup.md)
