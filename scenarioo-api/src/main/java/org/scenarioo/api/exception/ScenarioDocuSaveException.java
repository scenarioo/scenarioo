package org.scenarioo.api.exception;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

public class ScenarioDocuSaveException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	List<RuntimeException> causingExceptions;
	
	public ScenarioDocuSaveException(final List<RuntimeException> causingExceptions) {
		super(
				"Could not save all files in ScenarioDocuGenerator (see attached causing exceptions).");
		this.causingExceptions = causingExceptions;
	}
	
	@Override
	public void printStackTrace(final PrintWriter s) {
		printStackTrace(s, new ExceptionPrinter() {
			@Override
			public void printStackTrace(final RuntimeException e) {
				e.printStackTrace(s);
			}
		});
	}
	
	@Override
	public void printStackTrace(final PrintStream s) {
		printStackTrace(s, new ExceptionPrinter() {
			@Override
			public void printStackTrace(final RuntimeException e) {
				e.printStackTrace(s);
			}
		});
	}
	
	private void printStackTrace(final Appendable s, final ExceptionPrinter exceptionPrinter) {
		println(s, causingExceptions.size()
				+ " documentation file(s) could not be saved. See following causing exceptions: ");
		int index = 0;
		for (RuntimeException e : causingExceptions) {
			index++;
			println(s, ">>> Exception number " + index + " on saving documentation file:");
			exceptionPrinter.printStackTrace(e);
		}
	}
	
	private void println(final Appendable s, final String message) {
		try {
			String lineSeparator = String.format("%n");
			s.append(message + lineSeparator);
		} catch (IOException e) {
			throw new RuntimeException("Could not print stack trace for very unexpected reason.");
		}
	}
	
	private static interface ExceptionPrinter {
		public void printStackTrace(final RuntimeException e);
	}
	
}
