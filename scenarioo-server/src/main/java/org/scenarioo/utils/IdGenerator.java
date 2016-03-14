package org.scenarioo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;

public class IdGenerator {

	private static final Logger LOGGER = Logger.getLogger(IdGenerator.class);

	/**
	 * Generates an 8 character random id
	 */
	public static String generateRandomId() {
		UUID uuid = UUID.randomUUID();
		return generateIdUsingHash(uuid.toString());
	}

	/**
	 * Generates an 8 character id by creating a hash of the provided string
	 */
	public static String generateIdUsingHash(final String value) {
		MessageDigest converter;
		try {
			converter = MessageDigest.getInstance("SHA1");
			String id = new String(Hex.encodeHex(converter.digest(value.getBytes())));
			return id.substring(0, 8); // limit to first 8 characters, to keep URLs short and collision likelihood small
		} catch (final NoSuchAlgorithmException e) {
			LOGGER.info("Can't generate SHA1 hash code", e);
			throw new RuntimeException("Can't generate SHA1 hash code", e);
		}
	}

}
