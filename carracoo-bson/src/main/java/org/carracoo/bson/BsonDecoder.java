package org.carracoo.bson;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey
 * Date: 5/30/13
 * Time: 3:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class BsonDecoder  {
	public Object decode(byte[] data) {
		BsonBuffer bson = new BsonBuffer();
		bson.writeBytes(data);
		bson.position(0);
		return bson.readDocument();
	}
}
