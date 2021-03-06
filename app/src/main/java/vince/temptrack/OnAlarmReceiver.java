package vince.temptrack;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.SystemClock;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by root on 12/01/17.
 */

public class OnAlarmReceiver extends BroadcastReceiver {



    public void onReceive(Context context, Intent intent) {
        //1er jour: je suppose que ça empêche les mesures quand action battery changed pas déclenché (la nuit quand dodo)
        //Intent intent2 = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        //float  temp   = ((float) intent2.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0)/10);

        long timestamp = System.currentTimeMillis();
        Date date = new Date(timestamp);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.FRANCE);
        String date_txt = format.format(date);

        Map.Entry<String,Float> pair=new AbstractMap.SimpleEntry<>(date_txt,MainActivity.temperature);
        MainActivity.theTableau.add(pair);
        Log.d("Vincent"," On est dans onReceive... temperature="+MainActivity.temperature+" taille du tableau="+MainActivity.theTableau.size());

        //depuis alarmmanager set exact il faut bien que je relance une autre alarme!
        long now = SystemClock.elapsedRealtime();
        AlarmManager mgr= (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent i=new Intent(context, OnAlarmReceiver.class);
        PendingIntent pi= PendingIntent.getBroadcast(context, 0, i, 0);
        long heure_prochaine_alarme = now + MainActivity.frequence_alarme;
        mgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, heure_prochaine_alarme, pi);
        Log.d("Vincent","On est dans onReceive... now= " + now + " et prochaine alarme sera lancée à " + heure_prochaine_alarme);
    }

}




