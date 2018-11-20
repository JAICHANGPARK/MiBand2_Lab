/*  Copyright (C) 2016-2018 0nse, Carsten Pfeiffer

    This file is part of Gadgetbridge.

    Gadgetbridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Gadgetbridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>. */
package nodomain.knu2018.bandutils;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;

import nodomain.knu2018.bandutils.activities.ConfigureAlarms;
import nodomain.knu2018.bandutils.impl.GBAlarm;
import nodomain.knu2018.bandutils.model.ActivityUser;
import nodomain.knu2018.bandutils.util.GB;

/**
 * Implementation of SleepAlarmWidget functionality. When pressing the widget, an alarm will be set
 * to trigger after a predefined number of hours. A toast will confirm the user about this. The
 * value is retrieved using ActivityUser.().getSleepDuration().
 */
public class SleepAlarmWidget extends AppWidgetProvider {

    /**
     * This is our dedicated action to detect when the widget has been clicked.
     */
    public static final String ACTION =
            "nodomain.knu2018.gadgetbridge.SLEEP_ALARM_WIDGET_CLICK";

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.sleep_alarm_widget);

        // Add our own click intent
        Intent intent = new Intent(ACTION);
        PendingIntent clickPI = PendingIntent.getBroadcast(
                context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.sleepalarmwidget_text, clickPI);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (ACTION.equals(intent.getAction())) {
            int userSleepDuration = new ActivityUser().getSleepDuration();
            // current timestamp
            GregorianCalendar calendar = new GregorianCalendar();
            // add preferred sleep duration
            calendar.add(Calendar.HOUR_OF_DAY, userSleepDuration);


            // overwrite the first alarm and activate it
            GBAlarm alarm = GBAlarm.createSingleShot(0, true, calendar);
            alarm.store();

            if (GBApplication.isRunningLollipopOrLater()) {
                setAlarmViaAlarmManager(context, calendar.getTimeInMillis());
            }

            int hours = calendar.get(Calendar.HOUR_OF_DAY);
            int minutes = calendar.get(Calendar.MINUTE);

            GB.toast(context,
                    String.format(context.getString(R.string.appwidget_alarms_set), hours, minutes),
                    Toast.LENGTH_SHORT, GB.INFO);
        }
    }

    /**
     * Use the Android alarm manager to create the alarm icon in the status bar.
     *
     * @param packageContext {@code Context}: A Context of the application package implementing this
     *                       class.
     * @param triggerTime    {@code long}: time at which the underlying alarm is triggered in wall time
     *                       milliseconds since the epoch
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setAlarmViaAlarmManager(Context packageContext, long triggerTime) {
        AlarmManager am = (AlarmManager) packageContext.getSystemService(Context.ALARM_SERVICE);
        // TODO: launch the alarm configuration activity when clicking the alarm in the status bar
        Intent intent = new Intent(packageContext, ConfigureAlarms.class);
        PendingIntent pi = PendingIntent.getBroadcast(packageContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.setAlarmClock(new AlarmManager.AlarmClockInfo(triggerTime, pi), pi);
    }
}

