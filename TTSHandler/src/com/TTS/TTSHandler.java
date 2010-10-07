//http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/app/TextToSpeechActivity.html
package com.TTS;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class TTSHandler extends Activity implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

    private TextToSpeech mTts;
    private Button Button1;
    private EditText EditText1;
    private SeekBar SeekBar1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTts = new TextToSpeech(this, this);
        Button1 = (Button) findViewById(R.id.again_button);
        EditText1 = (EditText) findViewById(R.id.EditText01);
        SeekBar1 = (SeekBar) findViewById(R.id.SeekBar01);
        
    	//am.setStreamVolume(AudioManager.STREAM_SYSTEM, SeekBar1.getProgress(), AudioManager.FLAG_VIBRATE);
    	//ToneGenerator mytone = new ToneGenerator(AudioManager.STREAM_MUSIC,100);
    	//mytone.startTone(ToneGenerator.TONE_DTMF_0, 5000);
    	//mTts.setPitch(10);
    	//mTts.setSpeechRate(2);
    	
    	
    	/*String mytext = "Once upon a time in a land far far away there lived snow white and seven little dwarfs";
    	String destfile = Environment.getExternalStorageDirectory().toString() + "/test/test.wav";
    	EditText1.setText(destfile);
    	hm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, mytext);
    	//mTts.synthesizeToFile(mytext, hm, destfile);
    	try {
			mp.setDataSource(destfile);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//mp.setVolume(1, 0);
		try {
			mp.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mp.start();*/
        
        Button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	//SoundPool sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
            	//sp.setVolume(AudioManager.STREAM_MUSIC, 1, 0);
            	HashMap<String, String> hm = new HashMap<String, String>();
            	hm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_SYSTEM));
            	hm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "stuff");
            	mTts.setPitch(1);
            	mTts.setSpeechRate(1);
            	AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
            	am.setStreamVolume(AudioManager.STREAM_SYSTEM, SeekBar1.getProgress(), AudioManager.FLAG_VIBRATE);
            	mTts.speak(EditText1.getText().toString(), TextToSpeech.QUEUE_FLUSH, hm);
            }
        });
    }
    
    @Override
    public void onDestroy() {
        if (mTts != null) {
            mTts.stop();
            mTts.shutdown();
        }
        super.onDestroy();
    }

    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = mTts.setLanguage(Locale.ENGLISH);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            	this.onDestroy();
            mTts.setOnUtteranceCompletedListener(this);
        }
    }

	@Override
	public void onUtteranceCompleted(String arg0) {
		EditText1.setText("");
		
	}

}