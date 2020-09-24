package com.example.notesave.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;

import com.example.notesave.Utils.Contants;

import androidx.annotation.Nullable;

public class NoteDBHelper extends SQLiteOpenHelper {


    public NoteDBHelper(@Nullable Context context) {
        super(context, Contants.DB_NAME, null, Contants.DB_VERSION_COD);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String noteTabSql = "create table " + Contants.DATA_TB_NAME + "( " +
                Contants.DATA_ID+" integer primary key autoincrement,"  +
                Contants.DATA_ITEM_ID + " integer,"+
                Contants.DATA_CONTENT+" varchar," +
                Contants.DATA_TITLE+" varchar"+
                ")";
        db.execSQL(noteTabSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
