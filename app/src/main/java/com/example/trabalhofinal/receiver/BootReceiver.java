package com.example.trabalhofinal.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.trabalhofinal.data.AppDatabase;
import com.example.trabalhofinal.data.Medication;
import com.example.trabalhofinal.util.AlarmUtils;

import java.util.List;
import java.util.concurrent.Executors;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            Context appContext = context.getApplicationContext();
            Executors.newSingleThreadExecutor().execute(() -> {
                List<Medication> medications = AppDatabase.getInstance(appContext).appDao().getAllMedicationsSync();
                for (Medication med : medications) {
                    AlarmUtils.scheduleAlarm(appContext, med.id, med.name, med.time);
                }
            });
        }
    }
}
