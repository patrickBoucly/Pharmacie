package com.example.ensai.medic;

/**
 * Created by ensai on 18/05/17.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

// on importe les classes IntentIntegrator et IntentResult de la librairie zxing

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.facebook.stetho.inspector.network.PrettyPrinterDisplayType.JSON;

public class Scan extends Activity implements  View.OnClickListener,AdapterView.OnItemClickListener {
    private String text;
    private String denom;
    private String cis;
    private ArrayList<Medic> medics= new ArrayList<Medic>();
    private ListView resultats_scan;
   // private MedicDAO medicDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan);
        resultats_scan = (ListView) findViewById(R.id.resultats_scan);

        //



        //



        AssetManager mngr=this.getAssets();

        Log.i("ini" ,"en cours");

        CodeDAO dao=new CodeDAO(this);


        if(dao.getAll().size()==0) {
            (Toast.makeText(getApplicationContext(), "Initialisation du scanner: veuillez patienter quelques minutes", Toast.LENGTH_LONG)).show();
            try {
                InputStream iS = mngr.open("CIS_CIP.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(iS));
                List<String> lignes = readLines(reader);
                for (String line : lignes) {
                        String cis=line.substring(0, 8);
                        String cip=line.substring(9);
                        dao.add(new Code(cis,cip));
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Button mybutton = (Button) findViewById(R.id.scan_button);
            mybutton.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.scan_button){

// on lance le scanner au clic sur notre bouton
            new IntentIntegrator(this).initiateScan();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

// nous utilisons la classe IntentIntegrator et sa fonction parseActivityResult pour parser le résultat du scan
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanningResult != null) {

// nous récupérons le contenu du code barre
            String scanContent = scanningResult.getContents();

// nous récupérons le format du code barre
            String scanFormat = scanningResult.getFormatName();

            TextView scan_format = (TextView) findViewById(R.id.scan_format);
            TextView scan_content = (TextView) findViewById(R.id.scan_content);

// nous affichons le résultat dans nos TextView
            CodeDAO dao =new CodeDAO(this);

            cis=dao.getCIS(scanContent);
            scan_format.setText("FORMAT: " + scanFormat);
            scan_content.setText("CIS: " + cis);


            //faire appel à okhttp pour recuperer le nom du medicament

            String adresse = "https://open-medicaments.fr/api/v1/medicaments/"+ cis;
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
                        Log.i("test text", text);
                        JSONObject json = new JSONObject(text);
                        denom = json.getString("denomination");
                        Log.i("test denom", denom);
                        medics.add(new Medic(1,denom,cis));

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
                            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(Scan.this,
                                    android.R.layout.simple_list_item_1, medic);
                            resultats_scan.setAdapter(adapter);

                            resultats_scan.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                                public void onItemClick(AdapterView<?> arg0,View arg1, int position, long id){
                                    MedicDAO medicDAO= new MedicDAO(getApplicationContext());
                                    medicDAO.add(denom,cis);
                                    Intent n = new Intent(getApplicationContext(), MaPharma.class);
                                    startActivity(n);
                                }
                            });
                        }
                    }); // fin runOnUiThread
                } // fin onResponse
            });








        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Aucune donnée reçu!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//noinspection SimplifiableIfStatement
       // if (id == R.id.action_settings) {
            return true;
        //}

       // return super.onOptionsItemSelected(item);
    }



    public static List<String> readLines(BufferedReader reader) throws Exception {

        List<String> results = new ArrayList<String>();
        String line = reader.readLine();
        while (line != null) {
            results.add(line);
            line = reader.readLine();
        }
        return results;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}