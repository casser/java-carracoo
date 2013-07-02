package org.carracoo.utils;

import junit.framework.TestCase;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Sergey
 * Date: 6/29/13
 * Time: 7:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class PrinterTest extends TestCase {
	private static enum SimpleEnum{
		FIRST,SECOND,THIRD,FOURTH;
	}
	private Map<Object,Object> map = new LinkedHashMap<Object, Object>(){{
		put("number",1);
		put("boolean",true);
		put("date",new Date(1987,1,25));
		put("string",1);
		put("none",null);
		put("list",new ArrayList<Object>(){{
			add(new LinkedHashMap<Object,Object>(){{
				put("enum",SimpleEnum.FIRST);
				put("value","First");
			}});
			add(new LinkedHashMap<Object,Object>(){{
				put("enum",SimpleEnum.SECOND);
				put("value","Second");
			}});
			add(new LinkedHashMap<Object,Object>(){{
				put("enum",SimpleEnum.THIRD);
				put("value","Third");
			}});
			add(new LinkedHashMap<Object,Object>(){{
				put("enum",SimpleEnum.FOURTH);
				put("value","Fourth");
			}});
		}});
	}};

	public PrinterTest(){
		super();
	}

	public void testObjectPrinting(){
		Printer.print(map);
	}

}
