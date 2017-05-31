package com.example.ensai.medic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

/**
 * Created by ensai on 30/05/17.
 */

public class ModifierVaccin extends Activity {
    private String date;
    private String ind;
    private String denom;
    private int fait;
    private Button modifiDate;
    private Button modifiEtat;
    private Button valider;
    private VaccinsDAO dao=new VaccinsDAO(this);
    private DatePicker dp;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifier_vaccin);
        modifiDate=(Button) findViewById(R.id.modif_date);
        modifiEtat=(Button) findViewById(R.id.modif_etat);
        valider=(Button) findViewById(R.id.valider_date);
        valider.setVisibility(View.INVISIBLE);
        dp=(DatePicker) findViewById(R.id.datePicker2);
        dp.setVisibility(View.INVISIBLE);
        Intent intent = getIntent();


        date=intent.getExtras().getString("date");
        ind=intent.getExtras().getString("ind");
        denom=intent.getExtras().getString("denom");
        fait=intent.getExtras().getInt("fait");


    }

    public void modifierEtat(View v) {
        int newEtat=0;
        if(fait==0){
            newEtat=1;
        }
        dao.update(new Vaccin(ind,denom,date,newEtat));
        Intent intent = new Intent(this, MesVaccinsDetail.class);
        intent.putExtra("nom",ind);
        startActivity(intent);
        finish();
    }
    public void modifierDate(View v) {
        dp.setVisibility(View.VISIBLE);
        valider.setVisibility(View.VISIBLE);

    }
    public void valider(View v) {
        String day =  ""+dp.getDayOfMonth();
        String month = ""+(dp.getMonth()+1) ;
        String year = ""+dp.getYear();
        date= day+"/"+month+"/"+year;
        dao.updateDate(new Vaccin(ind,denom,date,fait));
        Intent intent = new Intent(this, MesVaccinsDetail.class);
        intent.putExtra("nom",ind);
        startActivity(intent);
        finish();
    }

    public void vers_accueil (View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
