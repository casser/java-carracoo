/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carracoo.json;

import com.carracoo.json.JsonDecoder;
import com.carracoo.json.JsonEncoder;
import com.carracoo.seeds.SeedParser;

/**
 *
 * @author Sergey
 */
public class JsonParser implements SeedParser {
	
	@Override
	public String format() {
		return "json";
	}
	
	@Override
	public Object decode(byte[] data) {
		return new JsonDecoder().decode(data);
	}

	@Override
	public byte[] encode(Object value) {
		return new JsonEncoder().encode(value);
	}
	
}
