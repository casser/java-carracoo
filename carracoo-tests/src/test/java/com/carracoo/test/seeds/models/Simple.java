/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carracoo.test.seeds.models;

import com.carracoo.seeds.lang.Corn;
import com.carracoo.seeds.lang.grains.GRAIN;

/**
 *
 * @author Sergey
 */
public class Simple extends Corn {
	public final GRAIN<Simple,String>		p1	= new GRAIN<Simple,String>(){
		
	};
	public final GRAIN<Simple,Simple>		p2	= new GRAIN<Simple,Simple>(){
		
	};
	public final GRAIN<Simple,String>		p3	= new GRAIN<Simple,String>(){
		
	};
}
