/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carracoo.utils;

import java.io.*;

/**
 * @author Sergey
 */
public class IO {

	public static String readInputStream(InputStream in) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(in);
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int result = bis.read();
		while (result != -1) {
			byte b = (byte) result;
			buf.write(b);
			result = bis.read();
		}
		return buf.toString();
	}

	public static void writeFile(InputStream stream, String path) {
		try {
			OutputStream out = null;
			int read = 0;
			byte[] bytes = new byte[1024];
			out = new FileOutputStream(new File(path));
			while ((read = stream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
