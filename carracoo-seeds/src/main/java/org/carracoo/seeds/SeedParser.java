/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carracoo.seeds;

/**
 *
 * @author Sergey
 */
public interface SeedParser {
	public String format();
	Object decode(byte[] data);
	byte[] encode(Object value);
}
