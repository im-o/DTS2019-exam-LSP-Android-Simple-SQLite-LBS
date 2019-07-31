package com.stimednp.dtsmywisata.mydb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.stimednp.dtsmywisata.Wisatas;

import java.util.ArrayList;

import static android.provider.MediaStore.Audio.Playlists.Members._ID;
import static com.stimednp.dtsmywisata.mydb.DatabaseContract.TABLE_WISATA;
import static com.stimednp.dtsmywisata.mydb.DatabaseContract.WisataColumns.COORLAT;
import static com.stimednp.dtsmywisata.mydb.DatabaseContract.WisataColumns.COORLNG;
import static com.stimednp.dtsmywisata.mydb.DatabaseContract.WisataColumns.DATE;
import static com.stimednp.dtsmywisata.mydb.DatabaseContract.WisataColumns.DESC;
import static com.stimednp.dtsmywisata.mydb.DatabaseContract.WisataColumns.TITLE;
import static com.stimednp.dtsmywisata.mydb.DatabaseContract.WisataColumns.URLIMAGE;

/**
 * Created by rivaldy on 7/29/2019.
 */

public class WisataHelper {
    private static final String DATABASE_TABLE = TABLE_WISATA;
    private static DatabaseHelper databaseHelper;
    private static WisataHelper INSTANCE;
    private static SQLiteDatabase database;

    private WisataHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static WisataHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new WisataHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    //open and close connetion db
    public void open() throws SQLException {
        database = databaseHelper.getWritableDatabase();
    }

    public void close() {
        databaseHelper.close();
        if (database.isOpen())
            database.close();
    }

    //CRUD
    public ArrayList<Wisatas> getAllWisata() {
        ArrayList<Wisatas> arrayList = new ArrayList<>();
        Cursor cursor = database.query(DATABASE_TABLE, null, null, null, null, null,
                _ID + " ASC", null);
        cursor.moveToFirst();
        Wisatas wisatas;
        if (cursor.getCount() > 0) {
            do {
                wisatas = new Wisatas();
                wisatas.setId(cursor.getInt(cursor.getColumnIndexOrThrow(_ID)));
                wisatas.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
                wisatas.setDesc(cursor.getString(cursor.getColumnIndexOrThrow(DESC)));
                wisatas.setDate(cursor.getString(cursor.getColumnIndexOrThrow(DATE)));
                wisatas.setUrl_image(cursor.getString(cursor.getColumnIndexOrThrow(URLIMAGE)));
                wisatas.setCoor_latitude(cursor.getString(cursor.getColumnIndexOrThrow(COORLAT)));
                wisatas.setCoor_longitude(cursor.getString(cursor.getColumnIndexOrThrow(COORLNG)));
                arrayList.add(wisatas);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }
        cursor.close();
        return arrayList;
    }

    //save data
    public long insertWisata(Wisatas wisatas) {
        ContentValues args = new ContentValues();
        args.put(TITLE, wisatas.getTitle());
        args.put(DESC, wisatas.getDesc());
        args.put(DATE, wisatas.getDate());
        args.put(URLIMAGE, wisatas.getUrl_image());
        args.put(COORLAT, wisatas.getCoor_latitude());
        args.put(COORLNG, wisatas.getCoor_longitude());
        return database.insert(DATABASE_TABLE, null, args);
    }

    //update
    public int updateWisata(Wisatas wisatas) {
        ContentValues args = new ContentValues();
        args.put(TITLE, wisatas.getTitle());
        args.put(DESC, wisatas.getDesc());
        args.put(DATE, wisatas.getDate());
        args.put(URLIMAGE, wisatas.getUrl_image());
        args.put(COORLNG, wisatas.getCoor_longitude());
        args.put(COORLAT, wisatas.getCoor_latitude());
        return database.update(DATABASE_TABLE, args, _ID + "= '" + wisatas.getId() + "'", null);
    }

    //delete
    public int deleteWisata(int id) {
        return database.delete(TABLE_WISATA, _ID + "= '" + id + "'", null);
    }
}
