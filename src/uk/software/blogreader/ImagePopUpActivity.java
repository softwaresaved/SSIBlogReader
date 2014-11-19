package uk.software.blogreader;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class ImagePopUpActivity extends Activity {
	
	private TextView tV;
	private ImageView image;

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
        setContentView(R.layout.imagepop);
       
        ImageView image = (ImageView) findViewById(R.id.fullimage);
        tV = (TextView)findViewById(R.id.imagepop_placename);
        
        
        
        tV.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				
			}
        	
        	
        });
       
      
    }
	

}
