package com.example.fcpsalerts;

import java.io.IOException;
import org.jsoup.Jsoup;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.fcpsalerts.fcpsalerts.R;

public class FCPS_Status_Service extends IntentService {

	protected SharedPreferences settings;

	public FCPS_Status_Service() {
		super("FCPS_Status_Service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// Loads shared preferences
		settings = getSharedPreferences("ASyncTask_Demo", 0);
		// Checks to see if user wants updates
		if (settings.getBoolean("notifications", true) == false)
			return;
		// Fetches status of FCPS emergency page
		String status = null;
		try {
			status = getStatus();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Check to see whether status is significant
		if (notificationWorthy(status)) {
			sendNotification(status);
		}
		// Updates previous status as long as the status fetched was not null
		if (status != null) {
			SharedPreferences.Editor editor = settings.edit();
			editor.putString("previousStatus", status);
		}
		scheduleNextUpdate();
	}

	protected String getStatus() throws IOException {
		return Jsoup.connect("http://www.fcps.edu/news/emerg.shtml").get()
				.getElementsByTag("strong").first().text();
	}

	private boolean notificationWorthy(String status) {
		boolean notificationsEnabled = settings.getBoolean("notifications",
				true);
		boolean statusNotDefault = status
				.equalsIgnoreCase("There are no emergency announcements at this time. ");
		boolean statusNotSame = status.equalsIgnoreCase(settings.getString(
				"previousStatus",
				"There are no emergency announcements at this time. "));
		return notificationsEnabled && statusNotDefault && statusNotSame;
	}

	private void sendNotification(String content) {

		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, 0);

		Notification n = new Notification.Builder(this)
				.setContentTitle("FCPS Emergency Update")
				.setContentText(content).setSmallIcon(R.drawable.ic_launcher)
				.setContentIntent(pendingIntent).setAutoCancel(true).build();

		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(0, n);
	}

	protected void scheduleNextUpdate() {
		int refresh = settings.getInt("refreshRate", 60) * 60000;

		Intent intent = new Intent(this, this.getClass());
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC, refresh, pendingIntent);
	}
}
