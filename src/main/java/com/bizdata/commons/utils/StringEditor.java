package com.bizdata.commons.utils;

import java.beans.PropertyEditorSupport;

import org.springframework.util.StringUtils;

public class StringEditor extends PropertyEditorSupport{


    /**
     * String html转义
     */
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (StringUtils.hasText(text)) {
            setValue(htmlEncode(text));
        }
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
