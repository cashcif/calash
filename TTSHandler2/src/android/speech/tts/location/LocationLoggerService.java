package android.speech.tts.location;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationLoggerService  extends Service implements LocationListener{
	public static double LATITUDE;
	public static double LONGITUDE;
	public static double ALTITUDE;
	public static float SPEED;
	
	 private final static String TAG = "LocationLoggerService";
	 LocationManager locationManager;
//	 GPXWriter writer;
	 
	 @Override
	 public void onCreate() {
	  subscribeToLocationUpdates();
	 }
	 
	@Override
	public void onLocationChanged(Location loc) {
		Log.d(TAG, loc.toString());
//	     ContentValues values = new ContentValues();
//	        values.put(GPSData.GPSPoint.LONGITUDE, loc.getLongitude());
//	        values.put(GPSData.GPSPoint.LATITUDE, loc.getLatitude());
//	        values.put(GPSData.GPSPoint.TIME, loc.getTime());
//	     getContentResolver().insert(GPSDataContentProvider.CONTENT_URI, values);
		LATITUDE = loc.getLatitude();
		LONGITUDE = loc.getLongitude();
		ALTITUDE = loc.getAltitude();
		SPEED = loc.getSpeed();
		
	}

	public void subscribeToLocationUpdates() {
	  this.locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	  this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
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
