package uk.software.blogreader;

import uk.software.blogreader.R;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

public class MainActivity extends Activity {
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			//NavUtils.navigateUpFromSameTask(this);
			switch(item.getItemId())
			{
			case android.R.id.home:
                   super.onBackPressed();
			}
		return true;
	}
		//Intent that will return to previous activity.
				Intent myIntent = new Intent(getApplicationContext(),MainActivity.class );
				myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivityForResult(myIntent, 0);
			   
			    return super.onOptionsItemSelected(item);
		
	}


	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}


	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent blogspage=new Intent(this,LoadingActivity.class);
    	startActivity(blogspage);
    	
    	ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
		
		// Show the Up button in the action bar.
		setupActionBar();
       
      
    }
	
    
    private void setupActionBar() {
		// TODO Auto-generated method stub
    	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		
	}
    }

}
