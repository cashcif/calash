//http://developer.android.com/resources/samples/ApiDemos/src/com/example/android/apis/app/TextToSpeechActivity.html
package com.TTS;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Locale;

public class TTSHandler extends Activity implements TextToSpeech.OnInitListener {

    private TextToSpeech mTts;
    private Button mAgainButton;
    private EditText EditText1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTts = new TextToSpeech(this, this);
        mAgainButton = (Button) findViewById(R.id.again_button);
        EditText1 = (EditText) findViewById(R.id.EditText01);

        mAgainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	mTts.speak(EditText1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
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
            int result = mTts.setLanguage(Locale.FRENCH);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)
            	this.onDestroy();
        }
    }

}