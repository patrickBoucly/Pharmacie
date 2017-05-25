package com.example.ensai.medic;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ensai on 25/05/17.
 */

public class PersonnesDAO {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_Name
    };

    public PersonnesDAO(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
    public void add(String name){
        open();
        database.execSQL("INSERT INTO personnes(nom) VALUES('"+name+"');");
        close();
    }

    public List<String> getAllNames(){
        List<String> liste = new ArrayList<String>();
        open();
        String[] tableColumns = new String[] {
                "nom",
        };


        Cursor cursor = database.query("personnes", tableColumns, null, null,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String nom = cursorToNom(cursor);
            liste.add(nom);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return liste;
    }

    private String cursorToNom(Cursor cursor) {
        String nom = new String(cursor.getString(0));
        return nom;
    }






}
