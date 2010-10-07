package android.speech.tts.location;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TTSTester extends Activity{
	private static final String TAG = "BatmobileMain";

	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.splash);
		
	    Log.v(TAG, "Start up screen");
	    Button buttonEnter = (Button) findViewById(R.id.buttonEnter);
	    
		ComponentName comp = new ComponentName(getPackageName(),   LocationLoggerService.class.getName());
		ComponentName service = startService(new Intent().setComponent(comp));
	    
	    buttonEnter.setOnClickListener(new OnClickListener() {
	    	public void onClick(View v) {
	   Intent simpleTTS = new Intent(TTSTester.this, SimpleTTS.class);
	startActivity(simpleTTS);
	    	}
	    });
	 }
}