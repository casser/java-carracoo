package org.carracoo.utils;

import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey
 * Date: 6/29/13
 * Time: 4:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class SYS {

	public static Map<String,String> envs(Pattern pattern){
		Map<String,String> env = System.getenv();
		Map<String,String> res = new LinkedHashMap<String, String>();
		for (String key:env.keySet()){
			if(pattern.matcher(key).matches()){
				res.put(key,env.get(key));
			}
		}
		return res;
	}

	public static Map<String,String> envs(String prefix){
		Map<String,String> env = System.getenv();
		Map<String,String> res = new LinkedHashMap<String, String>();
		for (String key:env.keySet()){
			if(key.startsWith(prefix)){
				res.put(key,env.get(key));
			}
		}
		return res;
	}

	public static Map<String,Object> props(Pattern pattern){
		Properties props = System.getProperties();
		Map<String,Object> res = new LinkedHashMap<String, Object>();
		for (Object k:props.keySet()){
			if(k!=null){
				String key = k.toString();
				if(pattern.matcher(key).matches()){
					res.put(key,props.get(key));
				}
			}
		}
		return res;
	}

	public static Map<String,Object> props(String prefix){
		Properties props = System.getProperties();
		Map<String,Object> res = new LinkedHashMap<String, Object>();
		for (Object k:props.keySet()){
			if(k!=null){
				String key = k.toString();
				if(key.startsWith(prefix)){
					res.put(key,props.get(key));
				}
			}
		}
		return res;
	}

	public static void print(Object obj) {

	}

	public static void print(Object obj,PrintStream out) {

	}

}
