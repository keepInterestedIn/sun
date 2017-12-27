package com.bizdata.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.springframework.stereotype.Component;

@Component
public class KindeditorIgnoreHtml {
	public static <T> T unescapeHtml(T model,String ignore){
		Field[] field = model.getClass().getDeclaredFields(); // 获取实体类的所有属性，返回Field数组
        try {
            for (int j = 0; j < field.length; j++) { // 遍历所有属性
                String name = field[j].getName(); // 获取属性的名字
                if(!ignore.equals(name))
                {
                	name = name.substring(0, 1).toUpperCase() + name.substring(1); // 将属性的首字符大写，方便构造get，set方法
                    String type = field[j].getGenericType().toString(); // 获取属性的类型
                    if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                        Method m = model.getClass().getMethod("get" + name);
                        String value = (String) m.invoke(model); // 调用getter方法获取属性值
                        if (value !=null){
                        	value = htmlEncode(value);
                        	m = model.getClass().getMethod("set"+name,String.class);
                        	m.invoke(model, value);
                        }
                    }
                    // 如果有需要,可以仿照上面继续进行扩充,再增加对其它类型的判断
                }
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
		return model;
	}
	private static String htmlEncode(char c) {
	    switch(c) {
	       case '&':
	           return "&amp;";
	       case '<':
	           return "&lt;";
	       case '>':
	           return "&gt;";
	       case '"':
	           return "&quot;";
	       case ' ':
	           return "&nbsp;";
	       default:
	           return c + "";
	    }
	}
	private static String htmlEncode(String str) {
	    if (str ==null || str.trim().equals(""))   return str;
	    StringBuilder encodeStrBuilder = new StringBuilder();
	    for (int i = 0, len = str.length(); i < len; i++) {
	       encodeStrBuilder.append(htmlEncode(str.charAt(i)));
	    }
	    return encodeStrBuilder.toString();
	}
}
