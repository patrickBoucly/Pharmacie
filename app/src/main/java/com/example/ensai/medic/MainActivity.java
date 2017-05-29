package com.example.ensai.medic;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;

import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends Activity {
    private Button buttonPharma;
    private Button ajouter_medic;
    private Button vaccin;
    private Button geoloc;
    private Button scan;
    private TextView textView;
    public static MySQLiteHelper bdd=null;
    private MedicDAO dao=new MedicDAO(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // création des bdd:
        bdd=new MySQLiteHelper(this);
        //visuels:
        buttonPharma = (Button) findViewById((R.id.menu_pharma));
        ajouter_medic = (Button) findViewById((R.id.ajouter_medic));
        vaccin = (Button) findViewById((R.id.vaccin));
        geoloc = (Button) findViewById((R.id.geoloc));
       // textView = (TextView) findViewById(R.id.tv);
        scan= (Button) findViewById(R.id.scan);
        Calendar c = Calendar.getInstance();
        int date = (c.get(Calendar.YEAR)-2000)*100+c.get(Calendar.MONTH)+1;
        Log.i("today",""+date);
        ArrayList<Medic> mesMedic=( ArrayList<Medic> )dao.getAllMedics();
        for(Medic m:mesMedic){
            int madate=Integer.parseInt(m.getPeremption());
            int i=0;
            Log.i("med",""+madate);

            if(madate<date){
                Log.i("medp","perimé!");
                String message="Votre "+m.getName()+"est périmé!)";
                Intent intent = new Intent(this, MaPharma.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.new_medic)
                                .setContentTitle("Médicament expiré")
                                .setContentText(message)
                                .setContentIntent(pendingIntent);


                mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                NotificationManager notificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
                notificationManager.notify(i, mBuilder.build());

            }
        }






    }

    public void vers_pharma(View v) {
        Intent intent = new Intent(this, MaPharma.class);
        startActivity(intent);
    }
    public void vers_ajouter_medic(View v) {
        Intent intent = new Intent(this, AjouterMedic.class);
        startActivity(intent);
    }
    public void vers_vaccin(View v) {
        Intent intent = new Intent(this, MesVaccins.class);
        startActivity(intent);
    }
    public void vers_geoloc(View v) {
        Intent intent = new Intent(this, Geoloc.class);
        startActivity(intent);
    }
    public void vers_scan(View v) {
        Intent intent = new Intent(this, Scan.class);
        startActivity(intent);
    }


}
