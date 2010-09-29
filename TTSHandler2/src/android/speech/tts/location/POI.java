package android.speech.tts.location;

import java.util.UUID;

import android.location.Location;
import android.location.LocationManager;

public class POI extends Location implements IPOI{

	private UUID Id;
	private String Name;

	public POI(String name, double altitude, double latitude, double longitude){
		super(LocationManager.GPS_PROVIDER);
		super.setAltitude(altitude);
		super.setLatitude(latitude);
		super.setLongitude(longitude);
		Name = name;
		Id = UUID.randomUUID();
	}

	public UUID getId() {
		return Id;
	}

	public String getName() {
		return Name;
	}

}
