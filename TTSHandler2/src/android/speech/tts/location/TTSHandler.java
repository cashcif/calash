package android.speech.tts.location;

import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import android.app.Activity;
import android.location.Location;
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
	private ArrayList<POI> pois;
	private LocationHandler gps;
	private POI currentLocation;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        currentLocation.setAltitude(gps.getCurrentLocation().getAltitude());
        currentLocation.setLatitude(gps.getCurrentLocation().getLatitude());
        currentLocation.setLongitude(gps.getCurrentLocation().getLongitude());
    }
    
    public void addPOIs(ArrayList<POI> newpoi){
    	for (POI i : newpoi)
    		pois.add(i);
    	resetExtremes();
    }
    
    public void deletePOIs(ArrayList<POI> oldpoi){
    	for (POI i : oldpoi)
    		for(POI j : pois)
    			if(i == j)
    				pois.remove(j);
    	resetExtremes();
    }
    
    public void clearPOIs(){
    	pois.clear();
    }
    
    public void speak(POI poi, int flushQueue){
    	setPitch(poi);
    	setVolume(poi);
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
	
	private void setPitch(POI poi){
    	if (gps.getCurrentLocation().getAltitude() < poi.getAltitude())
    		tts.setPitch((float) (poi.getAltitude()/maxaltitude*maxpitch));
    	else
    		tts.setPitch((float) (poi.getAltitude()/maxaltitude));
    }
	
	private void setVolume(POI poi){
		maxvolume = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		am.setStreamVolume(AudioManager.STREAM_MUSIC, (int) (poi.distanceTo(currentLocation)/maxdistance*maxvolume), AudioManager.FLAG_VIBRATE);
	}
	
	private void resetExtremes(){
		if (pois.isEmpty()){
			maxaltitude = Double.MIN_VALUE;
			minaltitude = Double.MAX_VALUE;
			maxdistance = Double.MIN_VALUE;
		}
		else{
			for (POI i : pois){
				if (maxaltitude < i.getAltitude())
					maxaltitude = i.getAltitude();
				if (minaltitude > i.getAltitude())
					minaltitude = i.getAltitude();
				if (i.distanceTo(currentLocation) > maxdistance)
					maxdistance = i.distanceTo(currentLocation);
			}
    	}
	}
	 
	 private POI getPOI(UUID id){
		 for (POI i : pois)
			 if (i.getId() == id)
				 return i;
		 return null;
	 }
}