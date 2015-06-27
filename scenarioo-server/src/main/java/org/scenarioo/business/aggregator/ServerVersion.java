package org.scenarioo.business.aggregator;

/**
 * Container for the current internal format version that this server supports.
 * 
 * First part of the version corresponds to library version that is supported, second part of the version depends on
 * internal aggregation format.
 * 
 * On import this version is stored for each build, if the format of a build does not correspond to this format, the
 * build is automatically reimported again (all aggregation data recalculated in new format).
 */
public class ServerVersion {

	/**
	 * Version of the internal file format of derived files in filesystem.
	 * 
	 * he data aggregator checks whether the file format is the same, otherwise the data has to be recalculated.
	 */
	public static final String DERIVED_FILE_FORMAT_VERSION = "2.0.1.dev3";

}
