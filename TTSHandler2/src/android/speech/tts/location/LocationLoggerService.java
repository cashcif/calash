package android.speech.tts.location;

import java.util.Timer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class LocationLoggerService  extends Service implements LocationListener{
	public static double LATITUDE;
	public static double LONGITUDE;
	public static double ALTITUDE;
	public static float SPEED;
	
	public static final String BROADCAST_LOCATION_MEASUREMENTS = "android.speech.tts.location.LocationLoggerService";
	private Bundle geoPointBundle;
	
	 private final static String TAG = "LocationLoggerService";
	 LocationManager locationManager;
	 
	 
	 @Override
	 public void onCreate() {
		 geoPointBundle = new Bundle();
		 Toast.makeText(this, "Service Created", Toast.LENGTH_LONG).show();
	  subscribeToLocationUpdates();
	  Log.d("LocationLoggerService", "onCreate:subscribeToLocationUpdates");
	  LocationTimerTask geoTimer = new LocationTimerTask(geoPointBundle, this);
	  
	  Timer locationMeasurements = new Timer();
		locationMeasurements.schedule(geoTimer , 1000, 1000);
		Log.d("LocationLoggerService", "onCreate:schedule");
	 }
	 
	
	 
	@Override
	public void onLocationChanged(Location loc) {
		Log.d(TAG, loc.toString());
		
		geoPointBundle.putDouble("latitude", loc.getLatitude());
		geoPointBundle.putDouble("longitude", loc.getLongitude());
		geoPointBundle.putDouble("altitude", loc.getAltitude());
		
		Log.d(TAG, "2" + loc.toString());		
	}

	public void subscribeToLocationUpdates() {
	  this.locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	  this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
	  Log.d("LocationLoggerService", "onCreate:subscribeToLocationUpdates()");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
	
	}
	@Override
	public void onDestroy() {
		super.onDestroy();

		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();

	}

	@Override
	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);

		Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

	}

}

//public void run() {
//Log.i("Thread Running", "Location listening thread");
//
//locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
////showLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
//if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
//	locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,this);
//if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
//	locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
//
//while (true) {
//	try {
//		
//		Log.i("Location Thread", "Latitude: " + LATITUDE + " , "
//				+ "Latitude: " + LONGITUDE);
//	} catch (InterruptedException e) {
//		Log.e("Error", e.getMessage());
//	}
//}
//}
