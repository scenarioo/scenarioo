# Viewer Configuration

Most of the features that are configurable in Scenarioo can be configured directly through the Configuration pages inside the Scenarioo webapplication. Just use the link "Manage" in the upper right corner.

The following sections describe more advanced configuration options that can only be configured through the viewer's `config.xml` file and can not be configured yet through the Scenarioo configuration pages directly. 

All the listed configuration possibilities are optional and you only need to follow these instructions if you want to use these additional features.

## Full Text Search

For enabling full text search inside your Scenarioo documentation you have to setup an Elasticsearch engine, as described in [Full Text Search Setup Instructions](../features/full-text-search/setup.md)

## Diff Viewer Comparisons

If you want to enable diff comparisons between two versions of your generated documentation builds (e.g. to see what changed in the last automated build run compared to another build run, including also screenshot comparisons) you have to follow the [DiffViewer Setup Instructions](../features/diff-viewer/setup.md)

## Branch Selection List Ordering

The order of the branch entries in the top level navigation branch selection dropdown is configurable.

Through an additional config property `branchSelectionListOrder` the following ordering options can be configured:
* `name-ascending`: the branches are sorted by name alphabetically - default value, if value not set the behaviour is the same
* `name-descending`: branches are sorted in descending order according their branch name - useful for projects that have names with version or release date or stuff like that inside
* `last-build-date-descending`: branches are sorted by the last build date

Example:
```xml
<branchSelectionListOrder>name-descending</branchSelectionListOrder>
```

## IMPORTANT Remarks

1. Changing the configuration file manually always needs a server restart such that the changes take effect.

2. Defining a new tab in the configuration, needs reimport of your builds to generate the data for the configured tabs. Just go to the "Manage" page and click on the refresh icons behind each build in the tab "Builds" to trigger a reimport.

You can also define more than one such tab, and a tab can also only list objects of one type (just as a list, no tree then).

The default configuration since Scenarioo version 2.0 already comes with two such simple object tabs predefined: Labels and Pages, to list all pages and labels in your documentation. If you upgrade from version 1.x you have to manually enable this two tabs by adding the following to your configuration:

```xml
     <customObjectTabs>
        <id>pages</id>
        <tabTitle>Pages</tabTitle>
        <objectTypesToDisplay>page</objectTypesToDisplay>
     </customObjectTabs>
     <customObjectTabs>
        <id>labels</id>
        <tabTitle>Labels</tabTitle>
        <objectTypesToDisplay>label</objectTypesToDisplay>
     </customObjectTabs>
```
In case you are using the default version 2.0 configuration and you do not want to see "Labels" and "Pages" in your documentation, you can remove those tabs by removing this configuration part from your config.xml file and restart the server.

