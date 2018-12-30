# Publish Scenarioo Documentation Data

Documentation data can be published in two ways.

## A) Copy into documentation directory

1. Generate or copy the data into the directory that Scenarioo shows under `Manage > General Settings > General > Documentation Data Directory Path`. This folder can be changed, see [Setup of Scenarioo Viewer Web App](Scenarioo-Viewer-Web-Application-Setup.md). In case you are generating your documentations directly into this directory you should write the `build.xml` as a last file, to ensure that a build does not get imported before it has completely been written.

2. To import newly added builds call the http GET REST endpoint `<your-scenarioo-url>/rest/builds/updateAndImport` to update the currently available builds and importing unimported (newly added) build(s).

**Up to Version 2.0.1 of Scenarioo, this is the only possible way to publish data.**

## B) HTTP POST request

Since version 2.1.0 of the Scenarioo viewer web application we also allow adding new documentation data using a HTTP POST request to a REST endpoint. This is only possible as an authenticated user that has the role `scenarioo-build-publisher`.

> We strongly recommend to use HTTPS for this in order to safely transmit the authentication information.

### Configure the user/password

By default, Sring Security in a Spring Boot 2 application configures a user `user` with a random password to secure all REST endpoints.
In this case, the default user name has been set to `scenarioo`. The random password is printed in the log output at application startup and looks like this:

```
Using generated security password: 67f584b8-04e6-46b3-af57-90f037c0ca5b
```

Based on the flexibility of Spring Boot configuration property resolution, there are various ways how the user/password combination can be overridden.

#### Provide an application.properties file

*Suitable for: Running Scenarioo as standalone WAR*

```
spring.security.user.name=scenarioo
spring.security.user.password=somePassword
```

#### Provide as environment variables

*Suitable for: Running Scenarioo as Docker image or standalone WAR*

Because of the underlying configuration property resolution, it is possible to define them as environment variables like this:

```
SPRING_SECURITY_USER_NAME=scenarioo
SPRING_SECURITY_USER_PASSWORD=somePassword
```

#### Provide as servlet context parameters

*Suitable for: Running Scenarioo on a separate Tomcat webserver*

To configure user and password on a Tomcat, you can provide the respective properties as servlet context init parameters in the `context.xml`.

```
<Context>
    <Parameter name="spring.security.user.name" value="scenarioo" override="true" description="HTTP user for publishing documentation data"/>
    <Parameter name="spring.security.user.password" value="somePassword" override="true" description="HTTP password for publishing documentation data"/>
</Context>    
```

There are a lot more ways how these properties can be exposed to the application. For a full list, please refer to the [Externalized Configuration](https://docs.spring.io/spring-boot/docs/2.0.2.RELEASE/reference/html/boot-features-external-config.html#boot-features-external-config) 
section in the official Spring Boot documentation.

### Usage

Use e.g. `curl` to add a documentation build to Scenarioo. Here's an example:

```
curl -f --user scenarioo:somePassword -F"file=@build-to-post.zip" http://localhost:8080/scenarioo/rest/builds
```

Please make sure you obey the following rules:

* The ZIP file must contain exactly one branch directory with a `branch.xml` file. The `branch.xml` file is only used if the branch did not exist yet in the documentation directory.
* The branch directory must contain exactly one build directory.
* The POST-Request must have the Content-Type `multipart/form-data`. If you use the above `curl` command, this works out of the box.
* The Multipart content must contain exactly one part that is of type `application/octet-stream`. This is the ZIP file. If you use the above `curl` command, this works out of the box.

There's no need to increase the POST request file size limit in Tomcat (parameter `maxPostSize`), as this limit does not apply to the content-type `multipart/form-data` that we use (see here: http://stackoverflow.com/questions/14075287/does-maxpostsize-apply-to-multipart-form-data-file-uploads).
