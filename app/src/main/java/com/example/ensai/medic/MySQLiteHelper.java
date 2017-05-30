package com.example.ensai.medic;

/**
 * Created by ensai on 19/05/17.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private String[] allColumnsMedic = { MySQLiteHelper.COLUMN_ID,MySQLiteHelper.COLUMN_Name,MySQLiteHelper.COLUMN_CIS,MySQLiteHelper.COLUMN_Peremption   };
    public static final String SAVE_CODE = "INSERT INTO code VALUES (NULL, ?, ?);";
    public static final String SEARCH_ALL_CODE= "SELECT * from code;";
    private String[] allColumns_code = {MySQLiteHelper.COLUMN_CIS,       MySQLiteHelper.COLUMN_CIP};
    public static final String TABLE_PHARMACIE ="pharmacie";
    public static final String SAVE_MEDIC = "INSERT INTO " + TABLE_PHARMACIE +" VALUES (NULL, ?, ?);";
    public static final String COLUMN_Name = "nom";
    private static final String DATABASE_NAME = "pharmacie";
    public static final String COLUMN_ID = "id";
    private static final int DATABASE_VERSION = 1;
    public static final String COLUMN_CIS = "cis";
    public static final String COLUMN_Peremption="peremption";
    public static final String TABLE_CODE ="code";
    public static final String COLUMN_CIP = "cip";
    public static final String TABLE_PERSONNES="personnes";
    public static final String TABLE_VACCINS="vaccins";
    public static final String COLUMN_VACCIN = "nom";

    // Commande sql pour la création de la base de données


    private static final String DATABASE_CREATE_PHARMACIE = "create table pharmacie(id integer primary key autoincrement, nom text not null, cis text not null,peremption text , FOREIGN KEY (cis) REFERENCES code(cis));";

//    private static final String DATABASE_CREATE_CODE="create table code(id integer primary key autoincrement,cis text not null, cip text not null);";
    private static final String DATABASE_CREATE_CODE="create table code(cis text not null, cip text primary key not null);";

    private static final String DATABASE_CREATE_PERSONNES="create table personnes(nom text  not null);";

    private static final String DATABASE_CREATE_VACCINS="create table vaccins(id integer primary key autoincrement,nom text , vaccin text , date text,realise integer);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.v("const","avt db");
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.v("INFO1","avt db");
        database.execSQL(DATABASE_CREATE_PHARMACIE);
        database.execSQL(DATABASE_CREATE_CODE);
        database.execSQL(DATABASE_CREATE_PERSONNES);
        database.execSQL(DATABASE_CREATE_VACCINS);
        Log.v("INFO1","creating db");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v("INFO1","onup");
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHARMACIE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CODE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONNES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VACCINS);
        onCreate(db);
    }

}
