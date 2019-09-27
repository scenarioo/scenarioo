package org.scenarioo.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IdGeneratorTest {

	@Test
	void randomIdHasLength8() {
		assertEquals(8, IdGenerator.generateRandomId().length());
	}

	@Test
	void randomValuesAreAlwaysDifferent() {
		String first = IdGenerator.generateRandomId();
		String second = IdGenerator.generateRandomId();
		assertNotEquals(first, second);
	}

	@Test
	void allHashesAreOfLength8() {
		assertEquals(8, IdGenerator.generateIdUsingHash("").length());
		assertEquals(8, IdGenerator.generateIdUsingHash("hello").length());
		assertEquals(8, IdGenerator.generateIdUsingHash("hello world").length());
	}

	@Test
	void hashOfNullString_throwsException() {
		assertThrows(NullPointerException.class, () -> IdGenerator.generateIdUsingHash(null));
	}

	@Test
	void hashOfEmptyString() {
		assertEquals("da39a3ee", IdGenerator.generateIdUsingHash(""));
	}

	@Test
	void hashOfNonEmptyString() {
		assertEquals("3e3de94a", IdGenerator.generateIdUsingHash("Scenarioo"));
	}

}
