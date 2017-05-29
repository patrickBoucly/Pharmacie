package com.example.ensai.medic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {
    private Button buttonPharma;
    private Button ajouter_medic;
    private Button vaccin;
    private Button geoloc;
    private Button scan;
    private TextView textView;
    public static MySQLiteHelper bdd=null;

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
