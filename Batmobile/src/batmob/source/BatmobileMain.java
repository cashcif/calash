package batmob.source;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class BatmobileMain extends Activity {
	private static final String TAG = "BatmobileMain";
	/** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button buttonEnter = (Button) findViewById(R.id.buttonEnter);
        Log.v(TAG, "Start up screen");
		buttonEnter.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Log.v(TAG, "Starting Application");
				Intent grid = new Intent(BatmobileMain.this, ButtonGrid.class);
				BatmobileMain.this.startActivity(grid);
				
			}

		});
    }
}