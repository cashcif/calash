package android.speech.tts.location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class BMyEyesMain extends Activity{
	private static final String TAG = "BatmobileMain";
		Button TestButton;
	//	Button DemoButton;
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    
	    setContentView(R.layout.splash);
	    
	    TestButton = (Button) findViewById(R.id.test);
	//    DemoButton = (Button) findViewById(R.id.demo);
		
	    Intent serviceIntent = new Intent();
		serviceIntent.setAction("android.speech.tts.location.LocationLogggerService");
		startService(serviceIntent);
		Log.i(TAG, "Service started");

		
		TestButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Intent simpleTTS = new Intent(BMyEyesMain.this, SimpleTTS.class);
		     	startActivity(simpleTTS);
			}
			});
	    
//		DemoButton.setOnClickListener(new View.OnClickListener(){
//			@Override
//			public void onClick(View arg0) {
//				Intent simpleTTS = new Intent(BMyEyesMain.this, SimpleTTS.class);
//		     	startActivity(simpleTTS);
//			}
//			});
	    
         
                     
          
          

	

	  
	 }
}
