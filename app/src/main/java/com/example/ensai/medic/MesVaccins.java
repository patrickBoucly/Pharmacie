package com.example.ensai.medic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ensai.medic.DAO.PersonnesDAO;

import java.util.List;

/**
 * Created by ensai on 11/05/17.
 */
public class MesVaccins extends Activity {
    private Button bouton2 = null;
    private  Button bouton_valider = null;
    private EditText editText=null;
    private ListView resultats_personnes;
    private PersonnesDAO personnesDAO;


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

    // afficher la liste des personnes enregistrées:
        List<String> personnes = personnesDAO.getAllNames();
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
        Toast.makeText(this, "Entrez le nom de la personne à ajouter", Toast.LENGTH_LONG).show();

    }
    public void  valider_ajouter_personne(View v) {
        String nom = editText.getText().toString();
        Log.i("vaccins", nom);
        personnesDAO.open();
        personnesDAO.add(nom);
        personnesDAO.close();
        Toast.makeText(this, "Ajoutons la personne "+nom, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MesVaccins.class);
        startActivity(intent);


    }

    public void vers_accueil (View v) {
        Toast.makeText(this, "Retour à l'écran d'accueil", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
