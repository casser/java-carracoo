/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carracoo.seeds.models;

import org.carracoo.seeds.lang.grains.GRAIN;
import org.carracoo.seeds.lang.grains.STRING;
import org.carracoo.seeds.lang.grains.GRAIN;
import org.carracoo.seeds.lang.grains.STRING;

/**
 *
 * @author Sergey
 */

public class Post extends Document {
	public final STRING<Post> id			= new STRING<Post>()		{
		
	};
	public final STRING	<Post>			title		= new STRING<Post>()		{
		
	};
	public final STRING	<Post>			content		= new STRING<Post>()		{
		
	};
	public final GRAIN<Post,User> author		= new GRAIN<Post,User>()	{
		
	};
	public final GRAIN	<Post,Comment>	comments	= new GRAIN<Post,Comment>(){
		{multiple(true);}
	};
}
