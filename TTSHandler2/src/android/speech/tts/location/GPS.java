package android.speech.tts.location;

import android.location.Location;
import android.location.LocationManager;

public class GPS implements ILocationHandler{
	
	@Override
	public Location getCurrentLocation() {
		Location loc = new Location(LocationManager.GPS_PROVIDER);
		loc.setAltitude(0);
		loc.setLatitude(-26.19);
		loc.setLongitude(28.027);
		return loc;
	}

}
