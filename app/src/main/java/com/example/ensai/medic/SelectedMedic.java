package com.example.ensai.medic;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ensai on 23/05/17.
 */

public class SelectedMedic extends Activity {
    TextView tv;
    Button sup;
    Button info;
    String monMedic="Médicament sélectionné : ";
    String nomMedic;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectedmedic);
        tv=(TextView) findViewById(R.id.selectedmedic);
        Intent intent = getIntent();
        String medic =intent.getExtras().getString("monmedic");
        nomMedic=medic;
        tv.setText(monMedic+medic);
        sup=(Button) findViewById(R.id.supprimer);
        info=(Button) findViewById(R.id.info);

    }

    public void suppresion(View v) {
        MedicDAO dao=new MedicDAO(this);
        dao.deleteMedicName(nomMedic);
    }
    public void information(View v) {
        String cis="61903804";
        String url="http://base-donnees-publique.medicaments.gouv.fr/affichageDoc.php?specid="+cis+"&typedoc=R";
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }
}
