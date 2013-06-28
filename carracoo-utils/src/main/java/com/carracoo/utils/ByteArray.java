package com.carracoo.utils;


public class ByteArray {

	private static final int CHUNK_SIZE = 16;

	private byte[] buffer;
	private int index;
	private int length;

	public int position() {
		return index;
	}

	public void position(int value) {
		if (value < 0 || value > capacity()) {
			throw new ArrayIndexOutOfBoundsException("Can't Go Backward");
		} else {
			index = value;
		}
	}

	public int length() {
		return length;
	}

	public int capacity() {
		return buffer.length;
	}

	public byte[] buffer() {
		return buffer;
	}

	public byte[] array() {
		byte[] array = new byte[length()];
		System.arraycopy(buffer, 0, array, 0, array.length);
		return array;
	}

	public ByteArray() {
		this(CHUNK_SIZE);
	}

	public ByteArray(int capasity) {
		this.length = 0;
		this.index = 0;
		buffer = new byte[capasity];
	}

	public boolean readBoolean() {
		return (readByte() > 0);
	}

	public void writeBoolean(boolean value) {
		writeByte((byte) (value ? 0x01 : 0x00));
	}

	public int readInt() {
		int i = (
			(buffer[index + 0] & 0xff) +
			((buffer[index + 1] & 0xff) << 8) +
			((buffer[index + 2] & 0xff) << 16) +
			(buffer[index + 3] << 24)
		);
		forward(4);
		return i;
	}

	public void writeInt(int value) {
		expand(4);
		write(index + 0, (byte) ((value >> 0) & 0xFF));
		write(index + 1, (byte) ((value >> 8) & 0xFF));
		write(index + 2, (byte) ((value >> 16) & 0xFF));
		write(index + 3, (byte) ((value >> 24) & 0xFF));
		forward(4);
	}

	public long readLong() {
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
		forward(8);
		return l;
	}

	public void writeLong(long value) {
		expand(8);
		write(index + 0, (byte) ((value >> 0) & 0xFF));
		write(index + 1, (byte) ((value >> 8) & 0xFF));
		write(index + 2, (byte) ((value >> 16) & 0xFF));
		write(index + 3, (byte) ((value >> 24) & 0xFF));
		write(index + 4, (byte) ((value >> 32) & 0xFF));
		write(index + 5, (byte) ((value >> 40) & 0xFF));
		write(index + 6, (byte) ((value >> 48) & 0xFF));
		write(index + 7, (byte) ((value >> 56) & 0xFF));
		forward(8);
	}

	public void writeDouble(double value) {
		writeLong(Double.doubleToRawLongBits(value));
	}

	public double readDouble() {
		return Double.longBitsToDouble(readLong());
	}

	public byte readByte() {
		byte b = buffer[index];
		forward();
		return b;
	}

	public void writeByte(byte value) {
		expand(1);
		write(index, value);
		forward(1);
	}

	public void writeBytes(byte[] value) {
		expand(value.length);
		write(index, value);
		forward(value.length);
	}

	public byte[] readBytes(int size) {
		byte[] b = new byte[size];
		readBytes(b);
		return b;
	}

	public void readBytes(byte[] value) {
		System.arraycopy(buffer, index, value, 0, value.length);
		forward(value.length);
	}

	public void write(int index, byte value) {
		buffer[index] = value;
	}

	public void write(int index, byte[] value) {
		try {
			System.arraycopy(value, 0, buffer, index, value.length);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void expandTo(int index) {
		if (index > buffer.length) {
			int nLength = buffer.length * 2;
			while (nLength < index) {
				nLength *= 2;
			}
			byte[] nb = new byte[nLength];
			System.arraycopy(buffer, 0, nb, 0, buffer.length);
			buffer = nb;
		}
		if (index > this.length) {
			this.length = index;
		}
	}

	public void expand(int size) {
		expandTo(index + size);
	}

	public void forward(int step) {
		index += step;
	}

	public void forward() {
		forward(1);
	}

	public void backward(int step) {
		if (index - step >= 0) {
			index -= step;
		} else {
			throw new ArrayIndexOutOfBoundsException("Can't Go Backward");
		}
	}

	public void backward() {
		backward(1);
	}

	public void print() {
		System.out.println(
			ByteUtils.getHexString(this.buffer) + "\n" +
			"-----------------------------------------------------------\n" +
			"I:" + position() + " L:" + length() + " C:" + capacity() +  " MD5:" + md5()
		);
	}

	public String md5(){
		return MD5.hex(array());
	}

}
