package uk.software.blogreader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import uk.software.parser.DOMParser;
import uk.software.parser.RSSFeed;

import uk.software.blogreader.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoadingActivity extends Activity{
	private String RSSFEEDURL = "http://www.software.ac.uk/blog/rss-all";
	private int fPos;
	RSSFeed feed;
	String htmlString;
	String fileName;
	
	
	//Progress Bar Functionality
		private ProgressBar mProgress;
		private int mProgressStatus=0;
		private Handler mHandler = new Handler();
		private TextView textView;	
		
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		//Intent that will return to previous activity.
				Intent myIntent = new Intent(getApplicationContext(),MainActivity.class);
				myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(myIntent,0);
				return super.onOptionsItemSelected(item);
			}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_display_blogs_list);
		
        //Progress bar on Loading activity screen
		mProgress = (ProgressBar)findViewById(R.id.progress_bartop);
		textView = (TextView)findViewById(R.id.textview1);
		
		fileName = "SSIRSSFeed.blog";

		File feedFile = getBaseContext().getFileStreamPath(fileName);

		ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr.getActiveNetworkInfo() == null) {

			// No connectivity. Check if feed File exists
			if (!feedFile.exists()) {

				// No connectivity & Feed file doesn't exist: Show alert to exit
				// & check for connectivity
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(
						"Unable to reach server, \nPlease check your connectivity.")
						.setTitle("Software and Research")
						.setCancelable(false)
						.setPositiveButton("Exit",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int id) {
										finish();
									}
								});

				AlertDialog alert = builder.create();
				alert.show();
			} else {

				// No connectivty and file exists: Read feed from the File
				Toast toast = Toast.makeText(this,
						"No connectivity! Reading last update...",
						Toast.LENGTH_LONG);
				toast.show();
				feed = ReadFeed(fileName);
				startLisActivity(feed);
			}

		} else {

			// Connected - Start parsing
			new AsyncLoadXMLFeed().execute();

		}

	}

	private void startLisActivity(RSSFeed feed) {

		Bundle bundle = new Bundle();
		bundle.putSerializable("feed", feed);

		// launch List activity
		Intent intent = new Intent(LoadingActivity.this, ListActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);

		// kill this activity
		finish();

	}

	private class AsyncLoadXMLFeed extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			
			//Progress bar populating list view thread
			new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(mProgressStatus < 100){
						mProgressStatus += 1;
						
						//Update Progress Bar
						mHandler.post(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								//Obtain feed
							
								mProgress.setProgress(mProgressStatus);
								textView.setText(mProgressStatus + "%");
								
							}
							
							
						});
						try{
							Thread.sleep(1000);
						}
						catch(InterruptedException e){
							e.printStackTrace();
						}
					}
					
				}
				
			}).start();
			// Obtain feed
			DOMParser myParser = new DOMParser();
			feed = myParser.parseXml(RSSFEEDURL);
			htmlString = myParser.parseHtml(feed.getItem(fPos).getLink());
			
			if (feed != null && feed.getItemCount() > 0)
				WriteFeed(feed);
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);

			startLisActivity(feed);
		}

	}

	// Method to write the feed to the File
	private void WriteFeed(RSSFeed data) {

		FileOutputStream fOut = null;
		ObjectOutputStream osw = null;

		try {
			fOut = openFileOutput(fileName, MODE_PRIVATE);
			osw = new ObjectOutputStream(fOut);
			osw.writeObject(data);
			osw.flush();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				fOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	// Method to read the feed from the File
	private RSSFeed ReadFeed(String fName) {

		FileInputStream fIn = null;
		ObjectInputStream isr = null;

		RSSFeed _feed = null;
		File feedFile = getBaseContext().getFileStreamPath(fileName);
		if (!feedFile.exists())
			return null;

		try {
			fIn = openFileInput(fName);
			isr = new ObjectInputStream(fIn);

			_feed = (RSSFeed) isr.readObject();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

		finally {
			try {
				fIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return _feed;

	}

}
