package org.scenarioo.business.aggregator;

/**
 * Container for the current internal format version that this server supports.
 */
public class ServerVersion {

	/**
	 * Version of the internal file format of derived files in filesystem.
	 * 
	 * On import this version is stored for each build, if the format of a build does not correspond to this format, the
	 * build is automatically reimported again (all aggregation data recalculated in new format).
	 * 
	 * First part of the version corresponds to library version that is supported, second part of the version depends on
	 * internal aggregation format. the second part should be increased whenever something important is changed in the
	 * internal format or the way that the aggregator is caluclating internal data on builds.
	 */
	public static final String DERIVED_FILE_FORMAT_VERSION = "2.1.0";

}
