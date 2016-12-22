package mika.com.android.ac.notification.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.SystemClock;

import mika.com.android.ac.R;

/**
 * Created by mika on 2016/12/20.
 */

public class AlarmPoll {

    private static final int POLL_CODE = R.integer.intent_poll_code;

    /**
     * 开始 alarm轮询服务
     *
     * @param context  context
     * @param conn     conn
     * @param interval 轮询时间间隔
     */
    public static void startPolling(Context context, ServiceConnection conn, int interval) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, PollingService.class);
        context.bindService(intent, conn, Context.BIND_AUTO_CREATE);
        PendingIntent pendingIntent = PendingIntent.getService(context, POLL_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), interval * 1000, pendingIntent);
    }

    /**
     * 取消alarm轮询服务
     *
     * @param context context
     * @param conn     cls
     */
    public static void cancelPolling(Context context, ServiceConnection conn) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, PollingService.class);
        context.unbindService(conn);
//        intent.setAction()
        PendingIntent pendingIntent = PendingIntent.getService(context, POLL_CODE, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.cancel(pendingIntent);
    }
}
