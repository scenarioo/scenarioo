/* scenarioo-api
 * Copyright (C) 2014, scenarioo.org Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As a special exception, the copyright holders of this library give you 
 * permission to link this library with independent modules, according 
 * to the GNU General Public License with "Classpath" exception as provided
 * in the LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.api.exception;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.List;

public class ScenarioDocuSaveException extends RuntimeException {
	
	List<RuntimeException> causingExceptions;
	
	public ScenarioDocuSaveException(final List<RuntimeException> causingExceptions) {
		super("Could not save all files in ScenarioDocuGenerator (see attached causing exceptions).");
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
