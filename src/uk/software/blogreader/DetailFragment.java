package uk.software.blogreader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import uk.software.blogreader.R;
import uk.software.parser.*;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.widget.ScrollView;
import android.widget.TextView;

public class DetailFragment extends Fragment {
	private int fPos;
	RSSFeed fFeed;
	private static final String TAG = "MyActivity";
	private static String htmlString;
	private static StringBuilder sb1;
	URL blogURL;
	InputStream is;
	BufferedReader br;
	String line = null;
	StringBuffer sb;
	WebView desc;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		fFeed = (RSSFeed) getArguments().getSerializable("feed");
		fPos = getArguments().getInt("pos");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.detail_fragment, container, false);

		// Initialise views
		TextView title = (TextView) view.findViewById(R.id.title);
		desc = (WebView) view.findViewById(R.id.desc);

		// Enable the vertical fading edge (by default it is disabled)
		ScrollView sv = (ScrollView) view.findViewById(R.id.sv);
		sv.setVerticalFadingEdgeEnabled(true);

		// Set webview properties
		WebSettings ws = desc.getSettings();
		ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		ws.setLightTouchEnabled(false);
		ws.setPluginState(PluginState.ON);
		ws.setJavaScriptEnabled(true);

		// Set the views
		title.setText(fFeed.getItem(fPos).getTitle());
		
		//Extracting part of SSI blog html page through Jsoup in AsyncLoadLinkFeed thread
		
		new AsyncLoadLinkFeed().execute();
	
		//desc.loadDataWithBaseURL("file:///android_asset/",htmlString, "text/html", "UTF-8", null);
        //http://www.androidcentral.com/
		//fFeed.getItem(fPos).getDescription()
		
		return view;
	}
	
	private class AsyncLoadLinkFeed extends AsyncTask<Void, Void, Void>{

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Log.v(TAG,"Blog is (in Main Thread again):"+htmlString);
			//desc.loadData(sb1.toString(), "text/html", "UTF-8");
			desc.loadDataWithBaseURL("file:///android_asset/",sb1.toString(), "text/html", "UTF-8", null);
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
                BlogParser myParser = new BlogParser();
				htmlString = myParser.parseHtml(fFeed.getItem(fPos).getLink());
				
				org.jsoup.nodes.Document htmlDoc = Jsoup.parse(htmlString);
			
				Element blogs = htmlDoc.select("div[class=content]").first();
				
				Elements writer = htmlDoc.body().getElementsByAttributeValue("class", "submitted");
				//htmlDoc.getElementsByTag("div.content");
				
				sb1 = new StringBuilder();
				
				//Checking if CSS Style sheet created and included locally would work.
				sb1.append("<html>");
				sb1.append("<head>");
				sb1.append("<link rel=stylesheet href='css/SSIStyle.css'>");
				sb1.append("</head>");
				sb1.append("<body>");
			    //sb1.append("<html><head><link href=\"file:///android_asset/css/SSIStyle.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>");
			    //sb1.append("<p><img src=fFeed.getItem(fPos).getImage()</p>");
			    sb1.append(blogs.html());
			    sb1.append("<font color=#999999>");
			    sb1.append(writer.text());
			    sb1.append("</font>");
			    sb1.append("</body></html>");
			    //Cross-checking if content is correct or not.
			    //Checking if htmlString has blogs html data*/
			    Log.v(TAG,"Blog is:"+htmlString);
					
			   // Log.v(TAG,"Blog content is:"+blogs.toString());
			    
			return null;
		}
		
		
	}

}
