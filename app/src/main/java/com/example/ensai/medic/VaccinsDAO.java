package com.example.ensai.medic;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ensai.medic.MySQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ensai on 25/05/17.
 */

public class VaccinsDAO {

    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;
    private String[] allColumns = { MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_Name , MySQLiteHelper.COLUMN_VACCIN, MySQLiteHelper.COLUMN_Peremption   };
    private String[] allColumns_vaccins = { "id","nom","vaccin","date","realise" };

    public VaccinsDAO(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void add(String name, String vaccin, String date,int fait){
        open();
        Log.i("cb dao",""+fait);
        database.execSQL("INSERT INTO vaccins(nom,vaccin,date,realise) VALUES('"+name+"','"+vaccin+"','"+date+"','"+fait+"');");
        close();
    }
    public void add(Vaccin v){
        open();
        database.execSQL("INSERT INTO vaccins(nom,vaccin,date,realise) VALUES('"+v.getIndividu()+"','"+v.getdenomination()+"','"+v.getDate()+"','"+v.getRealise()+"');");
        close();
    }

    public void update(Vaccin v){
        open();
        database.execSQL("UPDATE vaccins SET realise='"+v.getRealise()+"' where nom='"+v.getIndividu()+"' and vaccin='"+v.getdenomination()+"' and date='"+v.getDate()+"';");
        close();
    }
    public void updateDate(Vaccin v) {
        open();
        database.execSQL("UPDATE vaccins SET date='"+v.getDate()+"' where nom='"+v.getIndividu()+"' and vaccin='"+v.getdenomination()+"' and realise='"+v.getRealise()+"';");
        close();
    }




    public List<Vaccin> getAllVaccins() {
        open();
        List<Vaccin> vaccins = new ArrayList<Vaccin>();

        Cursor cursor = database.query(MySQLiteHelper.TABLE_VACCINS,
                allColumns_vaccins, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Vaccin v = cursorToVaccin(cursor);
            vaccins.add(v);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return vaccins;
    }
    public Vaccin cursorToVaccin(Cursor cursor) {
        Log.i("curs av:",""+cursor.getInt(4));
        boolean fait = (cursor.getInt(4) > 0);
        Log.i("curs:",""+fait);
        Vaccin v = new Vaccin(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getInt(4));
        return v;
    }





    public List<Vaccin> getFromName(String individu){
        List<Vaccin> liste = new ArrayList<Vaccin>();
        open();
        String[] tableColumns = new String[] {"id",
                "nom","vaccin", "date","realise"
        };
        String whereClause = "nom=?";
        String[] whereArgs = new String[] {
                individu
        };

        Cursor cursor = database.query("vaccins", tableColumns, whereClause, whereArgs,
                null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Vaccin vaccin = cursorToVaccin(cursor);
            liste.add(vaccin);
            cursor.moveToNext();
        }
        cursor.close();
        close();
        return liste;
    }
/*
    private String cursorToVaccin(Cursor cursor) {
        String vaccin = new String(cursor.getString(1));
        String date = new String(cursor.getString(2));
        String vaccin_date=vaccin + " : " + date;
        return vaccin_date;
    }
*/
    public void deleteVaccin(String vaccin, String nom) {
        open();
        database.delete(MySQLiteHelper.TABLE_VACCINS,"vaccin=? and nom=?",new String[]{vaccin,nom});
        close();

    }



}
