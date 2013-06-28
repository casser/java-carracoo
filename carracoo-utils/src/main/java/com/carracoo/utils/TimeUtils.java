package com.carracoo.utils;

public class TimeUtils {

	public static long max = 0;

	static long time = System.currentTimeMillis() - 0x0032aa8a6f58e2e0L;
	static long index = 0;

	public static synchronized byte[] getTimeBytes(byte stamp) {
		byte[] bytes = getTimeBytes();
		bytes[0] = stamp;
		return bytes;
	}

	public static synchronized byte[] getTimeBytes() {
		long time = getSuperNanoTime();
		byte[] bytes = new byte[]{
				((byte) ((time >> 56) & 0xFF)),
				((byte) ((time >> 48) & 0xFF)),
				((byte) ((time >> 40) & 0xFF)),
				((byte) ((time >> 32) & 0xFF)),
				((byte) ((time >> 24) & 0xFF)),
				((byte) ((time >> 16) & 0xFF)),
				((byte) ((time >> 8) & 0xFF)),
				((byte) ((time >> 0) & 0xFF))
		};
		return bytes;
	}

	public static synchronized long getSuperNanoTime(byte stamp) {
		long time = getSuperNanoTime();
		return (((long) stamp) << 56) | (time & 0x00ffffffffffffffL);
	}

	public static synchronized long getSuperNanoTime() {
		long nTime = System.nanoTime();
		if (nTime > time) {
			time = nTime;
			index = 0;
		} else if (nTime > time) {
			System.out.print("UPS");
		} else {
			index++;
		}
		if (index > max) {
			max = index;
		}

		return (time << 8) | (index);
	}
}
