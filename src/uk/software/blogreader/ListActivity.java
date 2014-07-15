package uk.software.blogreader;

import uk.software.blogreader.R;
import uk.software.blogreader.image.*;
import uk.software.parser.*;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ListActivity extends Activity {
	Application myApp;
	RSSFeed feed;
	ListView lv;
	CustomListAdapter adapter;
	String Uri = null;
	private static final String TAG = "MyActivity";
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;

		}
		//Intent that will return to previous activity.
				Intent myIntent = new Intent(getApplicationContext(),MainActivity.class );
				myIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			    startActivityForResult(myIntent, 0);

				return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.feed_list);

		myApp = getApplication();

		// Get feed form the file
		feed = (RSSFeed) getIntent().getExtras().get("feed");

		// Initialize the variables:
		lv = (ListView) findViewById(R.id.listView);
		lv.setVerticalFadingEdgeEnabled(true);

		// Set an Adapter to the ListView
		adapter = new CustomListAdapter(this);
		lv.setAdapter(adapter);

		// Set on item click listener to the ListView
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// actions to be performed when a list item clicked
				int pos = arg2;

				Bundle bundle = new Bundle();
				bundle.putSerializable("feed", feed);
				Intent intent = new Intent(ListActivity.this,
						DetailActivity.class);
				intent.putExtras(bundle);
				intent.putExtra("pos", pos);
				startActivity(intent);

			}
		});
		//Show up button in the action bar.
				setupActionBar();

	}
	private void setupActionBar() {
		// TODO Auto-generated method stub
	  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			
			getActionBar().setDisplayHomeAsUpEnabled(true);
		
	}
}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		adapter.imageLoader.clearCache();
		adapter.notifyDataSetChanged();
	}

	class CustomListAdapter extends BaseAdapter {

		private LayoutInflater layoutInflater;
		public ImageLoader imageLoader;

		public CustomListAdapter(ListActivity activity) {

			layoutInflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			imageLoader = new ImageLoader(activity.getApplicationContext());
		}

		@Override
		public int getCount() {

			// Set the total list item count
			return feed.getItemCount();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			// Inflate the item layout and set the views
			View listItem = convertView;
			int pos = position;
			if (listItem == null) {
				listItem = layoutInflater.inflate(R.layout.list_item, null);
			}

			// Initialize the views in the layout
			ImageView iv = (ImageView) listItem.findViewById(R.id.thumb);
			// set the ImageView opacity to 50%
			TextView tvTitle = (TextView) listItem.findViewById(R.id.title);
			TextView tvDate = (TextView) listItem.findViewById(R.id.date);

			Log.v(TAG, "Image Link is:"+feed.getItem(pos).getImage());
			
			if (feed.getItem(pos).getVideo() != null)
			{
			  Uri = "http:"+feed.getItem(pos).getVideo();
			  Log.v(TAG,"Video link is:"+Uri);
			}
			else
				Uri = feed.getItem(pos).getVideo();
			
			// Set the views in the layout
			if( (feed.getItem(pos).getImage() == null) && (Uri != null)){
				
				//File sdcard = Environment.getExternalStorageDirectory();
	            //File file = new File(sdcard,"VID_20130922_130050.mp4");
	            //FileOutputStream out;
	            //MediaMetadataRetriever retriever = new MediaMetadataRetriever();
	            //Bitmap thumbnail = retriever.getFrameAtTime((int)(33*10),MediaMetadataRetriever.OPTION_CLOSEST);
			Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(feed.getItem(pos).getVideo(),MediaStore.Video.Thumbnails.MICRO_KIND);
		    Matrix matrix = new Matrix();
		   
			if(thumbnail != null){
				 //try{
			   Bitmap bitmap = Bitmap.createBitmap(thumbnail, 0, 0,
				            thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);
			   //ByteArrayOutputStream stream = new ByteArrayOutputStream();
			   //retriever.setDataSource(file.getAbsolutePath());
			 
               //thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
               //byte[] byteArray = stream.toByteArray();
               
               //out = new FileOutputStream(file.getPath());
             //  try {
				//out.write(byteArray);
		//	} catch (IOException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
		//	}
               iv.setImageBitmap(bitmap);
            		   //thumbnail.createScaledBitmap(thumbnail, 300, 300, false));
				// }
				// catch(FileNotFoundException e){
				//	 e.printStackTrace();
				 //}
			}
			 else{
				 imageLoader.DisplayImage(feed.getItem(pos).getImage(), iv);
			 }
			}
			else {
				
			 if( (feed.getItem(pos).getImage() != null) && (Uri == null))	
			    imageLoader.DisplayImage(feed.getItem(pos).getImage(), iv);
			}
			tvTitle.setText(feed.getItem(pos).getTitle());
			tvDate.setText(feed.getItem(pos).getDate().substring(4, 16));

			return listItem;
		}

	}


}
