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
import android.widget.TextView;


import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import android.widget.Toast;

/**
 * Created by ensai on 11/05/17.
 */
public class AjouterMedic extends Activity implements View.OnClickListener,AdapterView.OnItemClickListener {
    private TextView tv2;
    private EditText tv3;
    private String text;
    private String denom;
    private Button bouton_ajouter;
    private Button bouton_rechercher;
    private ListView resultats;
    private ArrayList<Medic> medics= new ArrayList<Medic>();
    private MedicDAO medicDAO;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajoutermedic);
        tv3=(EditText) findViewById(R.id.tv3);
        bouton_ajouter = (Button) findViewById(R.id.bouton_ajouter);
        bouton_rechercher = (Button) findViewById(R.id.bouton_rechercher);
        resultats = (ListView) findViewById(R.id.resultats);
        bouton_rechercher.setOnClickListener(this);
        bouton_rechercher.setOnClickListener(this);
        Log.i("TEST1", tv3.getText().toString());
        medicDAO= new MedicDAO(this);
    }
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bouton_ajouter:
                break;
            case R.id.bouton_rechercher:
                Log.i("TEST1", tv3.getText().toString());
                String nom = tv3.getText().toString();
                String adresse = "https://open-medicaments.fr/api/v1/medicaments?query=" + nom;
                OkHttpClient okhttpClient = new OkHttpClient();
                Request myGetRequest = new Request.Builder()
                        .url(adresse)
                        .build();
                okhttpClient.newCall(myGetRequest).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                    }
                    @Override
                    public void onResponse(Response response) throws IOException {
                        //le retour est effectué dans un thread différent
                        try {
                            text = response.body().string();
                            JSONArray json = new JSONArray(text);
                            for (int i = 0; i < json.length(); i++) {
                                JSONObject jsonobject = json.getJSONObject(i);
                                denom = jsonobject.getString("denomination");
                                String code = ""+jsonobject.getInt("codeCIS");
                                medics.add(new Medic(1,denom,code));
                            }
                        } catch (JSONException exc) {

                            exc.printStackTrace();
                        }
                       runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ArrayList<String> medic= new ArrayList<String>();
                                for(Medic med: medics){
                                    medic.add(med.getName());
                                }
                                //TODO pb avec le tri aplhabetique: affichage correct mais selection erronnée ...
                                //tri alpabetique
                                // Collections.sort(medic);

                                final ArrayAdapter<String> adapter = new ArrayAdapter<String>(AjouterMedic.this,
                                        android.R.layout.simple_list_item_1, medic);
                                resultats.setAdapter(adapter);

                                resultats.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                                    public void onItemClick(AdapterView<?> arg0,View arg1, int position, long id){
                                            medicDAO.open();
                                            String name=medics.get(position).getName();
                                            Log.i("ici, name : ",name);
                                            String cis=medics.get(position).getCodeCIS().toString();
                                             Log.i("ici, cis : ",cis);
                                            Toast.makeText(getApplicationContext(), "cis: "+cis, Toast.LENGTH_LONG).show();
                                            medicDAO.add(name,cis);
                                            Intent n = new Intent(getApplicationContext(), MaPharma.class);
                                            startActivity(n);
                                    }
                                });
                            }
                        }); // fin runOnUiThread
                    } // fin onResponse
                });//fin Callback
                break;
            default:
                break;
        }
    }
    public void onItemClick(AdapterView<?> l, View v, int position, long id) {

    }


    public void vers_accueil (View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}