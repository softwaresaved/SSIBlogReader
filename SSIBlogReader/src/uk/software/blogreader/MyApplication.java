package uk.software.blogreader;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

public class MyApplication extends Application{
	
	//Logging TAG
    private static final String TAG = "MyApp";
	
    //Apps PROPERTY_ID
	private static final String PROPERTY_ID = "UA-46208653-1";
	
	public static int GENERAL_TRACKER =0;
	

	//Creating Trackers for collecting Google Analytics
	 /**
	   * Enum used to identify the tracker that needs to be used for tracking.
	   *
	   * A single tracker is usually enough for most purposes. In case you do need multiple trackers,
	   * storing them all in Application object helps ensure that they are created only once per
	   * application instance.
	   */
	  public enum TrackerName {
	    APP_TRACKER, // Tracker used only in this app.
	    GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg: roll-up tracking.
	    ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a company.
	  }

	  HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
	  
	  public MyApplication(){
		  
		  super();
	  }
	  
	  synchronized Tracker getTracker(TrackerName trackerId) {
		    if (!mTrackers.containsKey(trackerId)) {

		      GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
		      Tracker t = (trackerId == TrackerName.APP_TRACKER) ? analytics.newTracker(PROPERTY_ID)
		          : (trackerId == TrackerName.GLOBAL_TRACKER) ? analytics.newTracker(R.xml.global_tracker)
		              : analytics.newTracker(R.xml.ecommerce_tracker);
		      mTrackers.put(trackerId, t);

		    }
		    return mTrackers.get(trackerId); //Parameter in this function call can be just trackerId or specific TrackerName based on the analytics we want to collect.
		  }
	

}
