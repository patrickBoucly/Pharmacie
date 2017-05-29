package com.example.ensai.medic;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by ensai on 11/05/17.
 */
public class MaPharma extends Activity {
    Button bouton2 = null;
    ListView mes_medic;
    private MedicDAO medicDAO;
    private MonAdapter mesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapharma);
        mes_medic = (ListView) findViewById(R.id.mes_medic);
        medicDAO= new MedicDAO(this);
        medicDAO.open();

        List<Medic> values = medicDAO.getAllMedics();
        List<String> noms=new ArrayList<String>();
        int i=0;
        for(Medic val : values){
            noms.add(i,val.getName());
            i++;
        }
        ArrayAdapter adapter =new ArrayAdapter(this, android.R.layout.simple_list_item_1, noms);
        //MonAdapter adapter =new MonAdapter(values);

        mes_medic.setAdapter(adapter);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mes_medic.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    public void onItemClick(AdapterView<?> arg0,View arg1, int position, long id){

                        Intent n = new Intent(getApplicationContext(), MaPharma.class);
                        n.putExtra("position", position);
                        Log.i("Envoi",""+ mes_medic.getItemAtPosition(position).toString());
                        try {
                            Intent i = new Intent(getApplicationContext(), SelectedMedic.class);
                            i.putExtra("monmedic", mes_medic.getItemAtPosition(position).toString());

                            startActivity(i);
                        } catch(ActivityNotFoundException e) {
                            Log.i("erreur",""+e.getMessage());
                            Toast toast=Toast.makeText(getApplicationContext(), "GoogleMap non trouvé", Toast.LENGTH_LONG);
                            TextView v1 = (TextView) toast.getView().findViewById(android.R.id.message);
                            v1.setTextColor(Color.BLACK);
                            toast.show();

                        }
                        //test
                        // startActivity(n);
                    }
                });
            }
        }); // fin runOnUiThread











    }

    public void vers_accueil (View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



}
