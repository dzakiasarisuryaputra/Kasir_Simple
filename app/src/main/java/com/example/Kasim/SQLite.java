package com.example.Kasim;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SQLite extends SQLiteOpenHelper {


//    private SQLiteDatabase db;

    public SQLite(@Nullable Context context){
        super(context, "db_kasim.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("create table invoice (namaPembeli TEXT,nama TEXT,harga integer,jumlah integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int i, int ii) {
        DB.execSQL("drop table if exists db_kasim.db");
    }

    public  Boolean insertData(String namaPembeli,String nama,int harga,int jumlah){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("namaPembeli",namaPembeli);
        contentValues.put("nama",nama);
        contentValues.put("harga",harga);
        contentValues.put("jumlah",jumlah);
        long result = DB.insert("invoice",null,contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor getData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from invoice",null);
        return cursor;
    }

}
