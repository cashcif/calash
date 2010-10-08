package android.speech.tts.location;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocationLoggerServiceManager extends BroadcastReceiver {

 public static final String TAG = "LocationLoggerServiceManager";
 @Override
 public void onReceive(Context context, Intent intent) {
  // just make sure we are getting the right intent (better safe than sorry)
  if( "android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
//   ComponentName comp = new ComponentName(context.getPackageName(), LocationLoggerService.class.getName());
//   ComponentName service = context.startService(new Intent().setComponent(comp));
	  Intent serviceIntent = new Intent();
		serviceIntent.setAction("android.speech.tts.location.LocationLogggerService");
		context.startService(serviceIntent);

//   if (null == service){
//    // something really wrong here
//    Log.e(TAG, "Could not start service " + comp.toString());
//   }
//  } else {
//   Log.e(TAG, "Received unexpected intent " + intent.toString());   
  }
}
}