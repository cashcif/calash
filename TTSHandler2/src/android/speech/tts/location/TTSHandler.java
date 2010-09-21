package android.speech.tts.location;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import android.app.Activity;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;

public class TTSHandler extends Activity implements TextToSpeech.OnInitListener{
	
	private TextToSpeech tts = new TextToSpeech(this, this);
	private AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
	private double maxaltitude = Double.MIN_VALUE;
	private double minaltitude = Double.MAX_VALUE;
	private double maxdistance = Double.MIN_VALUE;
	private float maxpitch = 100;
	private int maxvolume = 0;
	private float maxspeechrate = 2;
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
    
    public void speak(IPOI poi, int flushQueue){
    	setPitch(poi);
    	setVolume(poi);
    	setSpeechRate();
    	tts.speak(poi.getName(), flushQueue, null);
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
    	if (gps.getCurrentLocation().getAltitude() < poi.getAltitude())
    		tts.setPitch((float) (poi.getAltitude()/maxaltitude*maxpitch));
    	else
    		tts.setPitch((float) (poi.getAltitude()/maxaltitude));
    }
	
	private void setVolume(IPOI poi){
		maxvolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (poi.distanceTo(currentLocation)/maxdistance*maxvolume), AudioManager.FLAG_VIBRATE);
	}
	
	private void setSpeechRate() throws InterruptedException{
		SensorEvent oldevent = compass.getCurrentDirection();
		wait(500);
		SensorEvent newevent = compass.getCurrentDirection();
		if(oldevent.values[0] < newevent.values[0] + 1)
			
		if (oldevent.values[0] > newevent.values[0] + 1)
			
		tts.setSpeechRate(1);
	}
	
	private void resetExtremes(){
		if (pois.isEmpty()){
			maxaltitude = Double.MIN_VALUE;
			minaltitude = Double.MAX_VALUE;
			maxdistance = Double.MIN_VALUE;
		}
		else{
			for (IPOI i : pois){
				double temp = i.getAltitude();
				if (maxaltitude < temp)
					maxaltitude = temp;
				if (minaltitude > temp)
					minaltitude = temp;
				temp = i.distanceTo(currentLocation);
				if (temp > maxdistance)
					maxdistance = temp;
			}
    	}
	}
	 
	 private IPOI getPOI(UUID id){
		 for (IPOI i : pois)
			 if (i.getId() == id)
				 return i;
		 return null;
	 }
}