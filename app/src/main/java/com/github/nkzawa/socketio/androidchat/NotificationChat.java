package com.github.nkzawa.socketio.androidchat;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.ContextThemeWrapper;
import android.widget.TableRow;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class NotificationChat {

    private static String TAG = NotificationChat.class.getSimpleName();
    private Context context;

    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final int NOTIFICATION_ID = 100;

    public NotificationChat(Context context){
        this.context = context;
    }

    public void showNotificationMessage(String title, String message, String timeStamp, Intent intent){
        showNotificationMessage(title, message, timeStamp, intent, null);
    }

    public void showNotificationMessage(final String title, final String message, final String timeStamp, Intent intent, String imageUrl){
        if(TextUtils.isEmpty(message))
            return;

        final int icon = R.drawable.ic_launcher;

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        if(!TextUtils.isEmpty(imageUrl)){
            if(imageUrl != null && imageUrl.length() > 4 && Patterns.WEB_URL.matcher(imageUrl).matches()){
                showSmallNotification(builder, icon, title, message, timeStamp, intent);
            }
        }
        else{
            showSmallNotification(builder, icon, title, message, timeStamp, intent);
        }

    }

    private void showSmallNotification(NotificationCompat.Builder builder, int icon, String title, String message, String timeStamp, Intent intent) {
        if(timeStamp.equals(""))
            timeStamp = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()) + "";

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.addLine(message);
        Notification notification;
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        final Intent emptyIntent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notification = builder.setSmallIcon(icon)
                .setAutoCancel(true)
                .setContentTitle(title)
                .setStyle(inboxStyle)
                .setContentText(message)
                .setContentIntent(pi)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

}














