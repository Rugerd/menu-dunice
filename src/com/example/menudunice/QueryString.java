package com.example.menudunice;

import java.io.*;
import java.net.*;

public class QueryString {
	 private StringBuffer query;

	    public QueryString() {
	        query = new StringBuffer();
	    }

	    public synchronized QueryString add(Object name, Object value)
	                throws UnsupportedEncodingException {
	        if (!query.toString().trim().equals("")) query.append("&");
	        query.append(URLEncoder.encode(name.toString(), "UTF-8"));
	        query.append("=");
	        query.append(URLEncoder.encode(value.toString(), "UTF-8"));
	        return this;
	    }

	    public String toString() {
	        return query.toString();
	    }
}
