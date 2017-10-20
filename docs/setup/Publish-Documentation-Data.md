# Publish Scenarioo Documentation Data

Documentation data can be published in two ways.

## A) Copy into documentation directory

1. Generate or copy the data into the directory that Scenarioo shows under `Manage > General Settings > General > Documentation Data Directory Path`. This folder can be changed, see [Setup of Scenarioo Viewer Web App](Scenarioo-Viewer-Web-Application-Setup.md). In case you are generating your documentations directly into this directory you should write the `build.xml` as a last file, to ensure that a build does not get imported before it has completely been written.

2. To import newly added builds call the http GET REST endpoint `<your-scenarioo-url>/rest/builds/updateAndImport` to update the currently available builds and importing unimported (newly added) build(s).

**Up to Version 2.0.1 of Scenarioo, this is the only possible way to publish data.**

## B) HTTP POST request

Since version 2.1.0 of the scenarioo viewewr web application we also allow adding new documentation data using a HTTP POST request to a REST endpoint. This is only possible as an authenticated user that has the role `scenarioo-build-publisher`.

> We strongly recommend to use HTTPS for this in order to safely transmit the authentication information.

### Configure a user

Add a user to `tomcat-users.xml`. Assign it the role `scenarioo-build-publisher` and choose a strong password. Here's an example:

```
<?xml version="1.0" encoding="UTF-8"?>
<tomcat-users>
    <role rolename="scenarioo-build-publisher"/>
    <user username="john" password="defineYourOwnPasswordHere" roles="scenarioo-build-publisher"/>
</tomcat-users>
```

### Usage

Use e.g. `curl` to add a documentation build to Scenarioo. Here's an example:

```
curl --user john:defineYourOwnPasswordHere -F"file=@build-to-post.zip" http://localhost:8080/scenarioo/rest/builds
```

Please make sure you obey the following rules:

* The ZIP file must contain exactly one branch directory with a `branch.xml` file. The `branch.xml` file is only used if the branch did not exist yet in the documentation directory.
* The branch directory must contain exactly one build directory.
* The POST-Request must have the Content-Type `multipart/form-data`. If you use the above `curl` command, this works out of the box.
* The Multipart content must contain exactly one part that is of type `application/octet-stream`. This is the ZIP file. If you use the above `curl` command, this works out of the box.

There's no need to increase the POST request file size limit in Tomcat (parameter `maxPostSize`), as this limit does not apply to the content-type `multipart/form-data` that we use (see here: http://stackoverflow.com/questions/14075287/does-maxpostsize-apply-to-multipart-form-data-file-uploads).
