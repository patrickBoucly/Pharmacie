package com.example.ensai.medic;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static com.example.ensai.medic.R.id.date;
import static com.example.ensai.medic.R.id.spinner;

/**
 * Created by ensai on 25/05/17.
 */

public class MesVaccinsDetail extends Activity {
    private Button bouton2 = null;
    private  Button bouton_valider = null;
    private EditText editText=null;
    private ListView resultats_vaccins;
    private VaccinsDAO vaccinsDAO;
    private String nom="";
    private  TextView test =null ;
    private Spinner spinner=null;
    private String vaccinAjoute=null;
    private DatePicker datePicker=null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mesvaccinsdetail);
        Intent intent = getIntent();
        nom =intent.getExtras().getString("nom");

        TextView textView = (TextView) findViewById(R.id.ecran_detail);
        textView.setText(nom);

        editText = (EditText) findViewById(R.id.vaccin);
        editText.setVisibility(View.INVISIBLE);
        bouton_valider= (Button) findViewById(R.id.valider_ajouter_vaccin);
        bouton_valider.setVisibility(View.INVISIBLE);

        bouton2 = (Button) findViewById(R.id.ajouter_personne);
        resultats_vaccins = (ListView) findViewById(R.id.resultats_vaccins);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.setVisibility(View.INVISIBLE);

        vaccinsDAO= new VaccinsDAO(this);



        // afficher la liste des personnes enregistrées:
        List<String> vaccins = vaccinsDAO.getFromName(nom);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MesVaccinsDetail.this,
                android.R.layout.simple_list_item_1, vaccins);
        resultats_vaccins.setAdapter(adapter);



        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setVisibility(View.INVISIBLE);



        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("BCG");
        categories.add("Diphtérie-Tétanos-Poliomyélite");
        categories.add("Coqueluche");
        categories.add("Haemophilus Influenzae de type b (HIB)");
        categories.add("Hépatite B");
        categories.add("Pneumocoque");
        categories.add("Méningocoque C");
        categories.add("Rougeole-Oreillons-Rubéole");
        categories.add("Papillomavirus humain (HPV)");
        categories.add("Grippe");
        categories.add("Zona");



        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                vaccinAjoute= item.toString();
                Log.i("test", item.toString());
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



    }




    public void ajouter_vaccin(View v) {
       // editText.setVisibility(View.VISIBLE);
        bouton_valider.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
        datePicker.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Entrez le nom du vaccin à ajouter", Toast.LENGTH_LONG).show();

    }

    public void  valider_ajouter_vaccin(View v) {
       // String vaccin = editText.getText().toString();
        String vaccin=vaccinAjoute;
        Log.i("verif", vaccin);

        // get the values for day of month , month and year from a date picker
        String day =  ""+datePicker.getDayOfMonth();
        String month = ""+(datePicker.getMonth()+1) ;
        String year = ""+datePicker.getYear();
        String date= day+"/"+month+"/"+year;

        // ajouter le vaccin dans la base
        vaccinsDAO.open();
        vaccinsDAO.add(nom, vaccin, date);
        vaccinsDAO.close();
        Toast.makeText(this, "Ajoutons le vaccin "+vaccin + " pour: "+nom + " à la date du " +date, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MesVaccins.class);
        startActivity(intent);


    }

}
