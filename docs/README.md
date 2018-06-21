# Scenarioo

This documentation is about the Software Development and Testing Tool "Scenarioo". 
 
Scenarioo helps you visualizing and documenting the functionalities of your software system in a guidebook, that contains the important user interaction flows, generated out of your UI tests. Each so called "User Scenario" is presented as step by step page flow with screenshots and any additional useful information that you want to provide. This documentation is always up to date with the current implementation and available to everybody involved in your Software Development Lifecycle.

For more hi level introduction about what scenarioo is, please refer to our web page: http://www.scenarioo.org

## Video: A Short Introduction to Scenarioo

Shows the essential features of Scenarioo and gives you some first insights into the Scenarioo documentation format structure.

<p><iframe width="883" height="497" src="https://www.youtube-nocookie.com/embed/C_Omyu190V4?rel=0" frameborder="0" allow="autoplay; encrypted-media" allowfullscreen></iframe></p>

## Scenarioo Overview

The following sections gives a basic introduction to Scenarioo and where to find more information for most important aspects in our documentation.

### What can Scenarioo do for me?

Scenarioo is a tool that helps you create an up-to-date software documentation leveraging existing UI tests. This can be an essential part of a "living documentation system" as described by Gojko Adzic in his book "Specification by Example".

With Scenarioo you can document your frontend application from a business and user perspective. Each UI test scenario is documented as a flow of screens. For a better overview, these scenarios are grouped into use cases. This gives a good high level picture of what an application is capable of and what typical scenarios you had in mind when implementing it.

The Scenarioo Viewer web application lets you browse the documentation easily and lets you jump from screen to screen, almost like a cangaroo (therefore our logo).

### What does Scenarioo consist of?

These are the parts of which Scenarioo is made of:

![Scenarioo Architecture](https://cloud.githubusercontent.com/assets/3780183/9653298/d03ba2dc-5222-11e5-80a4-9a58a6ccd004.png)

1. **Scenarioo Documentation Format**: A file format and folder structure for storing the automatic documentation of your user scenarios out of UI tests. As a user of Scenarioo you do not need to know much about this exact format, because you probably just want to use one of the existing writer libraries to write this format. If you want to know more details about the format, have a look at the [Scenarioo-Writer-Documentation-Format](features/Scenarioo-Writer-Documentation-Format.md).

2. **Scenarioo Writer libraries for different languages (currently available for Java, C# and JavaScript)**:
To generate the Scenarioo documentation files, you need to extend your UI tests (written in e.g. Protractor, Selenium, Robotium, etc.) so that they store information about use cases and scenarios into a documentation folder. The Scenarioo writer library helps you with this task. The Scenarioo Writer library knows how the documentation data has to be stored on disk, so that you can concentrate on supplying the relevant information. Have a look at [How to use a Scenarioo Writer Library](tutorial/How-to-use-Scenarioo-Writer-Library.md) to find out more.

3. **Scenarioo Viewer web application for browsing the generated documentation**: The web app makes the documentation available to anybody that is interested in it. This includes developers, business analysts and even business people. Therefore it let's you easily browse use cases, scenarios and their steps. If you just need to know how to set up the web app, have a look at the [Scenarioo Viewer Web Application Setup](tutorial/Scenarioo-Viewer-Web-Application-Setup.md).

### See Scenarioo in Action

Have a look at our Scenarioo Viewer Demo: http://demo.scenarioo.org

And see our [Collection of Examples](examples.md) for how to produce documentation data content. Some content produced by those examples is what you can also see in our Viewer Demo as example content.

There are also demo links for next release candidates of the Scenarioo Viewer that are currently under development, see [Downloads & Links](downloads-and-links.md).

### Setup & Use Scenarioo in Your Project

If you are interested how you can use Scenarioo in your own project, have a look here:

[How to Setup & Use Scenarioo](tutorial/README.md)

### Contribute to Scenarioo

If you want to contribute to Scenarioo please carefully read and follow the guidelines you find on this page:

[Information for Scenarioo Contributors](contribute/README.md)

### Information for Scenarioo Writer Developers

The Scenarioo Writer library is a core part of Scenarioo. The Java version of this library is the reference implementation. Furthermore we maintain a library for C# and JavaScript. If you want to contribute to one of this libraries or want to start a new Scenarioo Writer library in a different language, you will find most important required information on the following page.

[Scenarioo Writer Documentation Format](features/Scenarioo-Writer-Documentation-Format.md)

## Documentation Structure

You can find further information in this documentation folder, which is structured into following major sections:

* [Setup](tutorial/README.md): How to setup Scenarioo for a project
* [Features](features/README.md): Overview of the core concepts and architecture and detailed informations for specific features of Scenarioo.
* [Contribute](contribute/README.md): How developers can contribute to the Scenarioo Open Source Project
