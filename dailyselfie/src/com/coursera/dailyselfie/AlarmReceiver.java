package com.coursera.dailyselfie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
	
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

}
