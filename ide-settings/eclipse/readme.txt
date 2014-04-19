
The files in this directory contain the eclipse settings to be used for coding scenarioo in eclipse.

1. Apply Preferrences

1.1 Main Preferrences:

	The file preferences.xml contains following settings that should be similar 
	for all developers for scenarioo:
	- Global java code style preferences: format/imports/etc.
	- Global java compiler preferences: compiler/warnings/errors/etc.
	
	Simply import the preferences.epf file into your eclipse:
	1. File / Import ... / Preferences
	2. Choose the preferences.epf file and choose to import everything

	Do not change any of these settings in the eclipse preferences dialog
	that should be similar for all developers!

1.2 Codetemplates Preferences:

	The codetemplates contain templates for new class files, methods etc. 
	The templates are different for different scenarioo projects 
	because of the different licenses in the file headers.
	
	Therefore you have to apply these settings project by project, as follows:
	1. Go to each project and choose Properties / Java Code Style / Code Templates 
	2. Choose [x] Enable project specific settings
	3. Choose Import ...
	4. Choose the appropriate file for your project:
		codetemplates-api.xml		for scenarioo-api and any other library project
		codetemplates-server.xml	for scenarioo-server and any other strictly GNU licensed code
		codetemplates-examples.xml	for scenraioo example projects that can be freely reused

1.3 Auto Save Action

	Unfortunately the Auto Save settings can not easily be shared through files. 
	Therefore every developers MUST set the following manually:

	1. Choose Window / Preferences / Java / Editor / Save Actions 
	2. Choose following settings:
		[x] Perform selected actions on save
		[x] Format source code: (x) format all lines
		[x] Organize imports
		[x] Additional actions
	3. Select "Configure" and configure following settings:
		Code Organizing:
		[x] Remove trailing whitespace: (x) all lines
		[x] Correct indentation
		[ ] Sort members (DO NOT select this!)
		Code Style:
		[x] Use blocks in if/while/gfor/do: (x) always
		[ ] Use parentheses in expressions: not selected!
		[x] Use modifier 'final' where possible: [x] private fields [x] Parameter [ ] local variables
		Member Accesses:
		[ ] Use 'this' qualifier for field access (NOT CHECKED!)
		[ ] Use 'this' qualifier for method access (NOT CHECKED!)
		[x] Use declaring class as qualifier
		[ ] Qualify field accesses
		[ ] Qualify method accesses
		[x] Change all accesses through subtypes
		[x] Change all accesses through instances
		Missing Code:
		[x] Add missing annotations:
		[x] @Override: [x] Implementations of interface methods
		[x] @Deprecated
		Unnecessary Code:
		[x] Remove unused imports
		[ ] Remove unsued private members (you may check this if you like)
		[ ] Remove unsued local variables (you may check this if you like)
		[x] Remove unnecessary casts
		[x] Remove unnecessary $NON-NLS$ tags

1.4 Editor Contentassist Templates

	There are additional content assist templates for Eclipse that are helpful.

	These can be imported as follows:
	1. Go to Window / Preferences / Java / Editor / Templates / Import ...
	2. Select all the files inside the folder 'editor-contentassist-templates' you want to import
		(only select those you do not have yet, otherwise you will get duplicates)



2. Export Preferences:

	In case you have changes to the eclipse settings that have to be shared with other developers ...

2.1 Export Main Preferences

	1. File / Export ... / Preferences
	2. Choose "Jva Code Style Preferences" and "Java Compiler Preferences" only
	3. Choose the preferences.epf file to store into
	4. Finish

2.2 Export Codetemplate Preferences:

	1. Go to the project where you changed the codetemplates. 
	2. Choose Properties / Java Code Style / Code Templates / Export All ...
	3. Choose the file where to export the codetemplates for current project or project type.

2.4 Editor Contentassist Templates

	1. Go to Window / Preferences / Java / Editor / Templates 
	2. Choose only the new or changed templates that you want to export and select Export ...
	3. Make a new file inside folder 'editor-contentassist-templates' for your templates to share.
	4. inform the developers later about the new file (as soon as commited and published, see 2.5)!

2.5 Commiting your changed or new preference files:
	
	1. Before commiting your changed eclipse settings please compare your changes carefully:
		did you realy only change those parts of the preferences files that you wanted to change?
	2. After commiting and pushing the new preferences to the repository:
		Please inform all developers about the changes and ask them to reimport the changed settings 
		into their eclipse. 
		Send an email for this purpose to scenarioo-devs@googlegroups.com


