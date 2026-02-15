package com.example.aiagent;

import android.content.Context;
import android.os.Vibrator;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ActionHelper {
    private static Context appContext;

    public static void init(Context context) {
        appContext = context.getApplicationContext();
        createNotificationChannel();
    }

    public static void sendNotification(String title, String text) {
        if (appContext == null) return;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(appContext, "AI_AGENT_CHANNEL")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(appContext);
        notificationManager.notify(1001, builder.build());
    }

    public static void vibrate(long duration) {
        if (appContext == null) return;
        Vibrator vibrator = (Vibrator) appContext.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null && vibrator.hasVibrator()) {
            vibrator.vibrate(duration);
        }
    }

    public static void showToast(String message) {
        if (appContext == null) return;
        Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show();
    }

    private static void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "AI Agent Channel";
            String description = "Channel for AI Agent notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("AI_AGENT_CHANNEL", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = appContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
