package android.speech.tts.location;

import java.util.ArrayList;
import java.util.Locale;
import android.app.Activity;
import android.hardware.SensorEvent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import java.lang.Math;

public class RelativeTTS extends Activity implements TextToSpeech.OnInitListener{
	
	private TextToSpeech tts = new TextToSpeech(this, this);
	private AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
	private double maxelevation = Double.MIN_VALUE;
	private double minelevation = Double.MAX_VALUE;
	private double maxdistance = Double.MIN_VALUE;
	private float maxpitch = 100;
	private double pitchfactor = 0.1;
	private int maxvolume = 0;
	private float maxspeechrate = 2;
	private double speechratefactor = 0.1;	//TODO Initialise speechratefactor
	private ArrayList<IPOI> pois;
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
    
    public void addPOIs(ArrayList<IPOI> newpoi){
    	for (IPOI i : newpoi)
    		pois.add(i);
    	resetExtremes();
    }
    
    public void deletePOIs(ArrayList<IPOI> oldpoi){
    	for (IPOI i : oldpoi)
    		for(IPOI j : pois)
    			if(i == j)
    				pois.remove(j);
    	resetExtremes();
    }
    
    public void clearPOIs(){
    	pois.clear();
    	resetExtremes();
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
	
	private void resetExtremes(){
		if (pois.isEmpty()){
			maxelevation = Double.MIN_VALUE;
			minelevation = Double.MAX_VALUE;
			maxdistance = Double.MIN_VALUE;
		}
		else{
			for (IPOI i : pois){
				double temp = i.distanceTo(currentLocation);
				if (temp > maxdistance)
					maxdistance = temp;
				temp = Math.atan(i.getAltitude()/i.distanceTo(currentLocation));
				if(maxelevation < temp)
					maxelevation = temp;
				if(minelevation > temp)
					minelevation = temp;
			}
    	}
	}
	 
	private String convertDirec(float azimuth) {
		 int temp = (int) Math.floor((azimuth+15)/30);
		 if (azimuth < 15)
		    return "12 o clock";
		 return String.valueOf(temp) + " o clock";
	   }
}