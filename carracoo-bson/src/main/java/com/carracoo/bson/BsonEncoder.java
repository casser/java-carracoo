package com.carracoo.bson;


/**
 * Created with IntelliJ IDEA.
 * User: Sergey
 * Date: 5/30/13
 * Time: 3:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class BsonEncoder {
	
	
	public byte[] encode(Object value) {
		BsonBuffer out  = new BsonBuffer();
		out.writeDocument(value);
		return out.array();
	}
		
}
