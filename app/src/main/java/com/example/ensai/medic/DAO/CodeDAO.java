package com.example.ensai.medic.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.ensai.medic.Code;
import com.example.ensai.medic.outils.MySQLiteHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static com.example.ensai.medic.outils.MySQLiteHelper.TABLE_CODE;

/**
 * Created by ensai on 22/05/17.
 */

public class CodeDAO {
    public static final String SAVE_CODE = "INSERT INTO code VALUES (NULL, ?, ?);";
    public static final String SEARCH_ALL_CODE= "SELECT * from code;";
    // Champs de la base de données
    private SQLiteDatabase db;
    private MySQLiteHelper dbHelper;
    public CodeDAO(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }
    public void close() {
        dbHelper.close();
    }



    public void add(Code code){

        open();
        ContentValues values = new ContentValues();
        values.put("cis", code.getCis());
        values.put("cip", code.getCip());
// Inserting Row
        db.insert(TABLE_CODE, null, values);
        close(); // Closing database connection
    }



   /* public void addAll(List<Code>  listeCode){
        open();
        ContentValues values = new ContentValues();
        for(int i = 1; i <= listeCode.size(); i++){
           Code code=listeCode.get(i);
            values.put("cis", code.getCis());
            values.put("cip", code.getCip());
            db.insert(TABLE_CODE, null, values);
        }
        close();
    }*/


    public void addAll(List<Code>  listeCode) {
        String sql = "INSERT INTO "+ TABLE_CODE +" VALUES (?,?);";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        for (int i = 0; i<listeCode.size(); i++) {
            Code code=listeCode.get(i);
            statement.clearBindings();
            statement.bindString(1, code.getCis());
            statement.bindString(2, code.getCip());
            statement.execute();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }



    public String getCIS(String cip){
       // open();
        String res="";
        String req="SELECT cis from code where cip="+cip;
        Cursor cursor = db.rawQuery(req, null);
        if (cursor.moveToFirst()) {
            do {
                res=cursor.getString(0);
            } while (cursor.moveToNext());
        }
       // close();
        return res;
    }
    public List<Code> getAll(){
        List<Code> codeList = new ArrayList<Code>();
        open();
        Cursor cursor = db.rawQuery(SEARCH_ALL_CODE, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Code code = new Code(cursor.getString(0),cursor.getString(1));

                codeList.add(code);
            } while (cursor.moveToNext());
        }
       // close();
        return codeList;
    }
    public void initialize(AssetManager mngr){



        try {
            InputStream iS = mngr.open("CIS_CIP.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(iS));
            List<String> lignes=readLines(reader);
           for(String line:lignes){
                this.add(new Code(line.substring(0,8),line.substring(9)));
            }
        }catch (IOException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
//        Log.i("test" ,""+this.getAll().size());
       // Log.i("test get cis " ,""+this.getCIS("3400935510259"));
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
}
