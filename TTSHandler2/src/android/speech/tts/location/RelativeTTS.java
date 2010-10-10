package android.speech.tts.location;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class RelativeTTS extends Activity implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener, ITTS, SensorEventListener {
	
	private TextToSpeech tts;
	private HashMap<String, String> ttsmap;
	private AudioManager audioMan;
	private MediaPlayer mp;
	private double maxelevation;
	private double minelevation;
	private double maxdistance;
	private double pitchfactor;
	private int maxvolume;
	private int minvolume;
	private String destfile;
	double latitude;
	double longitude;
	double altitude;

	private ArrayList<POI> pois;
	private POI currentloc;
	
	private boolean finishedspeaking;
	private boolean checked;
	private boolean exp_checked;
	
	private SensorManager sensorManager;
	private SeekBar AzimuthBar;
	private SeekBar DistanceBar;
	private SeekBar ElevationBar;
	private SeekBar SpeedBar;
	private SeekBar VolumeBar;
	private TextView AzimuthText;
	private TextView DistanceText;
	private TextView ElevationText;
	private TextView SpeedText;
	private RadioButton SimpleRB;
	private RadioButton RelativeRB;
	private RadioButton AbsoluteRB;
	private Button SpeakButton;
	private CheckBox AutoCheck;
	private CheckBox ExpCheck;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
		LocationReceiver locationReceiver = new LocationReceiver();
		registerReceiver(locationReceiver, new IntentFilter(
				LocationLoggerService.BROADCAST_LOCATION_MEASUREMENTS));
	    
		tts = new TextToSpeech(this, this);
		mp = new MediaPlayer();
		destfile = Environment.getExternalStorageDirectory().toString() + "/test.wav";
		audioMan = (AudioManager) getSystemService(AUDIO_SERVICE);
		exp_checked = true;
		AzimuthBar = (SeekBar) findViewById(R.id.SeekBarAzimuth);
		DistanceBar = (SeekBar) findViewById(R.id.SeekBarDistance);
		ElevationBar = (SeekBar) findViewById(R.id.SeekBarElevation);
		SpeedBar = (SeekBar) findViewById(R.id.SeekBarSpeed);
		VolumeBar = (SeekBar) findViewById(R.id.SeekBarVolume);
		AzimuthText = (TextView) findViewById(R.id.TextViewAzimuth);
		DistanceText = (TextView) findViewById(R.id.TextViewDistance);
		ElevationText = (TextView) findViewById(R.id.TextViewElevation);
		SpeedText = (TextView) findViewById(R.id.TextViewSpeed);
		SimpleRB = (RadioButton) findViewById(R.id.RBSimple);
		RelativeRB = (RadioButton) findViewById(R.id.RBRelative);
		AbsoluteRB = (RadioButton) findViewById(R.id.RBAbsolute);
		AutoCheck = (CheckBox) findViewById (R.id.CheckAuto);
		ExpCheck = (CheckBox) findViewById (R.id.CheckExplicit);
		SpeakButton = (Button) findViewById(R.id.ButtonSpeak);
		ExpCheck.setEnabled(true);
		ExpCheck.setChecked(true);
		
    	pitchfactor = 2/(Math.PI);
		maxvolume = audioMan.getStreamMaxVolume(AudioManager.STREAM_MUSIC) - 2;
		minvolume = 2;
    	checked = false;
    	pois = new ArrayList<POI>();
    	pois.clear();
    	resetExtremes();
    	
    	finishedspeaking = true;
    	
    	ttsmap = new HashMap<String, String>();
    	ttsmap.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));
		ttsmap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "voice");
    	
		currentloc = new POI("Test Location", altitude,latitude,longitude); 
		//currentloc = new POI("Test Location",1852 , -26.1924094, 28.03104222); 
		POI point1 = new POI("Top of Stairs", 1802, -26.19246304, 28.03102076);
		POI point2 = new POI("Bottom of Stairs", 1801, -26.19242549, 28.03101003);
		POI point3 = new POI("Senate House", 1801, -26.19243622, 28.03095102);
		POI point4 = new POI("Boom", 1789, -26.1924094, 28.0314821);
		POI point5 = new POI("Chemistry Building Corner", 1789, -26.19237721, 28.03102612);
		POI point6 = new POI("Chemistry Building", 1789, -26.19253278, 28.03102612);
		POI point7 = new POI("Statue", 1789, -26.19251668, 28.03118169);
		POI point8 = new POI("Digital Arts or Nunary", 1777, -26.19243622, 28.03232431);
		POI point9 = new POI("Gate House", 1856, -26.19221677, 28.03201318);
		POI point10 = new POI("Wits Theatre", 1783, -26.19248986, 28.03156793);
		POI point11 = new POI("JCSE", 1870, -26.19281176, 28.03227791);
		POI point12 = new POI("Oppenheimer Sciences", 1783, -26.19179785, 28.03233504);
		pois = new ArrayList<POI>();
		pois.add(point1);
		pois.add(point2);
		pois.add(point3);
		pois.add(point4);
		pois.add(point5);
		pois.add(point6);
		pois.add(point7);
		pois.add(point8);
		pois.add(point9);
		pois.add(point10);
		pois.add(point11);
		pois.add(point12);
		
		// Real sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		AzimuthBar.setOnSeekBarChangeListener(SpatialSeekBarChangeListener);
		DistanceBar.setOnSeekBarChangeListener(SpatialSeekBarChangeListener);
		ElevationBar.setOnSeekBarChangeListener(SpatialSeekBarChangeListener);
		SpeedBar.setOnSeekBarChangeListener(SpeedSeekBarChangeListener);
		
		resetExtremes();
		
		SimpleRB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent simpleTTS = new Intent(RelativeTTS.this, SimpleTTS.class);
				startActivity(simpleTTS);
			}
		});
		 
		AbsoluteRB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent absoluteTTS = new Intent(RelativeTTS.this, AbsoluteTTS.class);
				startActivity(absoluteTTS);
			}
		});
		
		ExpCheck.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if (!isChecked)
		       	  	exp_checked = false;
		        if (isChecked)
		        	exp_checked = true;
		    }
		});
		
		SpeakButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if (SpeakButton.getText() == "Stop")
				{
				SpeakButton.setText("Speak");
			//	tts.stop();
				}
			else
			{
			SpeakButton.setText("Stop");
			announce();
			}
			}
		});
		
		AutoCheck.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		    {
		        if (!isChecked)
		        {
		        	checked = false;
		        	sensorManager.unregisterListener(RelativeTTS.this);
		        }
		        if (isChecked)
		        {
		        	checked = true;
		        	sensorManager.registerListener(RelativeTTS.this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
		    	//	sensorManager.registerListener(RelativeTTS.this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
		        }
		    }
		});
	}
    
	public void announce(){
		tts.stop();
		for(int i = 0; i < pois.size(); i++){
			while(!finishedspeaking);
			speak(pois.get(i), TextToSpeech.QUEUE_ADD);
		}
	}
	
    private void speak(POI poi, int flushQueue) {
    	float angle = (AzimuthBar.getProgress() + poi.bearingTo((Location) currentloc))%360;
    	if(angle <= 90 || angle >= 270){
    		setPitch(poi);
    		setVolume(poi);
    		setBalance(poi);
    		tts.setSpeechRate(((float) SpeedBar.getProgress())/5);
    		finishedspeaking=false;
			if(exp_checked)
				tts.synthesizeToFile(poi.getName() + convertDirec(angle) + fixDistance(poi.distanceTo(currentloc)+ DistanceBar.getProgress()) + fixElevation(poi.getAltitude()-currentloc.getAltitude()+ElevationBar.getProgress()), ttsmap, destfile);
				//tts.speak(poi.getName() + convertDirec(AzimuthBar.getProgress() + poi.bearingTo((Location) currentloc)) + fixDistance(poi.distanceTo(currentloc)+ DistanceBar.getProgress()) + fixElevation(poi.getAltitude()-currentloc.getAltitude()+ElevationBar.getProgress()), flushQueue, ttsmap);
			else
				tts.synthesizeToFile(poi.getName(), ttsmap, destfile);
				//tts.speak(poi.getName() + convertDirec(AzimuthBar.getProgress() + poi.bearingTo((Location) currentloc)), flushQueue, ttsmap);
			while(!finishedspeaking||mp.isPlaying());
    	}
    }
    
    private String fixDistance (float distance)
	{
		if (distance>=1000)
		{
			distance = Round(distance/1000, 2);
			return (Float.valueOf(distance) + "kilometres");
		}
		else
		{	
			distance = Math.round(distance);
			return (Float.valueOf(distance) + "metres");
		}
	}
    
	private String fixElevation (double elevation)
	{
		int elevation2 = (int)elevation;
		if (elevation<0)
		{
			elevation2 = (int)Math.abs(elevation);
			return (elevation2 + "degrees down");
		}
		else
			return (elevation2 + "degrees up");
	}
	
	private float Round(float Rval, int Rpl) {
		float p = (float)Math.pow(10,Rpl);
		Rval = Rval * p;
		float tmp = Math.round(Rval);
		return (float)tmp/p;
	}
	
	private void setPitch(POI poi){
		float num = poi.distanceTo(currentloc)+DistanceBar.getProgress();
		if (num==0)
			num = (float)0.1;
		double angle = Math.atan((poi.getAltitude()-currentloc.getAltitude()+ ElevationBar.getProgress())/num);
	//	double pitch = angle*pitchfactor + 1;
			tts.setPitch((float) (angle*pitchfactor + 1));
    }
	
	private void setVolume(POI poi){
		float cheese = poi.distanceTo(currentloc) + DistanceBar.getProgress();
		int tempVol = (int) (maxvolume*(1-(cheese/maxdistance)));
		if (tempVol<minvolume)
			audioMan.setStreamVolume(AudioManager.STREAM_MUSIC, minvolume, AudioManager.FLAG_VIBRATE);
		else
			audioMan.setStreamVolume(AudioManager.STREAM_MUSIC, tempVol, AudioManager.FLAG_VIBRATE);
	}
	
	private void setBalance(POI poi){
		float angle = (AzimuthBar.getProgress() + poi.bearingTo((Location) currentloc))%360;
		if(angle < 180 && angle > 0)
			mp.setVolume((float) ((90-angle)/90), 1);
		else
			mp.setVolume(1, (float) ((angle-270)/90));
	}
	
	private String convertDirec(float azimuth) {
		 int temp = (int) Math.floor((azimuth+15)/30);
		 temp = temp%12;
		 if (temp==0)
		 	temp =12;
		 if (azimuth < 15)
		    return "12 o clock";
		 return String.valueOf(temp) + " o clock";
	   };

	private void resetExtremes(){
		
			maxelevation = Double.MIN_VALUE;
			minelevation = Double.MAX_VALUE;
			maxdistance = Double.MIN_VALUE;
		
			for (POI i : pois){
				double temp = i.distanceTo(currentloc) + DistanceBar.getProgress();
				if (temp > maxdistance)
					maxdistance = temp;
				temp = Math.atan((i.getAltitude()-currentloc.getAltitude() + ElevationBar.getProgress())/(i.distanceTo(currentloc)+ DistanceBar.getProgress()));
				if(temp > maxelevation)
					maxelevation = temp;
				if(temp < minelevation)
					minelevation = temp;
			}
			pitchfactor = 1/(Math.max(maxelevation, Math.abs(minelevation)));
	}
	
	@Override
    public void onUtteranceCompleted(String arg0) {
		mp.reset();
		try {
			mp.setDataSource(destfile);
		} catch (IllegalArgumentException e) {
			
			e.printStackTrace();
		} catch (IllegalStateException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		try {
			mp.prepare();
		} catch (IllegalStateException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
        mp.start();
    	finishedspeaking = true;
    }
    
    public void onDestroy(){
    	if (tts != null){
    	tts.stop();
    	tts.shutdown();
    	}
    	if(mp != null)
    		mp.release();
    	super.onDestroy();
    }

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            	this.onDestroy();
        tts.setOnUtteranceCompletedListener(this);
        }
	}

	private SeekBar.OnSeekBarChangeListener SpatialSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			
		}
		
		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			resetExtremes();
			AzimuthText.setText("Azimuth: " + AzimuthBar.getProgress());
			DistanceText.setText("Distance: " + DistanceBar.getProgress());
			ElevationText.setText("Elevation: " + ElevationBar.getProgress());
		}
	};
	
	private SeekBar.OnSeekBarChangeListener SpeedSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
		
		@Override
		public void onStopTrackingTouch(SeekBar arg0) {
			
		}
		
		@Override
		public void onStartTrackingTouch(SeekBar arg0) {
			
		}
		
		@Override
		public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
			SpeedText.setText("Speech Rate: " + SpeedBar.getProgress());
		}
	};

