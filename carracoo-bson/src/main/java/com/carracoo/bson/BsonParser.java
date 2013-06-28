/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carracoo.bson;

import com.carracoo.bson.BsonEncoder;
import com.carracoo.bson.BsonDecoder;
import com.carracoo.seeds.SeedParser;

/**
 *
 * @author Sergey
 */
public class BsonParser implements SeedParser {
	
	@Override
	public String format() {
		return "bson";
	}
	
	@Override
	public Object decode(byte[] data) {
		return new BsonDecoder().decode(data);
	}

	@Override
	public byte[] encode(Object value) {
		return new BsonEncoder().encode(value);
	}
	
}
