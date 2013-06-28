package com.carracoo.seeds;

import com.carracoo.seeds.mapper.grain.GrainMapper;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey
 * Date: 5/30/13
 * Time: 3:22 AM
 * To change this template use File | Settings | File Templates.
 */
public class Seeds {
	
	private static class SeedParsers extends ConcurrentHashMap<String, SeedParser>{}
	
	private final SeedParsers PARSERS = new SeedParsers();
	
	private final SeedMapper mapper;
	
	public Seeds(){
		this(new GrainMapper());
	}
	
	public Seeds(SeedMapper mapper){
		this.mapper	= mapper;
	}
	
	public final Seeds parser(SeedParser parser){
		String view = parser.format();
		if(PARSERS.containsKey(view)){
			if(!PARSERS.get(view).getClass().equals(parser.getClass())){
				throw new RuntimeException("format with key <"+view+"> already exist");
			}
		}
		PARSERS.put(view, parser);
		return this;
	}
	
	public SeedParser parser(String view){
		return PARSERS.get(view);
	}
	
	public SeedParser parser(SeedView view){
		return parser(view.name());
	}
	
	public Object encodeMap(SeedView view, Object object) {
		return mapper.encode(view,object);
	}
	
	public <T> T decodeMap(SeedView view, Object value, Class<T> type) {
		return (T) mapper.decode(view,value,type);
	}
	
	public byte[] encode(SeedView view, Object object){
		return parser(view).encode(encodeMap(view,object));
	}
	
	public <T> T decode(SeedView view, byte[] bytes, Class<T> type){
		return decodeMap(view,parser(view).decode(bytes),type);
	}
}
