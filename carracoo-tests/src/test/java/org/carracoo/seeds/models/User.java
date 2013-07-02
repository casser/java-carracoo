/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.carracoo.seeds.models;

import org.carracoo.seeds.SeedView;
import org.carracoo.seeds.lang.grains.DATE;
import org.carracoo.seeds.lang.grains.STRING;
import org.carracoo.seeds.lang.grains.DATE;
import org.carracoo.seeds.lang.grains.STRING;

import java.util.Date;

/**
 *
 * @author Sergey
 */
public class User extends Document {
	
	public final STRING<User> id			= new STRING<User>()	{
		
	};
	public final STRING<User>		name		= new STRING<User>()	{
		{required(true);multiple(false);}
	};
	public final STRING<User>		password	= new STRING<User>()	{
		@Override
		public User parent() {
			return User.this;
		}

		@Override
		public boolean available(SeedView view) {
			return view.is("bson");
		}
		
	};
	public final STRING<User>		email		= new STRING<User>()	{
		@Override
		public User parent() {
			return User.this;
		}
		@Override
		public boolean available(SeedView view) {
			return view.isRoot() || view.path().match("author.email");
		}
	};
	public final DATE<User> createdAt	= new DATE<User>()		{
		{set(new Date());}
		@Override
		public boolean available(SeedView view) {
			return view.isRoot() || view.path().match("author.email");
		}
	};
	public final DATE<User>			updatedAt	= new DATE<User>()		{
		{set(new Date());}
		@Override
		public boolean available(SeedView view) {
			return view.isRoot() || view.path().match("author.email");
		}
	};	
	public final STRING<User>		links		= new STRING<User>()	{
		{multiple(true);}
		@Override
		public User parent() {
			return User.this;
		}
		
		@Override
		public boolean available(SeedView view) {
			return view.isRoot();
		}
	};
	
	@Override
	public Object get(SeedView view) {
		if(view.path().match("Post:Comment:comments.#\\d+.Comment:User:author")){
			return id.get();
		}else{
			return super.get(view);
		}
	}

	@Override
	public User   set(SeedView view, Object value) {
		if(value instanceof String){
			id.set((String)value);return this;
		}else{
			return null;
		}
	}
	
}