@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	
	
}

	public void onSensorChanged(SensorEvent event) {
		//	if(event.sensor.getType() == Sensor.TYPE_ORIENTATION)
	//      {
			AzimuthBar.setProgress((int)event.values[0]);
			ElevationBar.setProgress((int)-event.values[1]);
	//      }
//		if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
//	      {
//			 long curTime = System.currentTimeMillis();
//			// only allow one update every 100ms, otherwise updates
//			// come way too fast and the phone gets bogged down
//			// with garbage collection
//			if (lastUpdate == -1 || (curTime - lastUpdate) > 100) {
//			lastUpdate = curTime;			
//			num = (int)event.values[0];
//			SpeedBar.setProgress((int)event.values[0]);
//			}
//			if (lastUpdate == -1 || (curTime - lastUpdate) > 2000 || num>currentSpeechRate)
//			{
//			currentSpeechRate = event.values[0];
//			}
//	      }
		}
		
				 @Override
	protected void onResume() {
		      super.onResume();
				RelativeRB.setChecked(true);
				RelativeRB.setEnabled(false);
				
				SpeedBar.setEnabled(true);
				ElevationBar.setEnabled(true);
				AzimuthBar.setEnabled(true);
				DistanceBar.setEnabled(true);
				VolumeBar.setEnabled(false);
				if(checked)
					sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
				
				AutoCheck.setChecked(checked);
				
			//	sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
				}
		    
		    @Override
	protected void onPause() {
	      super.onPause();
	      sensorManager.unregisterListener(this);
	    }
			public boolean onCreateOptionsMenu(Menu menu) {
				MenuInflater inflater = getMenuInflater();
				inflater.inflate(R.menu.main_menu, menu);
				return true;
			}

			@Override
			public boolean onOptionsItemSelected(MenuItem item) {
				// Handle item selection
				switch (item.getItemId()) {
				case R.id.help:
					AlertDialog.Builder builderHelp = new AlertDialog.Builder(this);
					builderHelp.setMessage(R.string.help).setCancelable(true).setTitle("Help")
							.setNeutralButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int id) {
											dialog.cancel();
										}
									});
					AlertDialog help = builderHelp.create();
					help.show();
					tts.speak(getText(R.string.help).toString(), TextToSpeech.QUEUE_FLUSH, ttsmap);
					return true;
					
				case R.id.about:
					AlertDialog.Builder builderAbout = new AlertDialog.Builder(this);
					builderAbout
							.setMessage(R.string.about)
							.setCancelable(true).setNeutralButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog,
												int id) {
											dialog.cancel();
										}
									});
					AlertDialog about = builderAbout.create();
					about.show();
					tts.speak(getText(R.string.about).toString(), TextToSpeech.QUEUE_FLUSH, ttsmap);
					return true;
				default:
					return super.onOptionsItemSelected(item);
				}
			}
			private class LocationReceiver extends BroadcastReceiver{


				@Override
				public void onReceive(Context context, Intent intent) {
					
					latitude = intent.getDoubleExtra("latitude", -26.1912118495368958);
					longitude = intent.getDoubleExtra("longitude", 28.027008175869915);
					altitude = intent.getDoubleExtra("altitude", 1781);
					currentloc.setLatitude(latitude);
					currentloc.setLongitude(longitude);
					currentloc.setAltitude(altitude);

				}
			
		}
}