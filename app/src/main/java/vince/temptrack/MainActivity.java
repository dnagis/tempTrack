package vince.temptrack;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/*
début 120117 --
Monitorer la temperature la nuit surtout, quand le tel fonctionne au minimum--temp de la batterie
Niveau temperature c'est pas mal du tout.
Premier test: très peu de mesures la nuit alors qu'en journée aucun problème -- hypothèse:
https://developer.android.com/reference/android/app/AlarmManager.html -->
"Beginning with API 19 (KITKAT) alarm delivery is inexact: the OS will shift alarms in order to minimize wakeups and battery use."
 */

public class MainActivity extends AppCompatActivity {

    public static List<Map.Entry<String,Float>> theTableau = new ArrayList<>();
    public static float temperature;
    public static long frequence_alarme = 3600000;//en millisecondes
    //mBatInfoReceiver myBatInfoReceiver;
    TextView hello;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        premier_set_alarme();
        //myBatInfoReceiver = new mBatInfoReceiver();
        //Intent intentTemp = registerReceiver(new myBatInfoReceiver(), new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    public class myBatInfoReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, Intent intent) {
            temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0)/10;
        }

    }

    public void actionPressBouton(View v) {
        //8double temp = myBatInfoReceiver.get_temp();
        //String message = "Current " + BatteryManager.EXTRA_TEMPERATURE + " = " + temp +  Character.toString ((char) 176) + " C";
        //Log.d("Vincent","taille du tableau="+theTableau.size());
        //Log.d("Vincent","temp en double="+temp);
        hello   = (TextView) findViewById(R.id.textView);
        StringBuilder builder = new StringBuilder();
        /*for (Map.Entry<String,Float> item : theTableau) {
            builder.append(item.toString());
            builder.append("\n");
        }*/

//attention plante si size=0
        if (theTableau.size()>0) {
            for (int i = theTableau.size(); i > 0 && (i > theTableau.size()-28); i--) {
                builder.append(theTableau.get(i-1).toString());
                builder.append("\n");
                //System.out.println(crunchifyList.get(i));
            }
        }


        hello.setText(builder.toString().trim());




    }


    public void premier_set_alarme(){
        long now = SystemClock.elapsedRealtime();
        AlarmManager mgr= (AlarmManager) this.getSystemService(this.ALARM_SERVICE);
        Intent i=new Intent(this, OnAlarmReceiver.class);
        PendingIntent pi= PendingIntent.getBroadcast(this, 0, i, 0);
        //mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 900000, pi); //pb c'est que l'OS ajuste pour optimiser la batterie
        long heure_prochaine_alarme = now + frequence_alarme;
        mgr.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, heure_prochaine_alarme, pi);
        Log.d("Vincent","On est dans premier_set_alarme... now= " + now + " et prochaine alarme sera lancée à " + heure_prochaine_alarme);

    }




}




