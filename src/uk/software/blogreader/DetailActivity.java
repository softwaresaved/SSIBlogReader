package uk.software.blogreader;

import com.google.analytics.tracking.android.Fields;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import uk.software.parser.*;
import uk.software.blogreader.R;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

public class DetailActivity extends FragmentActivity{
	
	RSSFeed feed;
	int pos;
	private DescAdapter adapter;
	private ViewPager pager;
	GoogleAnalyticsTracker tracker;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
			super.onBackPressed();

	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}

		
		Intent myIntent = new Intent(this,ListActivity.class);
		myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		//myIntent.putExtras(bundle);
	    startActivityForResult(myIntent,0);
	    
	    finish();
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail);

		// Get the feed object and the position from the Intent
		feed = (RSSFeed) getIntent().getExtras().get("feed");
		pos = getIntent().getExtras().getInt("pos");

		// Initialize the views
		adapter = new DescAdapter(getSupportFragmentManager());
		pager = (ViewPager) findViewById(R.id.pager);

		// Set Adapter to pager:
		pager.setAdapter(adapter);
		pager.setCurrentItem(pos);
		 
		ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

	  	// Show the Up button in the action bar.
	  	setupActionBar();
			
	    //Get a tracker (should auto-report) for sending a screen view
		//Tracker t = ((MyApplication) getApplication()).getTracker(MyApplication.TrackerName.APP_TRACKER);
		//t.send(new HitBuilders.AppViewBuilder().build());
	  	Tracker t = GoogleAnalytics.getInstance((MyApplication) getApplicationContext()).newTracker("UA-46208653-2");
	  	t.set(Fields.SCREEN_NAME, "DetailActivity");
	  	t.send(new HitBuilders.EventBuilder().setCategory("UA").setAction("click").setLabel(feed.getItem(pos).getLink()).build());
		
		//Send Statistics tracking
		// tracker = GoogleAnalyticsTracker.getInstance();     
	      
	     //tracker.start("UA-46208653-1", this); // Start the tracker in manual dispatch mode.
	     //tracker.start("UA-46208653-2", 30, this);   //Tracker started  with a default dispatch interval of 30 seconds.
	     //tracker.trackPageView("/SSI Blog Page");
	     
	}
	
	
	public class DescAdapter extends FragmentStatePagerAdapter {
		public DescAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return feed.getItemCount();
		}

		@Override
		public Fragment getItem(int position) {

			DetailFragment frag = new DetailFragment();

			Bundle bundle = new Bundle();
			bundle.putSerializable("feed", feed);
			bundle.putInt("pos", position);
			frag.setArguments(bundle);

			return frag;

		}

	}
	
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
		
			getActionBar().setDisplayHomeAsUpEnabled(true);
		
	}
	}
}
