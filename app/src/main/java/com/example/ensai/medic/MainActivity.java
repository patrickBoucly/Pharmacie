package com.example.ensai.medic;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.os.Bundle;

import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class MainActivity extends Activity {
    private Button buttonPharma;
    private Button ajouter_medic;
    private Button vaccin;
    private Button geoloc;
    private Button scan;
    private TextView textView;
    public static MySQLiteHelper bdd=null;
    private MedicDAO dao=new MedicDAO(this);
    private static final int REQUEST_ACCESS_CAMERA = 110 , REQUEST_ACCESS_FINE_LOCATION = 111, REQUEST_ACCESS_COARSE_LOCATION = 112;
    private PendingIntent pendingIntentVac;
    private PendingIntent pendingIntentMed;
    private int i=0;
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

        /*check notif périmé*/
        Calendar c = Calendar.getInstance();
        int date = (c.get(Calendar.YEAR)-2000)*100+c.get(Calendar.MONTH)+1;
        Log.i("today",""+date);
        ArrayList<Medic> mesMedic=( ArrayList<Medic> )dao.getAllMedics();
        for(Medic m:mesMedic){
            if(m.getPeremption().length()>3) {
                int madate = Integer.parseInt(m.getPeremption());
                Log.i("med", "" + madate);
                if (madate < date) {
                    i=i+1;
                    Log.i("medp", "perimé!");
                    String message = "Votre " + m.getName() + "est périmé!)";
                    Intent intent = new Intent(this, MaPharma.class);
                    pendingIntentMed = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.drawable.launcher)
                                    .setContentTitle("Médicament expiré")
                                    .setContentText(message)
                                    .setContentIntent(pendingIntentMed);


                    mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                    NotificationManager notificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
                    notificationManager.notify(i, mBuilder.build());

                }
            }
        }


        /*check notif vaccins*/
        int dateJour=Integer.parseInt( "20"+date);
        VaccinsDAO vaccinDAO = new VaccinsDAO(this);
        List<Vaccin> vaccins=vaccinDAO.getAllVaccins();
        Log.i("nb vac", ""+vaccins.size());
        if(!vaccins.isEmpty()) {
            Log.i("date today",""+ dateJour);
            Log.i("date vac", vaccins.get(0).getDate());
            for (Vaccin v : vaccins) {

                Log.i("vac d", "" + v.getDate());
                int madate = 0;
                if(v.getDate().charAt(2) == '/') {
                    if (v.getDate().charAt(4) == '/') {
                        Log.i("a", "" + v.getDate().substring(5) + "0" + v.getDate().substring(3, 4));

                        madate = Integer.parseInt("" + v.getDate().substring(5) + "0" + v.getDate().substring(3, 4));
                    } else {
                        Log.i("b", "" + v.getDate().substring(6) + v.getDate().substring(3, 5));
                        madate = Integer.parseInt("" + v.getDate().substring(6) + v.getDate().substring(3, 5));
                    }
                }else{
                    if (v.getDate().charAt(3) == '/') {
                        Log.i("a", "" + v.getDate().substring(4) + "0" + v.getDate().substring(2, 3));

                        madate = Integer.parseInt("" + v.getDate().substring(4) + "0" + v.getDate().substring(2, 3));
                    } else {
                        Log.i("b", "" + v.getDate().substring(5) + v.getDate().substring(2, 4));
                        madate = Integer.parseInt( "" + v.getDate().substring(5) + v.getDate().substring(2, 4));
                    }

                }

                Log.i("med", "" + madate);

                if (madate < dateJour && v.getRealise()==0) {
                    i=i+1;
                    Log.i("vac", "à faire!");
                    String message = v.getIndividu() + " doit faire le vaccin " + v.getdenomination() + " avant le " + v.getDate();
                    Intent intent = new Intent(this, MesVaccins.class);
                    pendingIntentVac = PendingIntent.getActivity(this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    NotificationCompat.Builder mBuilder =
                            new NotificationCompat.Builder(this)
                                    .setSmallIcon(R.drawable.new_medic)
                                    .setContentTitle("Vaccin en approche!")
                                    .setContentText(message)
                                    .setContentIntent(pendingIntentVac);


                    mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                    NotificationManager notificationManager = (NotificationManager) getSystemService(this.NOTIFICATION_SERVICE);
                    notificationManager.notify(i, mBuilder.build());
                }
            }
        }

        /*check permissions*/
        boolean hasPermissionPhoneState = (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionPhoneState) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CAMERA},REQUEST_ACCESS_CAMERA);
        }

        boolean hasPermissionLocation = (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionLocation) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_ACCESS_FINE_LOCATION);
        }

        boolean hasPermissionWrite = (ContextCompat.checkSelfPermission(getApplicationContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermissionWrite) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_ACCESS_COARSE_LOCATION);
        }




    }

    public void vers_pharma(View v) {
        Intent intent = new Intent(this, MaPharma.class);
        startActivity(intent);
        finish();
    }
    public void vers_ajouter_medic(View v) {
        Intent intent = new Intent(this, AjouterMedic.class);
        startActivity(intent);
        finish();
    }
    public void vers_vaccin(View v) {
        Intent intent = new Intent(this, MesVaccins.class);
        startActivity(intent);
        finish();
    }
    public void vers_geoloc(View v) {
        Intent intent = new Intent(this, Geoloc.class);
        startActivity(intent);
        finish();
    }
    public void vers_scan(View v) {
        Intent intent = new Intent(this, Scan.class);
        startActivity(intent);
        finish();
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode)
        {
            case REQUEST_ACCESS_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                    //reload my activity with permission granted or use the features what required the permission
                    finish();
                    startActivity(getIntent());
                } else
                {
                    Toast.makeText(this, "L'application n'est pas autorisée à utiliser l'appareil photo, ce qui empêche l'utilisation du scanner", Toast.LENGTH_LONG).show();
                }
            }
            case REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                    //reload my activity with permission granted or use the features what required the permission
                    finish();
                    startActivity(getIntent());
                } else
                {
                    Toast.makeText(this, "L'application n'a pas l'autorisation d'accès à votre géolocalisation, ce qui empêche de trouver la pharmacie la plus proche", Toast.LENGTH_LONG).show();
                }
            }

            case REQUEST_ACCESS_COARSE_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Permission granted.", Toast.LENGTH_SHORT).show();
                    //reload my activity with permission granted or use the features what required the permission
                    finish();
                    startActivity(getIntent());
                } else{
                    Toast.makeText(this,  "L'application n'a pas l'autorisation d'accès à votre géolocalisation, ce qui empêche de trouver la pharmacie la plus proche", Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}
