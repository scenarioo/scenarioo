package org.scenarioo.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NumberFormatterTest {

	@Test
	void formatMinimumThreeDigits_positiveNumbersAndZero_returnsFormattedNumber() {
		assertEquals("000", NumberFormatter.formatMinimumThreeDigits(0));
		assertEquals("001", NumberFormatter.formatMinimumThreeDigits(1));
		assertEquals("999", NumberFormatter.formatMinimumThreeDigits(999));
		assertEquals("1000", NumberFormatter.formatMinimumThreeDigits(1000));
		assertEquals("9223372036854775807", NumberFormatter.formatMinimumThreeDigits(Long.MAX_VALUE));
	}

	@Test
	void formatMinimumThreeDigits_negativeNumbers_throwsException() {
		assertFormattingThrowsException(Long.MIN_VALUE);
		assertFormattingThrowsException(-1234512345);
		assertFormattingThrowsException(-2);
		assertFormattingThrowsException(-1);
	}

	private void assertFormattingThrowsException(long number) {
		final RuntimeException thrown = assertThrows(RuntimeException.class, () -> NumberFormatter.formatMinimumThreeDigits(number), "Should have thrown RuntimeException when formatting " + number);
		assertEquals("Encountered a negative number, which must be a bug: " + number, thrown.getMessage());
	}

}
