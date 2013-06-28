/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.carracoo.test.seeds;

import com.carracoo.seeds.SeedView;
import com.carracoo.seeds.Seeds;
import com.carracoo.seeds.mapper.grain.GrainDecoder;
import com.carracoo.seeds.mapper.grain.GrainEncoder;
import com.carracoo.test.seeds.models.Comment;
import com.carracoo.test.seeds.models.Post;
import com.carracoo.test.seeds.models.Simple;
import com.carracoo.test.seeds.models.User;
import com.carracoo.bson.BsonParser;
import com.carracoo.json.JsonParser;
import junit.framework.TestCase;

/**
 *
 * @author Sergey
 */
public class SeedsTest extends TestCase {
	private Seeds seeds;
	
	private Simple nested;
	private Simple simple;
	
	private User sergey;
	private User grisha;
	private User manila;
	
	private Post post;
	
	public SeedsTest(String testName) {
		super(testName);
		seeds	= new Seeds()	{{
			parser(new JsonParser());
			parser(new BsonParser());
		}};
		sergey	= new User()	{{
			name		.set("Sergey Mamyan");
			password	.set("mypassword");
			email		.set("Sergey.Mamyan@gmail.com");
			links		.set("Hello");
			links		.set("World");
		}}.commit();
		grisha	= new User()	{{
			name		.set("Grisha Valuk");
			password	.set("mypassword");
			email		.set("Grisha.Valuk@gmail.com");
			links		.set("Hello");
			links		.set("World");
		}}.commit();
		manila	= new User()	{{
			name		.set("Manila Gruchak");
			password	.set("mypassword");
			email		.set("manila.gruchak@gmail.com");
			links		.set("Hello");
			links		.set("World");
		}}.commit();
		post	= new Post()	{{
			author		.set(sergey);
			title		.set("Post Title");
			content		.set("Post Content");
			comments	.set(new Comment(){{
				author.set(manila);
				message.set("Hello All");
			}});
			comments	.set(new Comment(){{
				author.set(grisha);
				message.set("Hello Manila");
			}});
			comments	.set(new Comment(){{
				author.set(sergey);
				message.set("Hello Gyus");
			}});
		}}.commit();
				
		nested	= new Simple()	{{
			p1.set("N1");
			p3.set("N3-1");
			p3.set("N3-2");
		}}.commit();
		simple	= new Simple()	{{
			p1.set("P1");
			p2.set(nested);
			p3.set("P3-1");
			p3.set("P3-2");
			p3.set("P3-3");
		}}.commit();
		
		
		
	}
	
	private void print(Object value){
		System.out.println(value==null?"NULL":value.toString());
	}
	
	public void _testMapping() {		
		System.out.println(post);
		GrainEncoder encoder = new GrainEncoder();
		GrainDecoder decoder = new GrainDecoder();
		Object encoded = encoder.encode(new SeedView("json"),post);
		print(encoded);
		Post[]  decoded = decoder.decode(new SeedView("json"),encoded,Post[].class);
		print(decoded[0]);
	}
	
	public void testParsing() {
		byte[] userBytes = seeds.encode(new SeedView("json"),sergey);
		print(new String(userBytes));
		byte[] postBytes = seeds.encode(new SeedView("json"),post);
		print(new String(postBytes));
		Post decoded =  seeds.decode(new SeedView("json"), postBytes, Post.class);
		System.out.println(decoded);
	}
}
