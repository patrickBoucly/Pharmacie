package com.example.ensai.medic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by ensai on 23/05/17.
 */

public class SelectedMedic extends Activity {
    TextView tv;
    Button sup;
    Button info;
    String monMedic="Médicament sélectionné : ";
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectedmedic);
        tv=(TextView) findViewById(R.id.selectedmedic);
        Intent intent = getIntent();
        String medic =intent.getExtras().getString("monmedic");
        tv.setText(monMedic+medic);
        sup=(Button) findViewById(R.id.supprimer);
        info=(Button) findViewById(R.id.info);

    }
}
