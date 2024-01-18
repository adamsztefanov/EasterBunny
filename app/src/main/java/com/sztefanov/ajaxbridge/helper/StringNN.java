package com.sztefanov.ajaxbridge.helper;

public class StringNN {

	public static String removeLast(String string, int n) {
		String result = "";
		if (null != string && !string.isEmpty()) {
			result = string.substring(0, string.length() - n);
		}
		return result;
	}

}
