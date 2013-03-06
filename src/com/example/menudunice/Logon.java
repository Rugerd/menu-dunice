package com.example.menudunice;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


public class Logon {
	
	private static String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
	{
	    StringBuilder result = new StringBuilder();
	    boolean first = true;

	    for (NameValuePair pair : params)
	    {
	        if (first)
	            first = false;
	        else
	            result.append("&");

	        result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
	        result.append("=");
	        result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
	    }

	    return result.toString();
	}
	
	public static String getSessionId(String login, String password, String token) throws IOException {
		URL myUrl = new URL("http://dunice.ru:7000/login/");
		URLConnection urlConn = myUrl.openConnection();
		urlConn.setDoOutput(true);
		urlConn.setDoInput(true);
		urlConn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		urlConn.addRequestProperty("Cookie", "csrftoken=" + token);
		urlConn.addRequestProperty("X-CSRFToken", token);
		urlConn.addRequestProperty("X-Requested-With", "XMLHttpRequest");
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("csrfmiddlewaretoken",
                token));
		nameValuePairs.add(new BasicNameValuePair("login",
                login));
		nameValuePairs.add(new BasicNameValuePair("password",
                password));
	
		
		OutputStream os = urlConn.getOutputStream();
		BufferedWriter writer = new BufferedWriter(
		        new OutputStreamWriter(os, "UTF-8"));
		writer.write(getQuery(nameValuePairs));
		writer.close();
		os.close();

		urlConn.connect();
		
		String headerName=null;
		String cookie, cookieName, cookieValue;
		for (int i=1; (headerName = urlConn.getHeaderFieldKey(i))!=null; i++) {
		    if (headerName.equals("Set-Cookie")) {                  
		    	cookie = urlConn.getHeaderField(i); 
		    	cookie = cookie.substring(0, cookie.indexOf(";"));
		        cookieName = cookie.substring(0, cookie.indexOf("="));
		        cookieValue = cookie.substring(cookie.indexOf("=") + 1, cookie.length());
		        if (cookieName.equals("sessionid")) {
		        	return cookieValue;
		        }
		    }
		}
		return "";
	}
	
	
	public static String doLogin() throws IOException {
		String cookie, cookieName, cookieValue;
		URL myUrl = new URL("http://dunice.ru:7000/");
		URLConnection urlConn = myUrl.openConnection();
		urlConn.connect();
		String headerName=null;
		for (int i=1; (headerName = urlConn.getHeaderFieldKey(i))!=null; i++) {
		    if (headerName.equals("Set-Cookie")) {                  
		    	cookie = urlConn.getHeaderField(i); 
		    	cookie = cookie.substring(0, cookie.indexOf(";"));
		        cookieName = cookie.substring(0, cookie.indexOf("="));
		        cookieValue = cookie.substring(cookie.indexOf("=") + 1, cookie.length());
		        if (cookieName.equals("csrftoken")) {
		        	return cookieValue;// getSessionId(login, password, cookieValue);
		        }
		    }
		}
		return "";
	}
	
	public static void setOrder(ArrayList<LVItems> dataSelected, String id, String token) throws IOException{
			//String token = Logon.doLogin();
			//String id = Logon.getSessionId(login, password, token);
	    	URL myUrl = new URL("http://dunice.ru:7000/menu/confirm/");
			URLConnection urlConn = myUrl.openConnection();
			urlConn.setDoOutput(true);
			urlConn.setDoInput(true);
			urlConn.addRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			urlConn.addRequestProperty("Cookie", "sessionid=" + id + "; csrftoken=" + token);// + token);
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("csrfmiddlewaretoken", token));//csrftoken.getCsrftoken()));
			
			for(LVItems d: dataSelected){
				nameValuePairs.add(new BasicNameValuePair(d.name, "1"));
			}
			
			nameValuePairs.add(new BasicNameValuePair("confirm",
	                "Confirmation"));
			
			OutputStream os = urlConn.getOutputStream();
			BufferedWriter writer = new BufferedWriter(
			        new OutputStreamWriter(os, "UTF-8"));
			writer.write(getQuery(nameValuePairs));
			writer.close();
			os.close();
			
			urlConn.connect();
			urlConn.getContent();
	}
}
