package android.speech.tts.location;

import java.util.ArrayList;
import java.util.Locale;
import android.app.Activity;
import android.hardware.SensorEvent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import java.lang.Math;

public class AbsoluteTTS extends Activity implements TextToSpeech.OnInitListener, ITTS{
	
	private TextToSpeech tts;
	private AudioManager am;
	private double maxelevation;
	private double minelevation;
	private double maxdistance;
	private float maxpitch;
	private double pitchfactor;
	private int maxvolume;
	private float maxspeechrate;
	private double speechratefactor;
	private ILocationHandler gps;
	private IDirectionHandler compass;
	private POI currentLocation;
	private ArrayList<POI> pois;
	
	public AbsoluteTTS(){
		tts = new TextToSpeech(this, this);
    	am = (AudioManager) getSystemService(AUDIO_SERVICE);
    	maxelevation = Double.MIN_VALUE; //TODO initialise maxaltitude
    	minelevation = Double.MAX_VALUE; //TODO initialise minaltitude
    	maxdistance = 1000;
    	maxpitch = 100;
    	pitchfactor = 0.1;
    	maxvolume = 0;
    	maxspeechrate = 2;
    	speechratefactor = 0.1;
	}
	
    public void speak(POI poi, int flushQueue) {
    		setPitch(poi);
    		setVolume(poi);
    		try {
				setSpeechRate();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		//tts.speak(poi.getName() + convertDirec(poi.getBearing() - currentLocation.getBearing()), flushQueue, null);
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
	
	private void setPitch(POI poi){
		/*double angle = Math.atan(poi.getAltitude()/poi.distanceTo(currentLocation));
		if(angle >= 0)
			if( angle > maxelevation)
				tts.setPitch((float) (maxpitch*pitchfactor));
			else
				tts.setPitch((float) (angle*pitchfactor));
		else
			if( angle < minelevation)
				tts.setPitch((float) (minelevation*pitchfactor/maxelevation));
			else
				tts.setPitch((float) (angle*pitchfactor/maxelevation));*/
    }
	
	private void setVolume(POI poi){
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