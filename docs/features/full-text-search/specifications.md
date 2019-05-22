# Scenarioo Full Text Search - Specification by Example

This test cases specify how the full text search is supposed to behave in different situations.

## Configuration of the search feature

### TC1: By default the full text search feature is switched off

* The default config file that comes with Scenarioo does not contain
  the elasticSearchEndpoint config tag.
* If the elasticSearchEndpoint config tag is not present
  the full text search feature is switched off
  and the Manage -> General Settings -> Full Text Search section shows
  how the Elasticsearch  endpoint can be configured in the config.xml
  file.
* Because the search feature is switched off, the search box is not
  shown in the Scenarioo header.

### TC2: Elasticsearch enabled but endpoint not available

Set the elasticSearchEndpoint tag in config.xml to an endpoint
that has a valid format but where no Elasticsearch instance is 
running (e.g. localhost:9000).

* Search box is not shown in Header
* Settings page shows the configured Elasticsearch endpoint
* Settings page shows that Elasticsearch is not reachable

### TC3: Elasticsearch enabled and endpoint available

Set the elasticSearchEndpoint tag in config.xml to the endpoint where
Elasticsearch is actually available (e.g. localhost:9300).

* Search box is shown in the header
* Settings page shows the configured Elasticsearch endpoint
* Settings page shows that Elasticsearch is reachable
* Search for "Donate" shows a result, result can be opened


## Search

### TC4: Search can be triggered by pressing enter in the search box

### TC5: No results found

Enter a search term that should not yield any results (e.g. aaaaaaaa)

* Message is shown, that no results were found
* There's a link for the user to go back to the Use Case overview ("home" in the breadcrumbs)

### TC6: Search for various things -> SOME FAILING

Perform these searches in the self-docu:

* Use Case title ("breadcrumbs" in double quotes should show the UC "Use breadcrumbs" and some others) -> OK
* Use Case title ("breadcrumbs" without double quotes should show the UC "Use breadcrumbs" and some others) -> OK
* Use Case description ("reference" in double quotes should show the UC "Browse object details") -> OK
* Scenario title:
  "unique" (without double quotes)
  - should show UC "Manage branch aliases" and scenario "Unique aliases" -> OK
  - should show Create%20sketch/New%20issue%20success/scenarioo-fork-full-text-search_/0/1 -> FAILS, not shown
  "unique" (with double quotes)
  - should show UC "Manage branch aliases" and scenario "Unique aliases" -> OK
  - should show Create%20sketch/New%20issue%20success/scenarioo-fork-full-text-search_/0/1 -> OK
* Scenario description ("access" should show UC "Application Startup" with scenario "First visit") -> OK
* Page name ("scenarioo-fork-full-text-search_" in quotes should show all pages) -> FAILS, only three use cases shown
* Step name ("A different page variant" in quotes should show the "Browse page variants" step that contains this as a title) -> OK
* Class in HTML source ("label-success" in quotes should show all) -> FAILS, shows only a few pages, e.g. the "Manage branch aliases" is missing.
* Search for simple word in HTML source ("StartInitAction" should show a few pages) -> OK
* Search for something that is part of all HTML sources ("html" should show all UCs, scenarios and steps as result) -> FAILS, only three UCs are shown

Perform these searches in the Wikipedia Example, 2014-03-19:

* Use Case label ("normal-case" in quotes should find one use case) -> OK
* Scenario metadata ("a_cat_by_the_tail" without quotes should show all scenarios) -> OK
* Scenario metadata ("a_cat_by_the_tail" with quotes should also show the relevant scenarios) -> FAILS, shows irrelevant pages
* Step metadata ("no user rights" in quotes should show almost all steps) -> FAILS, some steps are missing that should show up (e.g. the "Switch Language" UC)
* Step label ("step-label-0" in quotes should show 8 steps) -> OK


## UI

### TC7: Breadcrumbs are shown

* Breadcrumbs show "Home" -> "Search Results for <search term>"
* Share this page works as usual

### TC8: Search box looks good and does not mess up navigation bar

* The right end of the search box is aligned with the right end of the breadcrumbs
* The bottom end of the search box is aligned with the navigation bar labels (e.g. Manage and Info)

### TC9: The search term is not part of the query string but instead of the path

* The path contains `/search/<term>` not `/search?q=term`

### TC10: Pressing enter in an empty search field does not trigger any actions


## Edge cases and error messages

### TC11: Server does not have an index for a branch

* List all indexes in the browser: http://localhost:9200/_cat/indices?v
* Delete the index of a branch: curl -XDELETE 'localhost:9200/scenarioo-wikipedia-docu-example-2014-03-19?pretty'
* Then search for something.
* The error message shows "The search index was not found for the selected build."

### TC12: Search terms with special characters

* / should not execute search
* " or "something should show an error message in the UI (this results in an elasticsearch parse error)
* `<?ç+°'&%>` should execute search but not find any results

### TC13: Index is deleted and recreated on reimport

* Import build
* Change something in the build data (e.g. add a new keyword that did not have
  results before or delete an entire Use Case folder)
* Reimport build and check whether the changes show up when searching

### TC14: Search engine temporarily not reachable

* Search field is shown, then turn off the Elasticsearch service.
* Enter something into the search field and press enter.
* The error message shows "The search engine is not running or not reachable for Scenarioo."
