package org.scenarioo.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class NumberFormatterTest {

	@Test
	public void formatMinimumThreeDigits_positiveNumbersAndZero_returnsFormattedNumber() {
		assertEquals("000", NumberFormatter.formatMinimumThreeDigits(0));
		assertEquals("001", NumberFormatter.formatMinimumThreeDigits(1));
		assertEquals("999", NumberFormatter.formatMinimumThreeDigits(999));
		assertEquals("1000", NumberFormatter.formatMinimumThreeDigits(1000));
		assertEquals("9223372036854775807", NumberFormatter.formatMinimumThreeDigits(Long.MAX_VALUE));
	}

	@Test
	public void formatMinimumThreeDigits_negativeNumbers_throwsException() {
		assertFormattingThrowsException(Long.MIN_VALUE);
		assertFormattingThrowsException(-1234512345);
		assertFormattingThrowsException(-2);
		assertFormattingThrowsException(-1);
	}

	private void assertFormattingThrowsException(long number) {
		try {
			NumberFormatter.formatMinimumThreeDigits(number);
			fail("Should have thrown RuntimeException when formatting " + number);
		} catch(RuntimeException e) {
			assertEquals("Encountered a negative number, which must be a bug: " + number, e.getMessage());
		}
	}

}
