package com.carracoo.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class MD5 {

	public static byte[] bytes() {
		UUID ra = UUID.randomUUID();
		long lb = ra.getLeastSignificantBits();
		long mb = ra.getMostSignificantBits();
		return new byte[]{
			((byte) ((lb >> 0)  & 0xFF)),
			((byte) ((lb >> 8)  & 0xFF)),
			((byte) ((lb >> 16) & 0xFF)),
			((byte) ((lb >> 24) & 0xFF)),
			((byte) ((lb >> 32) & 0xFF)),
			((byte) ((lb >> 40) & 0xFF)),
			((byte) ((lb >> 48) & 0xFF)),
			((byte) ((lb >> 56) & 0xFF)),
			((byte) ((mb >> 0)  & 0xFF)),
			((byte) ((mb >> 8)  & 0xFF)),
			((byte) ((mb >> 16) & 0xFF)),
			((byte) ((mb >> 24) & 0xFF)),
			((byte) ((mb >> 32) & 0xFF)),
			((byte) ((mb >> 40) & 0xFF)),
			((byte) ((mb >> 48) & 0xFF)),
			((byte) ((mb >> 56) & 0xFF))
		};
	}

	public static byte[] bytes(String input) {
		return bytes(input.getBytes());
	}

	public static byte[] bytes(byte[] buffer) {
		return bytes(buffer, 0, buffer.length);
	}

	public static byte[] bytes(byte[] buffer, int offset, int length) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(buffer, offset, length);
			return md.digest();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static String hex() {
		return ByteUtils.toHex(bytes());
	}

	public static String hex(String buffer) {
		return ByteUtils.toHex(bytes(buffer.getBytes()));
	}

	public static String hex(byte[] buffer) {
		return ByteUtils.toHex(bytes(buffer));
	}

	public static String hex(byte[] buffer, int offset, int length) {
		return ByteUtils.toHex(bytes(buffer, offset, length));
	}

	public static boolean isValid(String input) {
		return input.toLowerCase().matches("^[a-f0-9]{32}$");
	}
}
