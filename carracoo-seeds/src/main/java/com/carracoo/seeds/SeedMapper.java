/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carracoo.seeds;

/**
 *
 * @author Sergey
 */
public interface SeedMapper {
	public Object encode(SeedView view, Object object);
	public <T> T decode(SeedView view, Object value, Class<T> type);
}
