package com.example.ensai.medic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ensai.medic.DAO.MedicDAO;

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
        Intent i=new Intent(this,MaPharma.class);
        startActivity(i);
    }
    public void information(View v) {
        MedicDAO dao=new MedicDAO(this);
        Medic m=dao.getMedicFromName(nomMedic);
        Log.i("monId",""+m.getIdMedic());
        Log.i("monCIS ", m.getCodeCIS());
        Log.i("monName ", m.getName());
        String url="http://base-donnees-publique.medicaments.gouv.fr/affichageDoc.php?specid="+ m.getCodeCIS()+"&typedoc=R";
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }


    public void vers_accueil (View v) {
        Toast toast=Toast.makeText(this, "Retour à l'écran d'accueil", Toast.LENGTH_LONG);
        TextView v2 = (TextView) toast.getView().findViewById(android.R.id.message);
        v2.setTextColor(Color.BLACK);
        toast.show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
