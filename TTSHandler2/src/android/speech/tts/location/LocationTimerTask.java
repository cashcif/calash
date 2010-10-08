package android.speech.tts.location;

import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;


public class LocationTimerTask extends TimerTask{
	
	private Bundle geoPointBundle;
	private Intent GeoMeasurement = new Intent(
			LocationLoggerService.BROADCAST_LOCATION_MEASUREMENTS);
	private LocationLoggerService lls;
	
	LocationTimerTask(Bundle geoPointBundle, LocationLoggerService lls)
	{
		this.geoPointBundle= geoPointBundle;
		this.lls= lls;
	}

	@Override
	public void run() {
		GeoMeasurement.putExtras(geoPointBundle);
		lls.sendBroadcast(GeoMeasurement);
	}

}
