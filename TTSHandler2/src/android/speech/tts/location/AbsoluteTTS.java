package android.speech.tts.location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
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

public class AbsoluteTTS extends Activity implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener, ITTS, SensorEventListener {
	
	private TextToSpeech tts;
	private AudioManager audioMan;
	private double maxdistance;
	private double pitchfactor;
	private int maxvolume;
	private int minvolume;
	private float maxspeechrate;
	private float minspeechrate;
	private boolean checked;

	private ArrayList<POI> pois;
	private POI currentloc;
	private boolean finishedspeaking;
	private HashMap<String, String> ttsmap;
	private SoundPool sp;
	
	private SensorManager sensorManager;
	private float[] compassPoints;
	private float currentSpeechRate;
	private long lastUpdate;
	private CheckBox ExpCheck;
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
	private boolean exp_checked;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
		tts = new TextToSpeech(this, this);
		audioMan = (AudioManager) getSystemService(AUDIO_SERVICE);
    	pitchfactor = 2/(Math.PI);
		maxvolume = audioMan.getStreamMaxVolume(AudioManager.STREAM_MUSIC) - 2;
		minvolume = 1;
    	minspeechrate = 1;
    	maxspeechrate = 2;
    	
    	maxdistance = 1000;
    	
    	
    	pois = new ArrayList<POI>();
    	
    	finishedspeaking = true;
    	exp_checked = true;
    	ttsmap = new HashMap<String, String>();
    	ttsmap.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));
		ttsmap.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "voice");
    	
//    	POI point1 = new POI("Chamber of Mines", 52, -26.1909, 29);
//		POI point2 = new POI("Convergence Lab", 52, -26.1909, 28.0278);
//		POI point3 = new POI("Matrix", 52, -26.1909, 26.0278);
//		POI point4 = new POI("Tower of Light", 52, -26.1909, 28.278);
    	POI point1 = new POI("Chamber of Mines", 1200, -26.1913, 28.00278);
		POI point2 = new POI("Convergence Lab", 23, -26.1910, 28.0278);
		POI point3 = new POI("Matrix", 52, -26.1905, 28.0278);
		POI point4 = new POI("Tower of Light", 1, -26.1908, 28.000378);
		currentloc = new POI("Current Location",520 , -26.1909, 28.0278);
		//currentloc = new POI("Current Location",LocationLoggerService.ALTITUDE , LocationLoggerService.LATITUDE, LocationLoggerService.LONGITUDE);
		pois.add(point1);
		pois.add(point2);
		pois.add(point3);
		pois.add(point4);
		
		// Real sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		    	
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
		SpeakButton = (Button) findViewById(R.id.ButtonSpeak);
		ExpCheck = (CheckBox) findViewById (R.id.CheckExplicit);
		ExpCheck.setChecked(true);
    	checked = false;
		
		AzimuthBar.setOnSeekBarChangeListener(SpatialSeekBarChangeListener);
		DistanceBar.setOnSeekBarChangeListener(SpatialSeekBarChangeListener);
		ElevationBar.setOnSeekBarChangeListener(SpatialSeekBarChangeListener);
		SpeedBar.setOnSeekBarChangeListener(SpeedSeekBarChangeListener);
		ExpCheck.setEnabled(true);
		sp = new SoundPool(3, AudioManager.STREAM_MUSIC, 0);
		
		SimpleRB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent simpleTTS = new Intent(AbsoluteTTS.this, SimpleTTS.class);
				startActivity(simpleTTS);
			}
		});
		
		RelativeRB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent relativeTTS = new Intent(AbsoluteTTS.this, RelativeTTS.class);
				startActivity(relativeTTS);
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
				tts.stop();
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
		       	sensorManager.unregisterListener(AbsoluteTTS.this);
		        }
		        if (isChecked)
		        {
		        	checked = true;
		        	sensorManager.registerListener(AbsoluteTTS.this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
		    //		sensorManager.registerListener(AbsoluteTTS.this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
		        }

		    }
		});
	}
    
	public void announce(){
		tts.stop();
		for(int i = 0; i < pois.size(); i++){
			while(!finishedspeaking);
			speak(pois.get(i), TextToSpeech.QUEUE_ADD);
			finishedspeaking = false;
		}
	}
	
    private void speak(POI poi, int flushQueue) {
    		setPitch(poi);
    		setVolume(poi);
    		tts.setSpeechRate(((float) SpeedBar.getProgress())/10 + 1);
			if(exp_checked)
				tts.speak(poi.getName() + convertDirec(AzimuthBar.getProgress() + poi.bearingTo((Location) currentloc)) + fixDistance(poi.distanceTo(currentloc)+ DistanceBar.getProgress()) + fixElevation(poi.getAltitude()-currentloc.getAltitude()+ElevationBar.getProgress()), flushQueue, ttsmap);
			else
				tts.speak(poi.getName() + convertDirec(AzimuthBar.getProgress() + poi.bearingTo((Location) currentloc)), flushQueue, ttsmap);
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
    
    @Override
    public void onUtteranceCompleted(String arg0) {
    	finishedspeaking = true;    	
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
        tts.setOnUtteranceCompletedListener(this);
        }
	
	}

	private void setPitch(POI poi){
		float num = poi.distanceTo(currentloc) +DistanceBar.getProgress();
		if (num==0)
			num = (float)0.1;
		double angle = Math.atan((poi.getAltitude()-currentloc.getAltitude()+ElevationBar.getProgress())/num);
		//double pitch = angle*pitchfactor + 1;
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
	
 
	private String convertDirec(float azimuth) {
		 int temp = (int) Math.floor((azimuth+15)/30);
		 temp = temp%12;
		 if (temp==0)
		 	temp =12;
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

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	public void onSensorChanged(SensorEvent event) {
		compassPoints = event.values;
		int num = compassPoints.length;
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
				AbsoluteRB.setChecked(true);
				AbsoluteRB.setEnabled(false);
				
				SpeedBar.setEnabled(true);
				ElevationBar.setEnabled(true);
				AzimuthBar.setEnabled(true);
				DistanceBar.setEnabled(true);
				VolumeBar.setEnabled(false);
				if(checked)
				{
					checked = true;
					AutoCheck.setChecked(checked);
				sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
			//	sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
				}
				else
				{
					checked = false;
					AutoCheck.setChecked(checked);
				}
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
}