package org.scenarioo.dao.diffViewer;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.im4java.core.CompareCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.ArrayListOutputConsumer;

public class GraphicsMagickConfiguration {
	private static final Logger LOGGER = Logger.getLogger(GraphicsMagickConfiguration.class);

	private static Boolean isAvailable;

	public static synchronized boolean isAvailable() {
		if (isAvailable == null) {
			isAvailable = isGraphicsMagickAvailable();
		}
		return isAvailable;
	}

	private static boolean isGraphicsMagickAvailable() {
		CompareCmd gmConsole = new CompareCmd(true);
		gmConsole.setOutputConsumer(new ArrayListOutputConsumer());
		IMOperation gmOperation = new IMOperation();
		try {
			gmConsole.run(gmOperation.version());
		} catch (IOException | InterruptedException | IM4JavaException e) {
			LOGGER.warn("GraphicsMagick not available. Diff Viewer functionality won't be available.", e);
			return false;
		}
		return true;
	}
}
