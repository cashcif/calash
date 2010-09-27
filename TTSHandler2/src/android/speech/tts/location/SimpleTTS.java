package android.speech.tts.location;

import java.util.Locale;
import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

public class SimpleTTS extends Activity implements TextToSpeech.OnInitListener{
	private TextToSpeech tts;
	private ILocationHandler gps;
	private IPOI currentLocation;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        tts = new TextToSpeech(this, this);
        currentLocation.setAltitude(gps.getCurrentLocation().getAltitude());
        currentLocation.setLatitude(gps.getCurrentLocation().getLatitude());
        currentLocation.setLongitude(gps.getCurrentLocation().getLongitude());
    }
    
    public void speak(IPOI poi, int flushQueue){
    		tts.speak(poi.getName() + " is " + convertDirec(poi.getBearing() - currentLocation.getBearing()) + " " + poi.distanceTo(currentLocation) + " meters.", flushQueue, null);
    }
    
    public void onDestroy(){
    	if (tts != null){
    	tts.stop();
    	tts.shutdown();
    	}
    	super.onDestroy();
    }

	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            	this.onDestroy();
        }
	}
	 
	 private String convertDirec(float azimuth) {
		 int temp = (int) Math.floor((azimuth+15)/30);
		 if (azimuth < 15)
		    return "12 o clock";
		 return String.valueOf(temp) + " o clock";
	   }
}
