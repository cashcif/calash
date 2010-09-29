package android.speech.tts.location;

import java.util.ArrayList;
import java.util.Locale;
import android.app.Activity;
import android.content.Intent;
import android.hardware.SensorEvent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.lang.Math;

public class RelativeTTS extends Activity implements TextToSpeech.OnInitListener, ITTS{
	
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
	private ArrayList<POI> pois;
	private ILocationHandler gps;
	private IDirectionHandler compass;
	private POI currentLocation;
	
	private SeekBar AzimuthBar;
	private SeekBar DistanceBar;
	private SeekBar ElevationBar;
	private SeekBar SpeedBar;
	private TextView AzimuthText;
	private TextView DistanceText;
	private TextView ElevationText;
	private TextView SpeedText;
	private RadioButton SimpleRB;
	private RadioButton RelativeRB;
	private RadioButton AbsoluteRB;
	private Button SpeakButton;
	
	public RelativeTTS(){
		TextToSpeech tts = new TextToSpeech(this, this);
    	AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
    	maxelevation = Double.MIN_VALUE;
    	minelevation = Double.MAX_VALUE;
    	maxdistance = Double.MIN_VALUE;
    	maxpitch = 100;
    	pitchfactor = 0.1;
    	maxvolume = 0;
    	maxspeechrate = 2;
    	speechratefactor = 0.1;	//TODO Initialise speechratefactor
    	
    	AzimuthBar = (SeekBar) findViewById(R.id.SeekBarAzimuth);
		DistanceBar = (SeekBar) findViewById(R.id.SeekBarDistance);
		ElevationBar = (SeekBar) findViewById(R.id.SeekBarElevation);
		SpeedBar = (SeekBar) findViewById(R.id.SeekBarSpeed);
		AzimuthText = (TextView) findViewById(R.id.TextViewAzimuth);
		DistanceText = (TextView) findViewById(R.id.TextViewDistance);
		ElevationText = (TextView) findViewById(R.id.TextViewElevation);
		SpeedText = (TextView) findViewById(R.id.TextViewSpeed);
		SimpleRB = (RadioButton) findViewById(R.id.RBSimple);
		RelativeRB = (RadioButton) findViewById(R.id.RBRelative);
		AbsoluteRB = (RadioButton) findViewById(R.id.RBAbsolute);
		SpeakButton = (Button) findViewById(R.id.ButtonSpeak);
		
		SimpleRB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SpeedBar.setEnabled(true);
				ElevationBar.setEnabled(false);
			}
		});
		 
		RelativeRB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				//Intent relativeTTS = new Intent(RelativeTTS.this, AbsoluteTTS.class);
				//startActivity(relativeTTS);
				SpeedBar.setEnabled(false);
				ElevationBar.setEnabled(true);
			}
		});
		 
		AbsoluteRB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				SpeedBar.setEnabled(false);
				ElevationBar.setEnabled(true);
			}
		});
		
		SpeakButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				speak(null, TextToSpeech.QUEUE_ADD);
			}
		});
		
		AzimuthBar.setOnSeekBarChangeListener(SpatialSeekBarChangeListener);
		DistanceBar.setOnSeekBarChangeListener(SpatialSeekBarChangeListener);
		ElevationBar.setOnSeekBarChangeListener(SpatialSeekBarChangeListener);
		SpeedBar.setOnSeekBarChangeListener(SpeedSeekBarChangeListener);
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
	
	private void resetExtremes(){
		/*if (pois.isEmpty()){
			maxelevation = Double.MIN_VALUE;
			minelevation = Double.MAX_VALUE;
			maxdistance = Double.MIN_VALUE;
		}
		else{
			for (POI i : pois){
				double temp = i.distanceTo(currentLocation);
				if (temp > maxdistance)
					maxdistance = temp;
				temp = Math.atan(i.getAltitude()/i.distanceTo(currentLocation));
				if(maxelevation < temp)
					maxelevation = temp;
				if(minelevation > temp)
					minelevation = temp;
			}
    	}*/
	}
	 
	private String convertDirec(float azimuth) {
		 int temp = (int) Math.floor((azimuth+15)/30);
		 if (azimuth < 15)
		    return "12 o clock";
		 return String.valueOf(temp) + " o clock";
	   }

	private SeekBar.OnSeekBarChangeListener SpatialSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			AzimuthText.setText("Azimuth: " + AzimuthBar.getProgress());
			DistanceText.setText("Distance: " + DistanceBar.getProgress());
			ElevationText.setText("Elevation: " + ElevationBar.getProgress());
		}
	};
	
private SeekBar.OnSeekBarChangeListener SpeedSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			// TODO Auto-generated method stub
		}
		
		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			SpeedText.setText("Speech Rate: " + SpeedBar.getProgress());
		}
	};
}