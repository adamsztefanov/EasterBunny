package com.sztefanov.ajaxbridge.helper;

public class Random {

	public static int randomRange(int a, int b) {
		int range = (b + 1) - a;
		int result = (int) ((Math.random() * range) + a);
		return result;
	}
}
