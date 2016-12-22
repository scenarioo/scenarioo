# Safe Identifiers

The following fields are used as identifiers to identify their objects in the scenrioo documentation:

* branch name
* build name
* use case name
* scenario name
* page name
* object type
* object name
* label identifiers (with additional restrictions, see Labels)

This identifiers are also used in URLs in the viewer web app to directly link to such resources. As well as to identify the same objects between different builds (e.g. for Diff Viewer feature).

Because this identifiers are used in URLs the following characters are disallowed in such identifiers:

* /
* \

Libraries should replace such characters by some other allowed character, like `_`, when writing the documentation. Scenarioo Writer Libraries usually take care of this for the user.
