package android.speech.tts.location;

import java.util.UUID;

public interface IPOI {
	public double getLatitude();
	public void setLatitude(double latitude);
	public double getLongitude();
	public void setLongitude(double longitude);
	public double getAltitude();
	public void setAltitude(double height);
	public UUID getId();
	public String getName();
	public float distanceTo(IPOI poi);
}
