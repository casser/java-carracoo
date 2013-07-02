/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carracoo.json;

/**
 *
 * @author Sergey
 */
public class JSON {
	public <T> T decode(byte[] data) {
		return (T) new JsonDecoder().decode(data);
	}
	public byte[] encode(Object value) {
		return new JsonEncoder().encode(value);
	}
}
