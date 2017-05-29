package com.example.ensai.medic;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ensai on 23/05/17.
 */

public class SelectedMedic extends Activity {
    TextView tv;
    Button sup;
    Button info;
    String monMedic="Médicament sélectionné : ";
    String nomMedic;
    String date="";
    DatePicker simpleDatePicker;
    TextView dateperemption;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectedmedic);
        tv=(TextView) findViewById(R.id.selectedmedic);
        Intent intent = getIntent();
        String medic =intent.getExtras().getString("monmedic");
        date=intent.getExtras().getString("peremption");
        Log.i("madate",date);
        nomMedic=medic;
        tv.setText(monMedic+medic);
        sup=(Button) findViewById(R.id.supprimer);
        info=(Button) findViewById(R.id.info);
        dateperemption=(TextView) findViewById(R.id.dateperemption);
        simpleDatePicker = (DatePicker) findViewById(R.id.simpleDatePicker);
        if(date.length()>1) {
            simpleDatePicker.setVisibility(View.INVISIBLE);
            dateperemption.setText("Date d'expiration : " + date.substring(2) + "/20" + date.substring(0, 2));
        }
             // initiate a date picker

    }

    public void suppresion(View v) {
        MedicDAO dao=new MedicDAO(this);
        dao.deleteMedicName(nomMedic);
        Intent i=new Intent(this,MaPharma.class);
        startActivity(i);
    }
    public void information(View v) {
        MedicDAO dao=new MedicDAO(this);
        Medic m=dao.getMedicFromName(nomMedic);
        Log.i("monId",""+m.getIdMedic());
        Log.i("monCIS ", m.getCodeCIS());
        Log.i("monName ", m.getName());
        String url="http://base-donnees-publique.medicaments.gouv.fr/affichageDoc.php?specid="+ m.getCodeCIS()+"&typedoc=R";
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }

    public void AjoutDate(View v) {
        MedicDAO dao=new MedicDAO(this);
        Medic m=dao.getMedicFromName(nomMedic);
        Log.i("date exp",simpleDatePicker.getYear()+"  "+simpleDatePicker.getMonth());
        String dateStr="";
        int month=simpleDatePicker.getMonth()+1;
        if(month<10) {
            dateStr = ("" + simpleDatePicker.getYear()).substring(2) + "0" + month;
            Log.i("date exp",dateStr);

        }else{
            dateStr = ("" + simpleDatePicker.getYear()).substring(2)+ month;
            Log.i("date exp",dateStr);

        }

        Medic medica=dao.getMedicFromName(nomMedic);
        medica.setPeremption(dateStr);
        dao.update(medica);
        Intent i = new Intent(getApplicationContext(), SelectedMedic.class);
        i.putExtra("monmedic", nomMedic);
        i.putExtra("peremption",dateStr);
        startActivity(i);
    }


    public void vers_accueil (View v) {
        Intent intent = new Intent(this, SelectedMedic.class);
        startActivity(intent);
    }




}
