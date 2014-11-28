package com.coursera.dailyselfie;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

/**
 * Handles scheduling of alarms and receiving of broadcasted events
 * @author anfol1
 *
 */
public class SelfieAlarmHandler extends BroadcastReceiver {
	
	private long ALARM_PERIOD = 2 * 30 * 1000; //period for the alarm
	private int MY_NOTIFICATION_ID = 666;
	

	@Override
	public void onReceive(Context context, Intent intent) {
		//This method will create an notification and send mainactivity as its intent!
		
		//Toast.makeText(context, "Alarm fired", Toast.LENGTH_LONG).show();
		Intent i = new Intent(context,MainActivity.class);
		
		PendingIntent pending = PendingIntent.getActivity(context,1, i,PendingIntent.FLAG_UPDATE_CURRENT);
		Notification noti = new Notification.Builder(context).setSmallIcon(android.R.drawable.ic_menu_camera).setContentTitle("Hey pretty! Time to take another selfie :)").setContentIntent(pending).setAutoCancel(true).build();

		//Send the notification
		NotificationManager nmgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nmgr.notify(MY_NOTIFICATION_ID, noti);
	}
	
	/**
	 * Scedules the alarm.
	 */
	public void scheduleAlarm(Context context) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

		PendingIntent pendingIntent = createIntent(context);

	    //set a repeating alarm for the given alarm period
	    alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,  SystemClock.elapsedRealtime() + ALARM_PERIOD, ALARM_PERIOD , pendingIntent);
	    Log.i("TAG","Alarm scheduled");
	}
	
	private PendingIntent createIntent(Context context) {
		Intent broadcast_intent = new Intent(context, SelfieAlarmHandler.class);
		PendingIntent pendingIntent= PendingIntent.getBroadcast(context, 0,  broadcast_intent, 0);
		return pendingIntent;
	}
	
	/**
	 * Cancel the alarm (if it is set)
	 * @param context
	 */
	public void cancelAlarm(Context context) {
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		//create an intent that is the same as the one to cancel!
		
		PendingIntent pendingIntent = createIntent(context);
		    
		alarmManager.cancel(pendingIntent);
		Log.i("TAG","Alarm canceled");
	}
}
