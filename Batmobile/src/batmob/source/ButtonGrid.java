package batmob.source;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ButtonGrid extends Activity {
	private static final String TAG = "ButtonGrid";
	
	/** Called when the activity is first created. */
    
        @Override
    	public void onConfigurationChanged(Configuration newConfig) {
    		Log.w(TAG, "Override Function - Change Configuration");
    		super.onConfigurationChanged(newConfig);
    	}

    	@Override
    	public boolean onCreateOptionsMenu(Menu menu) {
    		MenuInflater inflater = getMenuInflater();
    		inflater.inflate(R.menu.main_menu, menu);
    		return true;
    	}
    	
        

    	@Override
    	public boolean onOptionsItemSelected(MenuItem item) {
    		// Handle item selection
    		switch (item.getItemId()) {
    		case R.id.help:
    			AlertDialog.Builder builderHelp = new AlertDialog.Builder(this);
    			builderHelp.setMessage(R.string.help_text).setCancelable(true).setTitle("Help")
    					.setNeutralButton("OK",
    							new DialogInterface.OnClickListener() {
    								public void onClick(DialogInterface dialog,
    										int id) {
    									dialog.cancel();
    								}
    							});
    			AlertDialog help = builderHelp.create();
    			help.show();
    			return true;
    		case R.id.about:
    			AlertDialog.Builder builderAbout = new AlertDialog.Builder(this);
    			builderAbout
    					.setMessage(R.string.about_text)
    					.setCancelable(true).setNeutralButton("OK",
    							new DialogInterface.OnClickListener() {
    								public void onClick(DialogInterface dialog,
    										int id) {
    									dialog.cancel();
    								}
    							});
    			AlertDialog about = builderAbout.create();
    			about.show();
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    		}
    	}

    	public void onCreate(Bundle savedInstanceState) {
    		super.onCreate(savedInstanceState);
    		    	        
    		Log.i(TAG, "Button Grid is Loaded");
    		setContentView(R.layout.foursquare);
    		
    		Button buttonPoint = (Button) findViewById(R.id.point);
    		buttonPoint.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				Log.v(TAG, "Locate POI");
    				
    	//			WikitudeARIntent intent = new WikitudeARIntent();
    				
    			}
    		});
    		Button buttonExit = (Button) findViewById(R.id.exit);
    		buttonExit.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				Log.v(TAG, "Exit Application");
    			}
    		});
    		Button buttonMute = (Button) findViewById(R.id.mute);
    		buttonMute.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				Log.v(TAG, "Mute Application");
    			}
    		});
    		Button buttonRepeat = (Button) findViewById(R.id.repeat);
    		buttonRepeat.setOnClickListener(new OnClickListener() {
    			public void onClick(View v) {
    				Log.v(TAG, "Repeat Last POI");
    			}
    		});

    }
}

