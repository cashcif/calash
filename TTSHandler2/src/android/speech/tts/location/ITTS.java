package android.speech.tts.location;

public interface ITTS {
	public void announce();
	public void onDestroy();
	public void onInit(int status);
}