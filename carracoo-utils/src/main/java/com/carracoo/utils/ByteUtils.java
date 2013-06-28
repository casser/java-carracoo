package com.carracoo.utils;

public class ByteUtils {
	static public class InvalidHexStringException extends Exception {
		private static final long serialVersionUID = -5528299467212367454L;

		public InvalidHexStringException(String hex) {
			super("Invalid Hex String <" + hex + ">");
		}
	}

	public static byte[] toBytesBE(long value) {
		return new byte[]{
				((byte) ((value >> 56) & 0xFF)),
				((byte) ((value >> 48) & 0xFF)),
				((byte) ((value >> 40) & 0xFF)),
				((byte) ((value >> 32) & 0xFF)),
				((byte) ((value >> 24) & 0xFF)),
				((byte) ((value >> 16) & 0xFF)),
				((byte) ((value >> 8) & 0xFF)),
				((byte) ((value >> 0) & 0xFF))
		};
	}

	public static boolean readBoolean(byte[] buffer, int index) {
		return (readByte(buffer, index) > 0);
	}

	public static void writeBoolean(byte[] buffer, int index, boolean value) {
		writeByte(buffer, index, (byte) (value ? 0x01 : 0x00));
	}

	public static void writeShort(byte[] buffer, int index, int value) {
		write(buffer, index + 0, (byte) ((value >> 0) & 0xFF));
		write(buffer, index + 1, (byte) ((value >> 8) & 0xFF));
	}

	public static void writeInt(byte[] buffer, int index, int value) {
		write(buffer, index + 0, (byte) ((value >> 0) & 0xFF));
		write(buffer, index + 1, (byte) ((value >> 8) & 0xFF));
		write(buffer, index + 2, (byte) ((value >> 16) & 0xFF));
		write(buffer, index + 3, (byte) ((value >> 24) & 0xFF));
	}

	public static void writeLong(byte[] buffer, int index, long value) {
		write(buffer, index + 0, (byte) ((value >> 0) & 0xFF));
		write(buffer, index + 1, (byte) ((value >> 8) & 0xFF));
		write(buffer, index + 2, (byte) ((value >> 16) & 0xFF));
		write(buffer, index + 3, (byte) ((value >> 24) & 0xFF));
		write(buffer, index + 4, (byte) ((value >> 32) & 0xFF));
		write(buffer, index + 5, (byte) ((value >> 40) & 0xFF));
		write(buffer, index + 6, (byte) ((value >> 48) & 0xFF));
		write(buffer, index + 7, (byte) ((value >> 56) & 0xFF));
	}

	public static int readShort(byte[] buffer, int index) {
		int i = (
			(buffer[index + 0] & 0xff) +
			((buffer[index + 1] & 0xff) << 8)
		);
		return i;
	}

	public static int readInt(byte[] buffer, int index) {
		int i = (
			(buffer[index + 0] & 0xff) +
			((buffer[index + 1] & 0xff) << 8) +
			((buffer[index + 2] & 0xff) << 16) +
			(buffer[index + 3] << 24)
		);
		return i;
	}

	public static long readLong(byte[] buffer, int index) {
		long l = (
			(buffer[index + 0] & 0xff) +
			(((buffer[index + 1] & 0xff)) << 8) +
			(((buffer[index + 2] & 0xff)) << 16) +
			(((buffer[index + 3] & 0xffL)) << 24) +
			(((buffer[index + 4] & 0xffL)) << 32) +
			(((buffer[index + 5] & 0xffL)) << 40) +
			(((buffer[index + 6] & 0xffL)) << 48) +
			(((long) buffer[index + 7]) << 56)
		);
		return l;
	}


	public static void writeDouble(byte[] buffer, int index, double value) {
		writeLong(buffer, index, Double.doubleToRawLongBits(value));
	}

	public static double readDouble(byte[] buffer, int index) {
		return Double.longBitsToDouble(readLong(buffer, index));
	}

	public static byte readByte(byte[] buffer, int index) {
		return buffer[index];
	}

	public static void writeByte(byte[] buffer, int index, byte value) {
		write(buffer, index, value);
	}

	public static void writeBytes(byte[] buffer, int index, byte[] value) {
		write(buffer, index, value);
	}

	public static byte[] readBytes(byte[] buffer, int index, int size) {
		byte[] b = new byte[size];
		readBytes(buffer, index, b);
		return b;
	}

	public static void readBytes(byte[] buffer, int index, byte[] value) {
		System.arraycopy(buffer, index, value, 0, value.length);
	}

	public static byte[] read(byte[] buffer, int sPos, int ePos) {
		return readBytes(buffer, sPos, ePos - sPos);
	}

	public static void write(byte[] buffer, int index, byte value) {
		buffer[index] = value;
	}

	public static void write(byte[] buffer, int index, byte[] value) {
		System.arraycopy(value, 0, buffer, index, value.length);
	}

	public static void printHexString(byte[] bytes) {
		System.out.println(getHexString(bytes));
	}

	public static String getHexString(byte[] bytes) {
		return getHexString(bytes, false);
	}

	public static String getHexString(byte[] bytes, Boolean ascii) {
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			int b = 0xff & bytes[i];
			if (ascii && (b > 32 && b < 127)) {
				buf.append(" " + ((char) b));
			} else {
				String s = Integer.toHexString(b);
				if (s.length() < 2)
					buf.append("0");
				buf.append(s);
			}
			buf.append(' ');
			if ((i + 1) % 16 == 0 && i != bytes.length - 1) {
				String index = Integer.toHexString(((i + 1) / 16) * 16);
				while (index.length() < 8) {
					index = "0" + index;
				}
				buf.append('\n');
				buf.append(index);
				buf.append(" | ");
			}
		}
		return
			"Offset(H)  00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F \n" +
			"---------|-------------------------------------------------\n" +
			"00000000 | " +
		buf.toString();
	}

	public static String toHex(byte[] bytes) {
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String s = Integer.toHexString(0xff & bytes[i]);
			if (s.length() < 2)
				buf.append("0");
			buf.append(s);
		}
		return buf.toString();
	}

	public static byte[] fromHex(String s) {
		if (1 == s.length() % 2) {
			throw new IllegalArgumentException("Invalid Hex [" + s + "]");
		}
		byte data[] = new byte[s.length() / 2];
		for (int i = 0; i < s.length(); i += 2) {
			data[i / 2] = (Integer.decode("0x" + s.charAt(i) + s.charAt(i + 1))).byteValue();
		}
		return data;
	}

	public static void printJavaCode(byte[] bytes) {
		System.out.println(getJavaCode(bytes));
	}

	public static String getJavaCode(byte[] bytes) {
		final StringBuilder buf = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			buf.append("(byte)0x");
			String s = Integer.toHexString(0xff & bytes[i]);
			if (s.length() < 2)
				buf.append("0");
			buf.append(s);
			buf.append(", ");
			if ((i + 1) % 16 == 0 && i != bytes.length - 1) {
				buf.append('\n');
			}
		}
		buf.delete(buf.length() - 2, buf.length());
		return "{\n" + buf.toString() + "\n};";
	}

	public static String toHexBE(long value) {
		return toHex(toBytesBE(value));
	}
}
