/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carracoo.test.seeds.models;

import com.carracoo.seeds.lang.grains.DATE;
import com.carracoo.seeds.lang.grains.GRAIN;
import com.carracoo.seeds.lang.grains.STRING;

/**
 *
 * @author Sergey
 */
public class Comment extends Document {
	public final STRING	<Comment>		message		= new STRING <Comment>()		{
		@Override
		public Comment parrent() {
			return Comment.this;
		}
	};
	public final DATE	<Comment>		createdAt	= new DATE	 <Comment>()		{
		@Override
		public Comment parrent() {
			return Comment.this;
		}
	};
	public final DATE	<Comment>		updatedAt	= new DATE	 <Comment>()		{
		@Override
		public Comment parrent() {
			return Comment.this;
		}
	};
	public final GRAIN	<Comment,User>	author		= new GRAIN	 <Comment,User>()	{
		@Override
		public Comment parrent() {
			return Comment.this;
		}
	};
}
