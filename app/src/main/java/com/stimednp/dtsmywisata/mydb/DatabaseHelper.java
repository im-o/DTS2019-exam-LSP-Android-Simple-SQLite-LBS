package com.stimednp.dtsmywisata.mydb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by rivaldy on 7/29/2019.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "db_wisata_kuliner";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE_WISATA = String.format("CREATE TABLE %s" +
                    "(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL," +
                    "%s TEXT NOT NULL)",
            DatabaseContract.TABLE_WISATA,
            DatabaseContract.WisataColumns._ID,
            DatabaseContract.WisataColumns.TITLE,
            DatabaseContract.WisataColumns.DESC,
            DatabaseContract.WisataColumns.DATE,
            DatabaseContract.WisataColumns.URLIMAGE,
            DatabaseContract.WisataColumns.COORLAT,
            DatabaseContract.WisataColumns.COORLNG

    );

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_WISATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.TABLE_WISATA);
        onCreate(db);
    }
}
