package com.bizdata.commons.utils;

import org.springframework.stereotype.Component;

@Component
public class StringEscapeHtml {
	
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
	public static String htmlEncode(String str) {
	    if (str ==null || str.trim().equals(""))   return str;
	    StringBuilder encodeStrBuilder = new StringBuilder();
	    for (int i = 0, len = str.length(); i < len; i++) {
	       encodeStrBuilder.append(htmlEncode(str.charAt(i)));
	    }
	    return encodeStrBuilder.toString();
	}
}
