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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ensai on 25/05/17.
 */

public class MesVaccinsDetail extends Activity {
    private Button bouton2 = null;
    private  Button bouton_valider = null;
    private  Button bouton_valider2 = null;
    private EditText editText=null;
    private ListView resultats_vaccins;
    private VaccinsDAO vaccinsDAO;
    private String nom="";
    private  TextView test =null ;
    private Spinner spinner=null;
    private Spinner spinner2=null;
    private String vaccinAjoute=null;
    private String vaccinASupprimer=null;
    private DatePicker datePicker=null;
    private CheckBox cb;
    private Boolean check;
    private List<Vaccin> vaccins;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mesvaccinsdetail);
        Intent intent = getIntent();
        nom =intent.getExtras().getString("nom");

        TextView textView = (TextView) findViewById(R.id.ecran_detail);
        textView.setText(nom);


        bouton_valider= (Button) findViewById(R.id.valider_ajouter_vaccin);
        bouton_valider.setVisibility(View.INVISIBLE);
        bouton_valider2= (Button) findViewById(R.id.valider_suppression_vaccin);
        bouton_valider2.setVisibility(View.INVISIBLE);
        bouton_valider2= (Button) findViewById(R.id.valider_suppression_vaccin);
        cb = (CheckBox) findViewById(R.id.checkbox);
        check=cb.isChecked();
        resultats_vaccins = (ListView) findViewById(R.id.resultats_vaccins);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        datePicker.setVisibility(View.INVISIBLE);

        vaccinsDAO= new VaccinsDAO(this);



        // afficher la liste des personnes enregistrées:
        vaccins = vaccinsDAO.getFromName(nom);
        List<String> message=new ArrayList<String>();
        for(Vaccin v:vaccins){
            Log.i("getrea:",""+v.getRealise());
            String fait=" vaccination à faire pour le ";
            if(v.getRealise()==1){
                fait=" vaccination faite le ";
            }
            message.add(v.getdenomination()+" :"+fait+"   "+v.getDate());
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(MesVaccinsDetail.this,
                android.R.layout.simple_list_item_1, message);
        resultats_vaccins.setAdapter(adapter);


        resultats_vaccins.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                Intent n = new Intent(getApplicationContext(), ModifierVaccin.class);
                n.putExtra("ind", vaccins.get(position).getIndividu());
                n.putExtra("date", vaccins.get(position).getDate());
                n.putExtra("denom", vaccins.get(position).getdenomination());
                n.putExtra("fait", vaccins.get(position).getRealise());
                Log.i("Envoi", "" + resultats_vaccins.getItemAtPosition(position).toString());
                startActivity(n);
                finish();

            }
        });

        // Spinner element
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setVisibility(View.INVISIBLE);

        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setVisibility(View.INVISIBLE);



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

        Toast toast=Toast.makeText(this, "Entrez le nom du vaccin à ajouter:", Toast.LENGTH_LONG);
        TextView v2 = (TextView) toast.getView().findViewById(android.R.id.message);
        v2.setTextColor(Color.BLACK);
        toast.show();

    }

    public void  valider_ajouter_vaccin(View v) {
        String vaccin=vaccinAjoute;

        // get the values for day of month , month and year from a date picker
        String day =  ""+datePicker.getDayOfMonth();
        String month = ""+(datePicker.getMonth()+1) ;
        String year = ""+datePicker.getYear();
        String date= day+"/"+month+"/"+year;

        // ajouter le vaccin dans la base
        vaccinsDAO.open();
        Log.i("cb",""+check);
        int i=0;
        if(check){
            i=1;
        }
        vaccinsDAO.add(nom, vaccin, date,i);
        vaccinsDAO.close();

        Toast toast2=Toast.makeText(this, "Ajoutons le vaccin "+vaccin + " pour: "+nom + " à la date du " +date, Toast.LENGTH_LONG);
        TextView v2 = (TextView) toast2.getView().findViewById(android.R.id.message);
        v2.setTextColor(Color.BLACK);
        toast2.show();

        Intent intent = new Intent(this, MesVaccinsDetail.class);
        intent.putExtra("nom", nom);
        startActivity(intent);
        finish();

    }
public void onCheckboxClicked(View v){
        check=cb.isChecked();
}

public void supprimerVaccin(View v){

    spinner2.setVisibility(View.VISIBLE);
    bouton_valider2.setVisibility(View.VISIBLE);
    List<Vaccin> listeVaccins = new ArrayList<Vaccin>();
    listeVaccins=vaccinsDAO.getFromName( nom);
    List<String> noms=new ArrayList<String>();
    for(Vaccin vac:listeVaccins){

        noms.add(vac.getdenomination());
    }
    // Creating adapter for spinner
    ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, noms);

    // Drop down layout style - list view with radio button
    dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    // attaching data adapter to spinner
    spinner2.setAdapter(dataAdapter2);

    spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            Object item = parent.getItemAtPosition(pos);
            String vaccinAvecDateASupprimer= item.toString();
            //extraire uniquement le nom du vaccin daté
            int longueurChaine=vaccinAvecDateASupprimer.length();
            Log.i("vaccin supp", vaccinAvecDateASupprimer);
            //vaccinASupprimer=vaccinAvecDateASupprimer.substring(0, longueurChaine-12);
            vaccinASupprimer=vaccinAvecDateASupprimer;
            Log.i("vaccin supp2", vaccinASupprimer);

        }
        public void onNothingSelected(AdapterView<?> parent) {
        }
    });

}


    public void  validerSuppressionVaccin(View v) {

        // supprimer le vaccin dans la base
        vaccinsDAO.open();
        vaccinsDAO.deleteVaccin(vaccinASupprimer,nom);
        vaccinsDAO.close();

        Toast toast3=Toast.makeText(this, "Supprimons le vaccin "+vaccinASupprimer + " pour: "+nom , Toast.LENGTH_LONG);
        TextView v3 = (TextView) toast3.getView().findViewById(android.R.id.message);
        v3.setTextColor(Color.BLACK);
        toast3.show();

        Intent intent = new Intent(this, MesVaccinsDetail.class);
        intent.putExtra("nom", nom);
        startActivity(intent);
        finish();

    }

    public void vers_accueil (View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }





}