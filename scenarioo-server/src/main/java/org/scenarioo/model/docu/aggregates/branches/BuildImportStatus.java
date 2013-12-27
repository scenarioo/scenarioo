package org.scenarioo.model.docu.aggregates.branches;

public enum BuildImportStatus {
	
	UNPROCESSED, QUEUED_FOR_PROCESSING, PROCESSING, SUCCESS, FAILED, OUTDATED;
	
	public boolean isFailed() {
		return equals(FAILED);
	}
	
	public boolean isSuccess() {
		return equals(SUCCESS);
	}
	
	public boolean isImportNeeded() {
		return equals(UNPROCESSED) || equals(OUTDATED);
	}
	
}
