package org.scenarioo.business.feature.importer;


import org.scenarioo.model.docu.feature.model.Branch;
import org.scenarioo.model.docu.feature.model.Build;
import org.scenarioo.model.docu.feature.model.ImportFeature;
import org.scenarioo.model.docu.feature.model.Scenario;

import java.io.File;
import java.util.*;

public class Validator<T> {


	interface FileFilter{
		boolean accept(File file);
	}

	private List<File> getFileList(String folder, FileFilter filter){
		ArrayList<File> list = new ArrayList<>();
		File[] files = new File(folder).listFiles();
		for(File f:files){
			if (filter.accept(f)){
				list.add(f);
			}
		}
		return list;
	}

	private static HashMap<Class, String> typeFiles = new HashMap<>();
	{
		typeFiles.put(ImportFeature.class,  "feature.xml" );
		typeFiles.put(Branch.class,         "branch.xml"  );
		typeFiles.put(Build.class,          "build.xml"   );
		typeFiles.put(Scenario.class, 		"scenario.xml");
	}

	public static Map<Class, String> getTypeFiles(){
		return typeFiles;
	}

	private Class<T> type;
	private String currentFolder;
	public Validator(Class<T> clazz, String currentFolderPath){
		type = clazz;
		currentFolder = currentFolderPath;
	}

	public boolean isTypeFolder(String folderName){
		File folder = new File(currentFolder + "/" + folderName);
		if (!folder.isDirectory()) return false;
		return getFileList(folder.getAbsolutePath(), file -> file.getName().equals(typeFiles.get(type)) && file.isFile()).size() > 0;
	}

}
