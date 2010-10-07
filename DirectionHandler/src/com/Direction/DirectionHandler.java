package com.Direction;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class DirectionHandler extends Activity implements SensorEventListener {
    /** Called when the activity is first created. */
   
   private SensorManager sensorManager;
   private TextView txtRawData;
   private float myAzimuth = 0;
   private float myPitch = 0;
   private float myRoll = 0;
   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        txtRawData = (TextView) findViewById(R.id.txt_info);
        
        // Real sensor manager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }
    
    @Override
    protected void onResume() {
      super.onResume();
      sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_NORMAL);
      //sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
    }
    
    @Override
    protected void onPause() {
      super.onPause();
      sensorManager.unregisterListener(this);
    }

   public void onSensorChanged(SensorEvent event) {
           /*myAzimuth = Math.round(event.values[0]);
           myPitch = Math.round(event.values[1]);
           myRoll = Math.round(event.values[2]);*/
	   myAzimuth = event.values[0];
       myPitch = event.values[1];
       myRoll = event.values[2];

       //http://sharkysoft.com/archive/printf/docs/javadocs/lava/clib/stdio/doc-files/specification.htm
           String out = String.format("Azimuth: %f\nPitch:%f\nRoll:%f\nDirection: %s", myAzimuth, myPitch, myRoll,printDirection(myAzimuth));
           txtRawData.setText(out);
   }
   
   private String printDirection(float azimuth) {
      if (azimuth < 22)
      return "N";
      else if (azimuth >= 22 && azimuth < 67)
         return "NE";
      else if (azimuth >= 67 && azimuth < 112)
         return "E";
      else if (azimuth >= 112 && azimuth < 157)
         return "SE";
      else if (azimuth >= 157 && azimuth < 202)
         return "S";
      else if (azimuth >= 202 && azimuth < 247)
         return "SW";
      else if (azimuth >= 247 && azimuth < 292)
         return "W";
      else if (azimuth >= 292 && azimuth < 337)
         return "NW";
      else if (azimuth >= 337)
         return "N";
        else
           return null;
   }

public void onAccuracyChanged(Sensor arg0, int arg1) {
	// TODO Auto-generated method stub
	
}
}