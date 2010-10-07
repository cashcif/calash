//http://www.vogella.de/articles/AndroidLocationAPI/article.html#overview_locationapi

package com.Location;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class LocationHandler extends Activity implements LocationListener{
	
	private TextView info;
	private LocationManager locationManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		info = (TextView) findViewById(R.id.TextView01);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		showLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
		if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
	}

	@Override
	public void onLocationChanged(Location loc) {
		showLocation(loc);
	}
	
	public void showLocation(Location loc) {
		if (loc != null) {
			info.setText(loc.toString());
		} else
			info.setText("GPS not available");
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
		//info.setText(arg0 + String.valueOf(arg1) + arg2.toString());
	}

}
