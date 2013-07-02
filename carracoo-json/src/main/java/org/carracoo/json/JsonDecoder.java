package org.carracoo.json;



import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey
 * Date: 5/30/13
 * Time: 3:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class JsonDecoder {
	private JsonLexer lexer;
	
	
	public Object decode(byte[] data) {
		return decode(new String(data));
	}
	
	public Object decode(String string) {
		lexer = new JsonLexer(string);
		return decodeValue();
	}

	public Object decode(InputStream stream) {
		lexer = new JsonLexer(stream);
		return decodeValue();
	}

	private Object decodeValue(){
		JsonLexer.Token token = lexer.next();
		switch (token.type){
			case NULL           : return null;
			case TRUE           :
			case FALSE          : return token.asBoolean();
			case STRING         : return token.asString();
			case NUMBER         : return token.asNumber();
			case OBJECT_START   : return decodeObject();
			case ARRAY_START    : return decodeArray();
			default             : throw new RuntimeException("invalid value token "+token);
		}
	}

	private Object decodeObject(){
		Map<String,Object> object     = new LinkedHashMap<String, Object>();
		JsonLexer.Token token   = lexer.next();
		switch (token.type){
			case OBJECT_END     : return object;
			case STRING         :
				boolean end = false;
				while (!end){
					String key          = token.asString();
					token               = lexer.next();
					if(!token.type.equals(JsonLexer.Token.Type.COLON)){
						throw new RuntimeException("invalid value token");
					}
					Object value   = decodeValue();
					object.put(key,value);
					token               = lexer.next();
					if(token.type.equals(JsonLexer.Token.Type.OBJECT_END)){
						end=true;
					}else
					if(token.type.equals(JsonLexer.Token.Type.COMMA)){
						token  = lexer.next();
					}else{
						throw new RuntimeException("invalid value token");
					}
				}
			break;
			default : throw new RuntimeException("invalid value token");
		}
		return object;
	}

	private Object decodeArray(){
		ArrayList<Object> array       = new ArrayList<Object>();
		JsonLexer.Token token   = lexer.current();
		switch (token.type){
			case ARRAY_START    :
				boolean end = false;
				while (!end){
					Object value   = decodeValue();
					array.add(value);
					token               = lexer.next();
					if(token.type.equals(JsonLexer.Token.Type.ARRAY_END)){
						end=true;
					}else
					if(!token.type.equals(JsonLexer.Token.Type.COMMA)){
						throw new RuntimeException("invalid value token");
					}
				}
			break;
			default             : throw new RuntimeException("invalid array token");
		}
		return array;
	}

	
}
