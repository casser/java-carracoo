/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carracoo.bson;

/**
 *
 * @author Sergey
 */
public class BSON {
	
	public Object decode(byte[] data) {
		return new BsonDecoder().decode(data);
	}

	public byte[] encode(Object value) {
		return new BsonEncoder().encode(value);
	}
}
