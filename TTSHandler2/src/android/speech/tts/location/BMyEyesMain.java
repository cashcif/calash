package android.speech.tts.location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class BMyEyesMain extends Activity{
	private static final String TAG = "BatmobileMain";
	
	private final int SPLASH_DISPLAY_LENGHT = 100;
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.splash);
	    Intent serviceIntent = new Intent();
		serviceIntent.setAction("android.speech.tts.location.LocationLogggerService");
		startService(serviceIntent);
		
	    
	    
	    new Handler().postDelayed(new Runnable(){
            public void run() {
	    Log.v(TAG, "Start up screen");
	    
            }
        }, SPLASH_DISPLAY_LENGHT);
	    
         Log.i(TAG, "Service started");

         Intent simpleTTS = new Intent(BMyEyesMain.this, SimpleTTS.class);
     	startActivity(simpleTTS);            
          
          Log.i(TAG, "Splash Activity Finished");

	

	  
	 }
}
