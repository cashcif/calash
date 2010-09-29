package android.speech.tts.location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RadioButton;

public class TTSTester extends Activity{
	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
		
		Intent simpleTTS = new Intent(this, SimpleTTS.class);
		startActivity(simpleTTS);
	 }
}
