package org.scenarioo.model.docu;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class GenerateScenariooApiSchema {

	private static final File SCHEMA_LOCATION = new File("D:/scenarioo-api.xsd");
/*
	public static void main(String[] args) throws Exception {
		Class[] classes = getClassesWithoutInterfacesAndTests("org.scenarioo.model.docu.feature.model");

		JAXBContext jaxbContext = JAXBContext.newInstance(classes);
		SchemaOutputResolver outputResolver = new MySchemaOutputResolver();
		jaxbContext.generateSchema(outputResolver);
	}
*/
	private static Class[] getClassesWithoutInterfacesAndTests(String packageName) throws ClassNotFoundException, IOException {
		Class[] classes = getClasses(packageName);
		List<Class<?>> filtered = new LinkedList<Class<?>>();
		for (Class aClass : classes) {
			if (!aClass.isInterface() && !isTest(aClass)) {
				filtered.add(aClass);
			}
		}
		return filtered.toArray(new Class[filtered.size()]);
	}

	private static boolean isTest(Class aClass) {
		return aClass.getSimpleName().endsWith("Test");
	}

	public static class MySchemaOutputResolver extends SchemaOutputResolver {

		@Override
		public Result createOutput(String namespaceURI, String suggestedFileName) throws IOException {
			StreamResult result = new StreamResult(SCHEMA_LOCATION);
			result.setSystemId(SCHEMA_LOCATION.toURI().toURL().toString());
			return result;
		}

	}

	/**
	 * Scans all classes accessible from the context class loader which belong to the given package and subpackages.
	 *
	 * @param packageName The base package
	 * @return The classes
	 */
	private static Class[] getClasses(String packageName)
		throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		assert classLoader != null;
		String path = packageName.replace('.', '/');
		Enumeration<URL> resources = classLoader.getResources(path);
		List<File> dirs = new ArrayList<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		ArrayList<Class> classes = new ArrayList<Class>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName));
		}
		return classes.toArray(new Class[classes.size()]);
	}

	/**
	 * Recursive method used to find all classes in a given directory and subdirs.
	 *
	 * @param directory   The base directory
	 * @param packageName The package name for classes found inside the base directory
	 * @return The classes
	 * @throws ClassNotFoundException
	 */
	private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
		List<Class> classes = new ArrayList<Class>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				assert !file.getName().contains(".");
				classes.addAll(findClasses(file, packageName + "." + file.getName()));
			} else if (file.getName().endsWith(".class")) {
				classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

}
