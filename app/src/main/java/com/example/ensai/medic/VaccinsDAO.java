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

public class VaccinsDAO {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_Name , MySQLiteHelper.COLUMN_Vaccin, MySQLiteHelper.COLUMN_Date   };

    public VaccinsDAO(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void add(String name, String vaccin, String date){
        open();
        database.execSQL("INSERT INTO vaccins(nom,vaccin,date) VALUES('"+name+"','"+vaccin+"','"+date+"');");
        close();
    }









    public List<String> getFromName(String nom){
        List<String> liste = new ArrayList<String>();
        open();
        String[] tableColumns = new String[] {
                "nom","vaccin", "date"
        };
        String whereClause = "nom=?";
        String[] whereArgs = new String[] {
                nom
        };

        Cursor cursor = database.query("vaccins", tableColumns, whereClause, whereArgs,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String vaccin = cursorToVaccin(cursor);
            liste.add(vaccin);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return liste;
    }

    private String cursorToVaccin(Cursor cursor) {
        String vaccin = new String(cursor.getString(1));
        String date = new String(cursor.getString(2));
        String vaccin_date=vaccin + " : " + date;
        return vaccin_date;
    }






}
