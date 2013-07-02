/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carracoo.seeds.models;

import org.carracoo.seeds.lang.grains.DATE;
import org.carracoo.seeds.lang.grains.GRAIN;
import org.carracoo.seeds.lang.grains.STRING;
import org.carracoo.seeds.lang.grains.GRAIN;

/**
 *
 * @author Sergey
 */
public class Comment extends Document {
	public final STRING	<Comment>		message		= new STRING <Comment>()		{
		@Override
		public Comment parent() {
			return Comment.this;
		}
	};
	public final DATE	<Comment>		createdAt	= new DATE	 <Comment>()		{
		@Override
		public Comment parent() {
			return Comment.this;
		}
	};
	public final DATE	<Comment>		updatedAt	= new DATE	 <Comment>()		{
		@Override
		public Comment parent() {
			return Comment.this;
		}
	};
	public final GRAIN<Comment,User> author		= new GRAIN	 <Comment,User>()	{
		@Override
		public Comment parent() {
			return Comment.this;
		}
	};
}
