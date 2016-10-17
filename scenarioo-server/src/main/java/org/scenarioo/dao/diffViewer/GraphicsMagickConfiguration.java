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
			gmConsole.run(gmOperation.version());
		} catch (IOException e) {
			return false;
		} catch (InterruptedException e) {
			return false;
		} catch (IM4JavaException e) {
			return false;
		}
		return true;
	}
}
