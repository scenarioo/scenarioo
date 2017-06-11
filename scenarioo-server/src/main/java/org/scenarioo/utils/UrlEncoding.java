package org.scenarioo.utils;

import org.scenarioo.api.util.files.FilesUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class UrlEncoding {//TODO REMOVE
	public static String decode(final String string) {
		return FilesUtil.decodeName(string);
	}

	public static String encode(final String value) {
		return FilesUtil.encodeName(value);
	}
}
