# Eclipse IDE Settings


This page explains how to configure your Eclipse IDE properly, such that all developers share the same formatting and compiler settings and can work without producing problems or conflicts.

You can find all needed eclipse setting files stored in following location:
https://github.com/scenarioo/scenarioo/tree/develop/ide-settings/eclipse

**IMPORTANT: EVERY DEVELOPER MUST TAKE CARE TO SETUP THIS SETTINGS CORRECTLY EACH TIME YOU SETUP A NEW ECLIPSE WORKSPACE, EXACTLY AS EXPLAINED HERE!!**

All developers have to follow the following instructions to use the same eclipse formatting and compiler settings:

## 1. Apply Preferences

### 1.1 Main Preferences

The file preferences_codestyle.epf and preferences_compiler.epf contains following settings that should be similar for all developers for scenarioo: 
* Global java code style preferences: format/imports/etc.
* Global java compiler preferences: compiler/warnings/errors/etc.

We had to separate these preferences into two files because there seems to be a bug in eclipse with import process when having both preferences in same file.
	
Import those settings into your eclipse, by performing the following steps:

1. File / Import / General / Preferences
2. Choose the preferences_compiler.epf file and choose to import everything.
3. Repeat the same for preferences_codestyle.epf
4. Because of a bug in eclipse the formatting settings do not get applied automatically, therefore you have to do the following after the import:
    * go to Window/Preferences
    * choose Java/Code Style/Formatter
    * verify active profile: should be "scenarioo"
    * select "edit...", do not change anything and select "OK"
    * click "Apply"
    * click "OK"
5. **IMPORTANT: MAKE SURE TO TEST WHETHER YOUR FORMAT SETTINGS ARE CORRECTLY APPLIED BEFORE CONTINUING AS FOLLOWS:** This is important because otherwise you or other developer might have merging troubles because of too much unwanted changes because of unwnated reformattings of existing code.
       * Choose a unchanged Java File from scenarioo-server in Eclipse and open it (choose one that is allready formated correctly, e.g. use the class ObjectReferenceTreeBuilder for testing this). 
       * Select Ctrl+Shift+F to format the whole file
       * Since you did not change anything in the file, it should not reformat anything
       * Make a diff to see which changes have been done to the file, there should be no changes at all.
       * If there are formating changes to the file, DO NOT CONTINUE TO CHANGE ANY FILES with this wrong formatting settings and immediately contact a lead developer to clarify correct formatting settings. Thanks!

From now on, do not change any of these settings in the Eclipse preferences dialog. All developers should use the same settings.

### 1.2 Codetemplates Preferences

The codetemplates contain templates for new class files, methods etc. 
The templates are different for different scenarioo projects because of the different licenses in the file headers.
	
Therefore you have to apply these settings project by project, as follows:

1. Go to each project and choose Properties / Java Code Style / Code Templates
2. Choose [x] Enable project specific settings
3. Choose Import ...
4. Choose the appropriate file for your project from directory scenarioo/ide-settings/eclipse:
  * codetemplates-api.xml	for scenarioo-java and any other writer library project
  * codetemplates-server.xml	for scenarioo-server and any other strictly GNU licensed code
  * codetemplates-examples.xml	for scenraioo example projects that can be freely reused
5. Choose [x] Automatically add comments for new methods and types

### 1.3 Auto Save Action

Unfortunately the Auto Save settings can not easily be shared through files. 
Therefore every developers MUST set the following manually (**bold settings are critical, different settings may cause merge conflicts!**):

1. Choose Window / Preferences / Java / Editor / Save Actions 
2. Choose following settings:
  * [x] Perform the selected actions on save
  * [x] **Format source code: (x) Format edited lines**
  * [x] Organize imports
  * [x] Additional actions
3. Select "Configure" and configure following settings:
  * Code Organizing :
    * [x] **Remove trailing whitespace: ( ) Ignore empty lines (do not select this! cause intelliJ does not ignore them) **
    * [ ] **Correct indentation (IMPORTANT: DO NEVER select this!!!)**
    * [ ] **Sort members (DO NEVER select this!)**
  * Code Style:
    * [x] Use blocks in if/while/gfor/do: (x) always
    * [ ] Use parentheses in expressions: not selected!
    * [x] Use modifier 'final' where possible: [x] private fields [x] Parameter [ ] local variables
  * Member Accesses:
    * [ ] Use 'this' qualifier for field access (NOT CHECKED!)
    * [ ] Use 'this' qualifier for method access (NOT CHECKED!)
    * [x] Use declaring class as qualifier
    * [ ] Qualify field accesses
    * [ ] Qualify method accesses
    * [x] Change all accesses through subtypes
    * [x] Change all accesses through instances
  * Missing Code:
    * [x] Add missing annotations:
    * [x] @Override: [x] Implementations of interface methods
    * [x] @Deprecated
  * Unnecessary Code:
    * [x] Remove unused imports
    * [ ] Remove unsued private members (you may check this if you like)
    * [ ] Remove unsued local variables (you may check this if you like)
    * [x] Remove unnecessary casts
    * [x] Remove unnecessary $NON-NLS$ tags
4. verify that your settings are correct, by doing the following:
   * Open class "DummyIdeFormattingSettingTestClass"
   * In the first method enter some code that should be formatted
   * Choose Ctrl+S to save your changes
   * the part of the code that you entered should become properly formatted
   * the rest of the code should not be reformatted (also no removed trailing whitespaces or empty lines!)
   * contact a lead developer to find out what is wrong, in case this is not the case (do not continue with bad formatting settings like these)

### 1.4 Editor Contentassist Templates

There are additional content assist templates for Eclipse that are helpful.

These can be imported as follows:

1. Go to Window / Preferences / Java / Editor / Templates / Import ...
2. Select all the files inside the folder 'editor-contentassist-templates' you want to import (only select those you do not have yet, otherwise you will get duplicates)

## 2. Export Preferences
In case you have changes to the eclipse settings that have to be shared with other developers ...

### 2.1 Export Code Style Preferences

1. File / Export ... / Preferences
2. Choose "Java Code Style Preferences" only
3. Export to file preferences_codestyle.epf
4. Finish

### 2.2 Export Compiler Preferences

1. File / Export ... / Preferences
2. Choose "Java Compiler Preferences" only
3. Export to file preferences_compiler.epf
4. Finish


### 2.3 Export Codetemplate Preferences

1. Go to the project where you changed the codetemplates. 
2. Choose Properties / Java Code Style / Code Templates / Export All ...
3. Choose the file where to export the codetemplates for current project or project type.

### 2.4 Export Auto Save Preferences

Unfortunately these settings can not be shared through files, you have to change the manual setup description (see above) and inform all developers about these changes.

### 2.5 Export Editor Contentassist Templates

1. Go to Window / Preferences / Java / Editor / Templates 
2. Choose only the new or changed templates that you want to export and select Export ...
3. Make a new file inside folder 'editor-contentassist-templates' for your templates to share.
4. inform the developers later about the new file (as soon as commited and published, see 2.5)!

### 2.6 Commiting your changed or new preference files
	
1. Before commiting your changed eclipse settings please compare your changes carefully: 
  * did you realy only change those parts of the preferences files that you wanted to change?
2. After commiting and pushing the new preferences to the repository:
  * Please inform all developers about the changes and ask them to reimport the changed settings into their eclipse: Send an email for this purpose to scenarioo-devs mailing list.
