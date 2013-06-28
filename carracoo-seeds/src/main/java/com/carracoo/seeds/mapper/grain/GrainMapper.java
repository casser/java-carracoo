/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carracoo.seeds.mapper.grain;

import com.carracoo.seeds.SeedMapper;
import com.carracoo.seeds.SeedView;

/**
 *
 * @author Sergey
 */
public class GrainMapper implements SeedMapper {

	@Override
	public Object encode(SeedView view, Object object) {
		return new GrainEncoder().encode(view, object);
	}

	@Override
	public <T> T decode(SeedView view, Object value, Class<T> type) {
		return new GrainDecoder().decode(view, value, type);
	}
	
}
