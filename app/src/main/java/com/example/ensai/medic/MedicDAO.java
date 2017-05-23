package com.example.ensai.medic;

/**
 * Created by ensai on 19/05/17.
 */

import java.io.BufferedReader;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;



public class MedicDAO {
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID,MySQLiteHelper.COLUMN_Name,MySQLiteHelper.COLUMN_CIS,
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
                "cis"
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
                "cis"
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
        return medics;
    }
    private Medic cursorToMedic(Cursor cursor) {
        Medic medic = new Medic(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
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

