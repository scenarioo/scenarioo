# Details - Additional Documentation Information

Details are a generic data structure used for storing additional (application specific) information inside the Scenarioo documentation.

This `details` are simple maps of key-value-pairs. Each `entry` in such a details map has a `key` and a `value`. 

The `key` is always a simple string (the name of the information). 

The `value` of such an entry can be of different kinds:
   * `string`: a simple textual information
   * `ObjectDescription` (complex type): describes an object with an identity given by a `type` (string to group all objects of same kind) and a unique `name` to identify this object of this type (the `name` should be unique for all objects of this same type, such that all occurrences of the same object can be identified correctly). Every value of type ObjectDescription will be stored in the Scenarioo object repository, which means, that you can easily browse for all occurrences of this same object with same type and name. Such an object typically can have again `details` with additional information about the object (this details can again recursively contain `ObjectDescritpion` or any other value types, as listed here).
   * `ObjectReference` (complex type): possibility to only store a reference to an object (only by `type` and `name`, without `details`), to reference the full object that is already stored in some other place inside the documentation (with all its details as a full `ObjectDescription`).
   * `ObjectList`: possibility to store a list of values (e.g. as a bullet list). The contained values could be simply strings, or again of `ObjectDescription` or any other supported value type, as listed here.
   * `ObjectTreeNode`: possibility to store tree structures. Each tree node has an `item` which is the payload of the node, that can be a simple string information or again an `ObjectDescription` or any other supported value type, as listed here. Furthermore each tree node can again have `details` for specific additional information about an item (e.g. the item could be an `ObjectDescription` or `ObjectReference` and the `details` of the tree node contain additional information that is only valid for this specific occurrence of this object inside this tree but not belongs to the object itself). The `children` are again `ObjectTreeNode`s (which are the sub trees of the tree).

See also some data structure examples below.

### Scenarioo Object Repository: the power of `ObjectDescription`s

The Scenarioo Object Repository will index all your objects of type `ObjectDescription` inside your documentation (in any places). 

You can easily add additional so called "custom object tabs" to the start page of your Scenarioo documentation in the Scenarioo Viewer web app. Such a "custom object tab" let's you browse all objects of one or serveral object types. Through this you can enable all Scenarioo Viewer users to easily find all occurrences of a specific object in your Scenarioo documentation.

### Adding Custom Object Tabs to the Homepage

By adding custom object tabs to your Scenarioo home page, you can enable browsing all objects of some type(s) (almost the same way as use cases can be browsed on the entry page). This feature currently has to be enabled by adding some XML fragment to your Scenarioo configuration file (`config.xml`).

The following XML fragment added to your configuration file, will make all your objects in your scenarioo documentation of the types listed as `<objectTypesToDisplay>` browsable in one searchable object tree inside a new tab called "Calls" on the home page of Scenarioo. This example lists all objects of type "service", "businessOperation" and "uiAction" in one tree, just as you can see it in the Demo of Scenarioo. Also the details properties "description" and "realName" are displayed in the resulting tree table as columns.

```xml
    <customObjectTabs>
        <id>calls</id>
        <tabTitle>Calls</tabTitle>
        <objectTypesToDisplay>service</objectTypesToDisplay>
        <objectTypesToDisplay>businessOperation</objectTypesToDisplay>
        <objectTypesToDisplay>uiAction</objectTypesToDisplay>
        <customObjectDetailColumns>
            <columnTitle>Description</columnTitle>
            <propertyKey>description</propertyKey>
        </customObjectDetailColumns>
        <customObjectDetailColumns>
            <columnTitle>Real Name for Service</columnTitle>
            <propertyKey>realName</propertyKey>
        </customObjectDetailColumns>
    </customObjectTabs>
```
This example is an extract from the demo configuration, for full example configuration file see [config.xml](https://github.com/scenarioo/scenarioo/blob/develop/scenarioo-docu-generation-example/src/test/resources/config-for-demo/config.xml)

### Details Data Structure Examples

#### Example 1 - Simple String Entries

For adding some simple information, you could simply provide entries with string data as values:

    <details>
        <entry>
            <key>key1</key>
            <value xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="xs:string">value1</value>
        </entry>
        <entry>
            <key>key2</key>
            <value xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="xs:string">value2</value>
        </entry>
    </details>

#### Example 2 - ObjectList with ObjectDescriptions

You could e.g. add a list of all service calls (as object descriptions) that occur on a step:

    <details>
        <entry>
            <key>Service Calls</key>
            <value xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:type="objectList">
                <items xsi:type="objectDescription">
                    <name>storeUserSettings</name>
                    <type>serviceCall</type>
                    <details>
                        <entry>
                            <key>URL</key>
                            <value xsi:type="xs:string">http://localhost:8080/rest/storeUserSettings</value>
                        </entry>
                        <entry>
                            <key>parameters</key>
                            <value xsi:type="xs:string">username, settings</value>
                        </entry>
                    </details>
                </items>
                <items xsi:type="objectDescription">
                    <name>loadNotifications</name>
                    <type>serviceCall</type>
                    <details>
                        <entry>
                            <key>URL</key>
                            <value xsi:type="xs:string">http://localhost:8080/rest/notifications</value>
                        </entry>
                        <entry>
                            <key>parameters</key>
                            <value xsi:type="xs:string">userName</value>
                        </entry>
                    </details>
                </items>
            </value>
        </entry>
    </details>

Thanks to the object repository you can then configure an additonal "custom object tab" to appear on the start page of the Scenarioo Viewer web app to make all objects of type "serviceCall" searchable in an additional tab on the Scenarioo Viewer start page.
 
 See [Configuration of Custom Object Tabs](../tutorial/Configuration.md#custom-object-tabs) for how to configure such custom object tabs to browse your details objects in Scenarioo.

