package com.example.ensai.medic;

/**
 * Created by ensai on 19/05/17.
 */

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.ensai.medic.Medic;
import com.example.ensai.medic.MySQLiteHelper;


public class MedicDAO {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,MySQLiteHelper.COLUMN_Name,MySQLiteHelper.COLUMN_CIS,MySQLiteHelper.COLUMN_Peremption
    };

    public MedicDAO(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
    public void add(String name,String cis){
        open();

        database.execSQL("INSERT INTO pharmacie(nom,cis) VALUES('"+name+"','"+cis+"');");
        close();
    }
    public void add(String name,String cis,String peremption){
        open();
        database.execSQL("INSERT INTO pharmacie(nom,cis,peremption) VALUES('"+name+"','"+cis+"','"+peremption+"');");
        close();
    }
    public void update(Medic medic){
        open();
        /*
        ContentValues args = new ContentValues();
        args.put("name", medic.getName());
        args.put("cis", medic.getCodeCIS());
        args.put("peremption", medic.getPeremption());
        String name=medic.getName();
        database.update("pharmacie", args, "name" + "=" + '"+name+"', null);*/
        database.execSQL("UPDATE pharmacie SET peremption='"+medic.getPeremption()+"' where nom='"+medic.getName()+"';");
        close();
    }


    public void deleteMedicCIS(Medic medic) {
        String cis = medic.getCodeCIS();
        System.out.println("Medicament ayant le code CIS suivant effacé :" + cis);
        database.delete(MySQLiteHelper.TABLE_PHARMACIE, MySQLiteHelper.COLUMN_CIS
                + " = " + cis, null);
    }
    public Medic getMedicFromName(String name){
        open();
        Medic res;
        String[] tableColumns = new String[] {
                "id",
                "nom",
                "cis",
                "peremption"
        };
        String whereClause = "nom=?";
        String[] whereArgs = new String[] {
                name
        };
        Cursor cursor = database.query("pharmacie", tableColumns, whereClause, whereArgs,
                null, null, null);
        cursor.moveToFirst();
        res=cursorToMedic(cursor);
        cursor.close();
        close();
        return res;
    }
    public Medic getMedicFromCIS(String cis){
        Medic res;
        open();
        String[] tableColumns = new String[] {
                "id",
                "nom",
                "cis",
                "peremption"
        };
        String whereClause = "cis=?";
        String[] whereArgs = new String[] {
                cis
        };
        Cursor cursor = database.query("pharmacie", tableColumns, whereClause, whereArgs,
                null, null, null);
        cursor.moveToFirst();
        res=cursorToMedic(cursor);
        cursor.close();
        close();
        return res;
    }
    public List<Medic> getAllMedics() {
        open();
        List<Medic> medics = new ArrayList<Medic>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_PHARMACIE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Medic medic = cursorToMedic(cursor);
            medics.add(medic);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return medics;
    }
    private Medic cursorToMedic(Cursor cursor) {
        Medic medic = new Medic(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3));
        return medic;
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
    public void deleteMedicName(String name) {
        open();
        database.execSQL("DELETE from pharmacie where nom='"+name+"';");
        close();
    }

}