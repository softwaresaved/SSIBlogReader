package uk.software.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class BlogParser {
	private String htmlBlog = new String();
	
	public String parseHtml(String link){
		
		URL blogURL = null;
		try {
			blogURL = new URL(link);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			InputStream is = (InputStream) blogURL.getContent();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			StringBuffer sb = new StringBuffer();
			
			while((line = br.readLine()) != null){
				   sb.append(line);
				 }
				 htmlBlog = sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return htmlBlog;
		
	}

}
