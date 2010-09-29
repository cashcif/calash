package android.speech.tts.location;

public interface ITTS {
	public void speak(POI poi, int flushQueue);
	public void onDestroy();
	public void onInit(int status);
}