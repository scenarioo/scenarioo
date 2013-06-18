


REST stuff:
 - compilation problems, wadl modifications
	1. select ngusd-web/Run As/Maven clean
	2. select ngusd-web/Run As/Maven generate-sources
	3. done
 
 - manual test:
    http://localhost:8080/ngusd/rest/branches
 
 
 - request/response monitoring: use Eclipse/Window/Preferences/Run-Debug/TCP-IP Monitor

Files:
	/ngusd-web/src/main/wadl/examples.wadl															defines our REST services -> 
	/ngusd-web/src/main/resources/rest/xsd/examples.xsd												defines data structure for POST requests and responses
	/ngusd-web/src/main/java/org/ngusd/rest/examples/EchoImpl.java									rest service implementation
	/ngusd-web/target/generated-sources/cxf/org/ngusd/rest/generated/examples/Echo.java 			generated service interface
	/ngusd-web/target/generated-sources/cxf/org/ngusd/rest/generated/examples/model/EchoItem.java	generated data structure, from examples.xsd
	
	
TODO:
 - unit test
 - json instead of xml