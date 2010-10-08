package android.speech.tts.location;

import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class LocationTimerTask extends TimerTask{
	
	private Bundle geoPointBundle;
	private Intent GeoMeasurement = new Intent(
			LocationLoggerService.BROADCAST_LOCATION_MEASUREMENTS);
	private LocationLoggerService lls;
	private final static String TAG = "LocationLoggerService";
	
	LocationTimerTask(Bundle geoPointBundle, LocationLoggerService lls)
	{
		this.geoPointBundle= geoPointBundle;
		this.lls= lls;
		Log.d(TAG, "In timer constructor");
		
	}

	@Override
	public void run() {
		
		GeoMeasurement.putExtras(geoPointBundle);
		lls.sendBroadcast(GeoMeasurement);
		Log.d(TAG, "In timer run");

	}

}
