package org.carracoo.json;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey
 * Date: 5/31/13
 * Time: 12:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class JsonReader {

	public static final int DEFAULT_CHUNK = 256;

	private int          chunk;
	private int          position;
	private int          marker;
	private boolean      eof;
	private Reader       reader;
	private StringBuffer buffer;

	public JsonReader(InputStream  stream){
		this(stream,DEFAULT_CHUNK);
	}

	public JsonReader(String  string){
		this(string,DEFAULT_CHUNK);
	}

	public JsonReader(Reader reader){
		this(reader,DEFAULT_CHUNK);
	}

	public JsonReader(InputStream  stream,int chunk){
		this(new InputStreamReader(stream),chunk);
	}

	public JsonReader(String string, int chunk){
		this(new StringReader(string),chunk);
	}

	public JsonReader(Reader reader, int chunk){
		this.eof        = false;
		this.position   = 0;
		this.chunk      = chunk;
		this.buffer     = new StringBuffer(chunk);
		this.reader     = reader;
	}

	public void load(){
		if(eof){
			return;
		}
		if(position>=buffer.length()){
			for(int i=0;i<chunk;i++){
				try {
					int ch = reader.read();
					if(ch>0){
						buffer.append((char)ch);
					}else{
						eof = true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public int read() {
		load();
		if(position<buffer.length()){
			int ch = buffer.charAt(position);
			position++;
			return ch;
		}else{
			return -1;
		}
	}

	public int rewind(){
		return buffer.charAt(--position);
	}

	public void mark(){
		marker=position;
	}

	public void reset(){
		position=marker;
	}

	public int position(){
		return position;
	}

	public String substring(int from,int to){
		return buffer.substring(from, to);
	}
}
