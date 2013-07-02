/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carracoo.bson;

import org.carracoo.bson.BsonEncoder;
import org.carracoo.bson.BsonDecoder;
import org.carracoo.seeds.SeedParser;

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
