package com.carracoo.json;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey
 * Date: 5/30/13
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonLexer {

	public static class Token{

		public static enum Type{
			OBJECT_START,
			OBJECT_END,
			ARRAY_START,
			ARRAY_END,
			COMMA,
			COLON,
			STRING,
			NUMBER,
			TRUE,
			FALSE,
			NULL,
			WS,
			COMMENT,
			EOF;
		}

		public Type         type;
		public JsonReader   buffer;
		public int          start;
		public int          end;

		public Token(Type type,JsonReader buffer,int start,int end){
			this.type   = type;
			this.buffer = buffer;
			this.start  = start;
			this.end    = end;
		}

		public static Token create(Type type,JsonReader buffer,int start,int end){
			return new Token(type,buffer,start,end);
		}

		public boolean isString(){
			return Type.STRING.equals(this.type);
		}

		public boolean isNumber(){
			return Type.NUMBER.equals(this.type);
		}

		public boolean isBoolean(){
			return Type.FALSE.equals(this.type) || Type.TRUE.equals(this.type);
		}
		public boolean isEof(){
			return Type.EOF.equals(this.type);
		}
		public boolean isNull(){
			return Type.NULL.equals(this.type);
		}

		public boolean isValue(){
			return isBoolean()||isNull()||isString()||isNumber();
		}

		public String value(){
			return buffer.substring(start,end);
		}

		public Number asNumber(){
			if(isNumber()){
				return new Double(value());
			}else{
				throw new RuntimeException("cant cast to boolean");
			}
		}

		public String asString(){
			if(isString()){
				return buffer.substring(start+1,end-1);
			}else{
				throw new RuntimeException("cant cast to boolean");
			}
		}

		public Boolean asBoolean(){
			if(isBoolean()){
				return Type.TRUE.equals(this.type);
			}else{
				throw new RuntimeException("cant cast to boolean");
			}
		}

		@Override
		public String toString() {
			return type.name()+"["+start+","+end+"] "+value()
				.replaceAll("\r","\\\\r")
				.replaceAll("\n","\\\\n")
				.replaceAll(" ","\\\\s")
				.replaceAll("\t","\\\\t")
			;
		}
	}


	private static class Location {

		public int position;
		public int line;
		public int index;

		public Location(){
			this(0,0,0);
		}

		public Location(int index, int line, int position){
			this.index      = index;
			this.line       = line;
			this.position   = position;
		}

		public int walk(int ch){
			index++;
			position++;
			if(ch=='\n'){
				position=0;
				line++;
			}
			return ch;
		}

		public Location mark(){
			return new Location(index,line,position);
		}

		@Override
		public String toString() {
			return index+":"+line+"-"+position;
		}
	}


	private JsonReader   reader;
	private Token        token;

	private int symbol;

	public JsonLexer(InputStream stream){
		this(new InputStreamReader(stream));
	}

	public JsonLexer(String json){
		this(new StringReader(json));
	}

	public JsonLexer(Reader reader){
		this.reader  = new JsonReader(reader);
	}

	public int rewind() {
		if(symbol>=0){
			return reader.rewind();
		}else{
			return symbol;
		}
	}

	public int read() {
		symbol = reader.read();
		return symbol;
	}

	public Token next() {
		return next(true);
	}
	public Token current() {
		return token;
	}
	public Token next(boolean skipWhitespace) {
		int from = reader.position();
		int ch = read();

		switch(ch){
			case '{'  :
				token = Token.create(Token.Type.OBJECT_START,reader,from,reader.position());
			break;
			case '}'  :
				token = Token.create(Token.Type.OBJECT_END,reader,from,reader.position());
			break;
			case '['  :
				token = Token.create(Token.Type.ARRAY_START,reader,from,reader.position());
			break;
			case ']'  :
				token = Token.create(Token.Type.ARRAY_END,reader,from,reader.position());
			break;
			case ':'  :
				token = Token.create(Token.Type.COLON,reader,from,reader.position());
			break;
			case ','  :
				token = Token.create(Token.Type.COMMA,reader,from,reader.position());
			break;
			default   :
				if(isWhitespaceStart(ch)){
					readWhitespace();
					token = Token.create(Token.Type.WS,reader,from,reader.position());
				}else
				if(isStringStart(ch)){
					readString();
					token = Token.create(Token.Type.STRING,reader,from,reader.position());
				}else
				if(isNullStart(ch)){
					readNull();
					token = Token.create(Token.Type.NULL,reader,from,reader.position());
				}else
				if(isTrueStart(ch)){
					readTrue();
					token = Token.create(Token.Type.TRUE,reader,from,reader.position());
				}else
				if(isFalseStart(ch)){
					readFalse();
					token = Token.create(Token.Type.FALSE,reader,from,reader.position());
				}else
				if(isNumberStart(ch)){
					readNumber();
					token =  Token.create(Token.Type.NUMBER,reader,from,reader.position());
				}else if(ch==-1){
					token = Token.create(Token.Type.EOF,reader,from,reader.position());
				}else{
					throw new RuntimeException("Invalid Json Data");
				}
		}
		while(skipWhitespace && Token.Type.WS.equals(token.type)){
			token = next();
		}
		return token;
	}

	private boolean isDigit(int ch){
		return (ch>='0' && ch<='9');
	}
	private boolean isHexDigit(int ch){
		return (ch>='0' && ch<='9') || (ch>='a' && ch<='f');
	}
	private boolean isWhitespaceStart(int ch){
		return ch==' ' || ch=='\t' || ch=='\n' || ch=='\r';
	}

	private boolean isFalseStart(int ch){
		return ch=='f';
	}
	private boolean isTrueStart(int ch){
		return ch=='t';
	}
	private boolean isNullStart(int ch){
		return ch=='n';
	}
	private boolean isStringStart(int ch){
		return ch=='"';
	}
	private boolean isNumberStart(int ch){
		return isDigit(ch) || ch=='-';
	}
	private void readFalse(){
		if(!(read()=='a' && read()=='l' && read()=='s'&& read()=='e')){
			throw new RuntimeException("Invalid true keyword");
		}
	}
	private void readTrue(){
		if(!(read()=='r' && read()=='u' && read()=='e')){
			throw new RuntimeException("Invalid true keyword");
		}
	}
	private void readNull(){
		if(!(read()=='u' && read()=='l' && read()=='l')){
			throw new RuntimeException("Invalid null keyword");
		}
	}
	private void readString(){
		int     ch      = -1;
		boolean c       = true;
		do{
			ch = read();
			if(isStringStart(ch)){
				c=false;
			}else
			if(ch=='\\'){
				ch = read();
				if(!(ch=='"'||ch=='\\'||ch=='/'||ch=='b'||ch=='f'||ch=='n'||ch=='r'||ch=='t'||ch=='u')){
					throw new RuntimeException("Invalid String");
				}else
				if(ch=='u'){
					for(int i=0;i<4;i++){
						if(!isHexDigit(read())){
							throw new RuntimeException("Invalid String");
						}
					}
				}
			}
		}while(c);
	}
	private void readWhitespace(){
		int     ch      = -1;
		boolean c       = true;
		do{
			ch = read();
			if(!isWhitespaceStart(ch)){
				c=false;
			}
		}while(c);
		rewind();
	}
	private void readNumber(){
		int     ch      = -1;
		boolean c       = true;
		boolean doted   = false;
		boolean exped   = false;
		if(symbol=='-'){
			if(!isDigit(read())){
				throw new RuntimeException("Invalid Number");
			}
		}
		do{
			ch = read();
			if(!isDigit(ch)){
				if(ch=='.'){
					if(doted){
						c=false;
					}else {
						doted=true;
					}
				} else
				if(ch=='e' || ch=='E'){
					if(exped){
						c=false;
					}else {
						ch = read();
						if(ch=='-'||ch=='+'){
							exped=true;
						}else{
							exped=true;
						}
					}
				} else {
					c=false;
				}
			}
		}while(c);
		rewind();
	}
}
