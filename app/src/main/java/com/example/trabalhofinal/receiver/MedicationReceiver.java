package com.example.trabalhofinal.receiver;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.navigation.NavDeepLinkBuilder;
import com.example.trabalhofinal.MainActivity;
import com.example.trabalhofinal.R;

public class MedicationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("medicationName");
        int id = intent.getIntExtra("medicationId", -1);

        Log.d("MED_DEBUG", "Alarme DISPARADO para: " + name + " (ID: " + id + ")");

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = "medication_reminders";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                channelId, 
                "Lembretes de Medicamentos", 
                NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Canal para avisar a hora de tomar os remédios");
            channel.enableVibration(true);
            if (manager != null) manager.createNotificationChannel(channel);
        }

        // Deep link to ConfirmMedicationFragment
        Bundle args = new Bundle();
        args.putInt("medicationId", id);
        args.putString("medicationName", name);

        PendingIntent pi = new NavDeepLinkBuilder(context)
                .setGraph(R.navigation.nav_graph)
                .setDestination(R.id.confirmMedicationFragment)
                .setArguments(args)
                .createPendingIntent();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_health)
                .setContentTitle("Hora do Remédio!")
                .setContentText("Está na hora de tomar: " + name)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setFullScreenIntent(pi, true) // High priority alert
                .setContentIntent(pi);

        if (manager != null) {
            manager.notify(id, builder.build());
        }
    }
}
