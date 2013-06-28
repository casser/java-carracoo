/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carracoo.test.seeds.parser;

import com.carracoo.bson.BsonDecoder;
import com.carracoo.bson.BsonEncoder;
import com.carracoo.utils.ByteUtils;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import junit.framework.TestCase;

/**
 *
 * @author Sergey
 */
public class BsonTest extends TestCase {
	
	
	private Map<String, Object> map;
	
	public BsonTest(String testName) {
		super(testName);
		map = new LinkedHashMap<String, Object>() {{
			put("string", "Hello World");
			put("integer", 524);
			put("double", 2.65);
			put("null", null);
			put("long", 123456789L);
			put("id", "1245ab461245ab461245ab46");
			put("list", new ArrayList<Object>() {{
				add(new LinkedHashMap<String, Object>() {{
					put("id", "1245ab461245ab461245ab46");
					put("name", "Martin Rodriges");
					put("email", "martin.r@gmail.com");
					put("posts", 16);
				}});
				add(new LinkedHashMap<String, Object>() {{
					put("id", "1245ab461245ab461245ab46");
					put("name", "Sergey Mamyan");
					put("email", "sergey.mamyan@gmail.com");
					put("posts", 57);
				}});
				add(new LinkedHashMap<String, Object>() {{
					put("id", "1245ab461245ab461245ab46");
					put("name", "Novel Bush");
					put("email", "novel.bush@ema.com");
					put("posts", 124);
				}});
			}});
		}};
	}
	
	public void testParsing() {
		BsonEncoder encoder = new BsonEncoder();
		BsonDecoder decoder = new BsonDecoder();
		byte[] bson		= encoder.encode(map);
		ByteUtils.printHexString(bson);
		Object decoded  = decoder.decode(bson);
		System.out.println(decoded);
	}
	
}
