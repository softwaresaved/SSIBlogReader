package uk.software.blogreader;

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
