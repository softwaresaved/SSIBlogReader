package uk.software.blogreader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import pl.polidea.view.ZoomView;

import uk.software.blogreader.R;
import uk.software.blogreader.image.ImageLoader;
import uk.software.parser.*;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class DetailFragment extends Fragment {
	
	private int fPos;
	RSSFeed fFeed;
	private static final String TAG = "MyActivity";
	//private static String htmlString;
	//private static StringBuilder sb1;
	
	public ImageLoader imageLoader;
	
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

		imageLoader = new ImageLoader(getActivity().getApplicationContext());
	
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.detail_fragment, container, false);
		
		// Initialise views
		TextView title = (TextView) view.findViewById(R.id.title);
		final ImageView iv = (ImageView) view.findViewById(R.id.iv); 
		desc = (WebView) view.findViewById(R.id.desc);

		// Enable the vertical fading edge (by default it is disabled)
		ScrollView sv = (ScrollView) view.findViewById(R.id.sv);
		sv.setVerticalFadingEdgeEnabled(true);

		//final int width = getActivity().getApplicationContext().getResources().getDisplayMetrics().widthPixels;
		//final int height = getActivity().getApplicationContext().getResources().getDisplayMetrics().heightPixels;
		
		// Set webview properties
		WebSettings ws = desc.getSettings();
		
		desc.setHorizontalScrollBarEnabled(true);
		ws.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		ws.setAppCacheMaxSize( 5 * 1024 * 1024 ); // 5MB
		ws.setAppCachePath( getActivity().getApplicationContext().getCacheDir().getAbsolutePath());
		ws.setAllowFileAccess(true);
		ws.setAppCacheEnabled(true);
		ws.setJavaScriptEnabled(true);
		ws.setCacheMode( WebSettings.LOAD_DEFAULT ); // load online by default
		ws.setLightTouchEnabled(false);
		ws.setPluginState(PluginState.ON);
		
		ws.setSupportZoom(true);
		ws.setBuiltInZoomControls(true);  //Zoom in/ out when pinched 
		ws.setDomStorageEnabled(true);
		
		// Set the views
		title.setText(fFeed.getItem(fPos).getTitle());
		
		//Extracting part of SSI blog html page through Jsoup in AsyncLoadLinkFeed thread
		
		//new AsyncLoadLinkFeed().execute(); //Uncomment this statement when using AsyncTask and calling in background
		
		Log.v(TAG, "Detailed Activity Image Link is:"+fFeed.getItem(fPos).getImage());
		
		imageLoader.DisplayImage(fFeed.getItem(fPos).getImage(), iv,400,300);
		
		iv.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loadPhoto(iv, 400, 400); //It will give the image id
			}
			
			
			
		} );
		
		
		//ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		
		//Method I which caches only few images (might be ones which are accessed when in online mode)
		//load Online
		/*if(connMgr.getActiveNetworkInfo() != null && connMgr.getActiveNetworkInfo().isConnected() == true)
		{
			ws.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
			desc.loadDataWithBaseURL("file:///android_asset/",fFeed.getItem(fPos).getDescription(), "text/html", "UTF-8", null );
		}*/
		
		
		//load in offline mode
		/*else{
			ws.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
			desc.loadDataWithBaseURL("file:///android_asset/",fFeed.getItem(fPos).getDescription(), "text/html", "UTF-8", null );
		
		}*/
		
		//Method II
		/*if(connMgr.getActiveNetworkInfo() == null || !connMgr.getActiveNetworkInfo().isConnected()){
		
			ws.setAppCachePath( getActivity().getApplicationContext().getCacheDir().getAbsolutePath());
			ws.setCacheMode(WebSettings.LOAD_DEFAULT);
			desc.loadDataWithBaseURL("file:///android_asset/",fFeed.getItem(fPos).getDescription(), "text/html", "UTF-8", null );	
		}
		else{
			
			ws.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
			desc.loadDataWithBaseURL("file:///android_asset/",fFeed.getItem(fPos).getDescription(), "text/html", "UTF-8", null );
		}*/
		
		//Method III
		if ( !isNetworkAvailable() ) { // loading offline
		    ws.setCacheMode( WebSettings.LOAD_CACHE_ELSE_NETWORK );
		}
		desc.loadDataWithBaseURL("file:///android_asset/",fFeed.getItem(fPos).getDescription(), "text/html", "UTF-8", null );
		
		return view;
	}
	
	private boolean isNetworkAvailable() {
		// TODO Auto-generated method stub
		ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService( Context.CONNECTIVITY_SERVICE );
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
		
	}
	
	private void loadPhoto(ImageView imageView,int width, int height){

		ImageView tempImageView = imageView;
		
		AlertDialog.Builder imageDialog = new AlertDialog.Builder(this.getActivity());
		
		// (That new View is just there to have something inside the dialog that can grow big enough to cover the whole screen.)
		/*Dialog d =imageDialog.setView(new View(this.getActivity())).create();
		
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(d.getWindow().getAttributes());
		lp.width = WindowManager.LayoutParams.MATCH_PARENT;
		lp.height = WindowManager.LayoutParams.MATCH_PARENT;
		d.show();
		d.getWindow().setAttributes(lp);*/
		
		LayoutInflater inflater = (LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View layout = inflater.inflate(R.layout.imagepop, (ViewGroup) this.getActivity().findViewById(R.id.layout_root));
		
		ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
		//image.setImageDrawable(tempImageView.getDrawable());
		imageLoader.DisplayImage(fFeed.getItem(fPos).getImage(),image,width,height);
		
		imageDialog.setView(layout);
		
		imageDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
			}
		});
		imageDialog.create();
		imageDialog.show();
		
	}
	
/*	
	private class AsyncLoadLinkFeed extends AsyncTask<Void, Void, Void>{

		
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
				Elements pngs = htmlDoc.select("img");
				
				sb1 = new StringBuilder();
				
				//Checking if CSS Style sheet created and included locally would work.
				sb1.append("<html>");
				sb1.append("<head>");
				sb1.append("<link rel=stylesheet href='css/SSIStyle.css'>");
				sb1.append("</head>");
				sb1.append("<body>");
				pngs.remove();
			    sb1.append(blogs.html().toString().replaceAll("&nbsp;", ""));
			    sb1.append("<font color=#999999>");
			    sb1.append(writer.text());
			    sb1.append("</font>");
			    sb1.append("</body></html>");
			    //Cross-checking if content is correct or not.
			    //Checking if htmlString has blog's html data
			    Log.v(TAG,"Blog is:"+htmlString);
					
			  
			return null;
		}
		
		
	}*/


	
}
