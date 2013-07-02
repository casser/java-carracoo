/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carracoo.json;

import org.carracoo.json.JsonDecoder;
import org.carracoo.json.JsonEncoder;
import org.carracoo.seeds.SeedParser;

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
