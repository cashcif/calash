package android.speech.tts.location;

import java.util.Locale;
import android.app.Activity;
import android.hardware.SensorEvent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import java.lang.Math;

public class AbsoluteTTS extends Activity implements TextToSpeech.OnInitListener{
	
	private TextToSpeech tts = new TextToSpeech(this, this);
	private AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
	private double maxelevation = Double.MIN_VALUE; //TODO initialise maxaltitude
	private double minelevation = Double.MAX_VALUE; //TODO initialise minaltitude
	private double maxdistance = 1000;
	private float maxpitch = 100;
	private double pitchfactor = 0.1;
	private int maxvolume = 0;
	private float maxspeechrate = 2;
	private double speechratefactor = 0.1;
	private ILocationHandler gps;
	private IDirectionHandler compass;
	private IPOI currentLocation;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        currentLocation.setAltitude(gps.getCurrentLocation().getAltitude());
        currentLocation.setLatitude(gps.getCurrentLocation().getLatitude());
        currentLocation.setLongitude(gps.getCurrentLocation().getLongitude());
    }
    
    public void speak(IPOI poi, int flushQueue) throws InterruptedException{
    		setPitch(poi);
    		setVolume(poi);
    		setSpeechRate();
    		tts.speak(poi.getName() + convertDirec(poi.getBearing() - currentLocation.getBearing()), flushQueue, null);
    }
    
    public void onDestroy(){
    	if (tts != null){
    	tts.stop();
    	tts.shutdown();
    	}
    	super.onDestroy();
    }

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            	this.onDestroy();
        }
	}
	
	private void setPitch(IPOI poi){
		double angle = Math.atan(poi.getAltitude()/poi.distanceTo(currentLocation));
		if(angle >= 0)
			if( angle > maxelevation)
				tts.setPitch((float) (maxpitch*pitchfactor));
			else
				tts.setPitch((float) (angle*pitchfactor));
		else
			if( angle < minelevation)
				tts.setPitch((float) (minelevation*pitchfactor/maxelevation));
			else
				tts.setPitch((float) (angle*pitchfactor/maxelevation));
    }
	
	private void setVolume(IPOI poi){
		maxvolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (poi.distanceTo(currentLocation)/maxdistance*maxvolume), AudioManager.FLAG_VIBRATE);
	}
	
	private void setSpeechRate() throws InterruptedException{
		SensorEvent oldevent = compass.getCurrentDirection();
		wait(1000);
		float temp = (float) ((oldevent.values[0] - compass.getCurrentDirection().values[0])*speechratefactor);
		if(temp > maxspeechrate)
			temp = maxspeechrate;
		tts.setSpeechRate(temp);
	}
	 
	 private String convertDirec(float azimuth) {
		 int temp = (int) Math.floor((azimuth+15)/30);
		 if (azimuth < 15)
		    return "12 o clock";
		 return String.valueOf(temp) + " o clock";
	   }
}