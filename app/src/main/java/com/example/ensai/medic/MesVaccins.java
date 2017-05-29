package com.example.ensai.medic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by ensai on 11/05/17.
 */
public class MesVaccins extends Activity {
    private Button bouton2 = null;
    private Button bouton_valider2 = null;
    private  Button bouton_valider = null;
    private EditText editText=null;
    private ListView resultats_personnes;
    private PersonnesDAO personnesDAO;
    private Spinner spinner3=null;
    private String personneASupprimer=null;
    private String nom="";
    private List<String> personnes=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mesvaccins);
        personnesDAO= new PersonnesDAO(this);

        editText = (EditText) findViewById(R.id.personne);
        editText.setVisibility(View.INVISIBLE);
        bouton_valider= (Button) findViewById(R.id.valider_ajouter_personne);
        bouton_valider.setVisibility(View.INVISIBLE);

       bouton2 = (Button) findViewById(R.id.ajouter_personne);
       resultats_personnes = (ListView) findViewById(R.id.resultats_personnes);

        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner3.setVisibility(View.INVISIBLE);

        bouton_valider2= (Button) findViewById(R.id.valider_suppression_personne);
        bouton_valider2.setVisibility(View.INVISIBLE);

    // afficher la liste des personnes enregistrées:
         personnes= personnesDAO.getAllNames();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MesVaccins.this,
                android.R.layout.simple_list_item_1, personnes);
        resultats_personnes.setAdapter(adapter);

        // cliquer sur un nom amène à la page MesVaccinsDetail
        resultats_personnes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                Intent n = new Intent(getApplicationContext(), MesVaccins.class);
                n.putExtra("position", position);
                Log.i("Envoi", "" + resultats_personnes.getItemAtPosition(position).toString());
                Intent i = new Intent(getApplicationContext(), MesVaccinsDetail.class);
                i.putExtra("nom", resultats_personnes.getItemAtPosition(position).toString());
                startActivity(i);

            }
        });





    }

    public void ajouter_personne(View v) {

        editText.setVisibility(View.VISIBLE);
        bouton_valider.setVisibility(View.VISIBLE);

        Toast toast1=Toast.makeText(this, "Entrez le nom de la personne à ajouter: " , Toast.LENGTH_LONG);
        TextView v3 = (TextView) toast1.getView().findViewById(android.R.id.message);
        v3.setTextColor(Color.BLACK);
        toast1.show();



    }
    public void  valider_ajouter_personne(View v) {
        String nom = editText.getText().toString();
        Log.i("vaccins", nom);
        personnesDAO.open();
        personnesDAO.add(nom);
        personnesDAO.close();
        Toast toast2=Toast.makeText(this, "Ajout de:  "+nom  , Toast.LENGTH_LONG);
        TextView v3 = (TextView) toast2.getView().findViewById(android.R.id.message);
        v3.setTextColor(Color.BLACK);
        toast2.show();

        Intent intent = new Intent(this, MesVaccins.class);
        startActivity(intent);


    }







    public void supprimerPersonne(View v){

        spinner3.setVisibility(View.VISIBLE);
        bouton_valider2.setVisibility(View.VISIBLE);


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, personnes);

        // Drop down layout style - list view with radio button
        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner3.setAdapter(dataAdapter3);

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);
                personneASupprimer=item.toString();

            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    //TODO il faudrait aussi supprimer les vaccins de la personne supprimée (si on a le temps...)

    public void  validerSuppressionPersonne(View v) {

        // supprimer le vaccin dans la base
        personnesDAO.open();
        personnesDAO.deletePersonne(personneASupprimer);
        personnesDAO.close();

        Toast toast3=Toast.makeText(this, "Supprimons la personne "+personneASupprimer  , Toast.LENGTH_LONG);
        TextView v3 = (TextView) toast3.getView().findViewById(android.R.id.message);
        v3.setTextColor(Color.BLACK);
        toast3.show();

        Intent intent = new Intent(this, MesVaccins.class);
        startActivity(intent);

    }





    public void vers_accueil (View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
