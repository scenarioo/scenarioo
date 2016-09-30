package org.scenarioo.dao.diffViewer;

import org.im4java.core.CompareCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.process.ArrayListOutputConsumer;

import java.io.IOException;

public class GraphicsMagickConfiguration {
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
			gmConsole.run(gmOperation);
		} catch (IOException e) {
			// Ignore, we only want to know if graphics magick could be found
		} catch (InterruptedException e) {
			// Ignore, we only want to know if graphics magick could be found
		} catch (IM4JavaException e) {
			return !isCausedByIOException(e);
		}

		return true;
	}

	private static boolean isCausedByIOException(Throwable e) {
		Throwable cause = e.getCause();
		if (cause == null) {
			return false;
		} else if (cause instanceof IOException) {
			return true;
		} else {
			return isCausedByIOException(cause);
		}
	}
}
