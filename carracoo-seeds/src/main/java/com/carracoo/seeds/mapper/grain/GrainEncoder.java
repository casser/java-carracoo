package com.carracoo.seeds.mapper.grain;

import com.carracoo.seeds.SeedView;
import com.carracoo.seeds.lang.Corn;
import com.carracoo.seeds.lang.Grain;
import com.carracoo.utils.ObjectUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * Created with IntelliJ IDEA.
 * User: Sergey
 * Date: 5/30/13
 * Time: 3:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class GrainEncoder {
	
	public Object encode(SeedView view, Object model) {
		return encodeValue(view, model);
	}
	
	private Object encodeValue(SeedView view, Object model) {
		if(model==null){
			return null;
		}
		ObjectUtils.ObjectInfo info = ObjectUtils.info(model);
		if(info.isMap()){
			return encodeMap(view, model);
		}else
		if(info.isArray()){
			return encodeArray(view, model);
		}else
		if(info.isSimple()){
			return model;
		}else
		if(model instanceof Corn){
			return encodeModel(view, (Corn)model);
		}else{
			return model.getClass().getSimpleName()+"(\""+model.toString()+"\")";
		}
	}
	
	private Object encodeMap(SeedView view, Object model) {
		Map<Object,Object> map = (Map<Object,Object>) model;
		Map<Object,Object> res = new LinkedHashMap<Object, Object>(map.size());
		for(Map.Entry<Object,Object> entry:map.entrySet()){
			String key = entry.getKey().toString();
			Object val = entry.getValue();
			view.enter(key);
			res.put(key,encodeValue(view, val));
			view.exit();
		}
		return res;
	}
	
	private Object encodeArray(SeedView view, Object model) {
		Iterable<Object> lst = ObjectUtils.iterable(model);
		List<Object>	 res = new ArrayList<Object>();
		int index = 0;
		for(Object item:lst){
			view.enter(index++);
			res.add(encodeValue(view, item));
			view.exit();
		}
		return res;
	}
	
	private Object encodeModel(SeedView view, Corn model) {
		HashMap object = new HashMap();
		Object converted  = model.get(view);
		if(!model.equals(converted)){
			return encodeValue(view,converted);
		}
		for(Grain property : model.properties()){
			view.enter(property);
			if(property.available(view)){
				if(property.multiple()){
					object.put(property.name(), encodeValue(view, property.all()));
				}else{
					object.put(property.name(), encodeValue(view, property.get(view)));
				}
				
			}
			view.exit();
		}
		return object;
	}
}
