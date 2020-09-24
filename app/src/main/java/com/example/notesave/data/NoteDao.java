package com.example.notesave.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.notesave.Utils.Contants;
import com.example.notesave.base.BaseApplication;
import com.example.notesave.bean.NoteData;

import java.util.ArrayList;
import java.util.List;

public class NoteDao implements INoteDao {

    private final NoteDBHelper mDBHelp;
    private INoteDaoCallback mNotDaoCallback = null;
    private static NoteDao mNoteDato;

    public static NoteDao getInstance(){
        if(mNoteDato == null){
            synchronized (NoteData.class){
                if(mNoteDato == null){
                    mNoteDato = new NoteDao();
                }
            }
        }
        return mNoteDato;
    }


    private NoteDao(){
        mDBHelp = new NoteDBHelper(BaseApplication.getContext());
    }


    @Override
    public void addData(NoteData data) {
        SQLiteDatabase db = null;
        boolean isSuccess = false;
        try{
            db = mDBHelp.getWritableDatabase();
            db.beginTransaction();;
            ContentValues values = new ContentValues();

//            values.put(Contants.DATA_ITEM_ID,data.getDataItemId());
            values.put(Contants.DATA_CONTENT,data.getContent());
            values.put(Contants.DATA_TITLE,data.getTitle());

            db.insert(Contants.DATA_TB_NAME,null,values);
            db.setTransactionSuccessful();
            isSuccess = true;
        }catch (Exception e){
            e.printStackTrace();
            isSuccess = false;
        }finally {
            if(db != null){
                db.endTransaction();
                db.close();
            }
            if(mNotDaoCallback != null){
                mNotDaoCallback.onAddResult(isSuccess);
            }
        }
    }

    @Override
    public void deleteData(NoteData data) {
        SQLiteDatabase db = null;
        boolean isDeleteSuccess = false;
        try{
            db = mDBHelp.getWritableDatabase();
            db.beginTransaction();
            int delete = db.delete(Contants.DATA_TB_NAME, Contants.DATA_ID + "=?",new String[]{data.getDataItemId() + ""});
            db.setTransactionSuccessful();
            isDeleteSuccess = true;
        }catch (Exception e){
            e.printStackTrace();
            isDeleteSuccess = false;
        }finally {
            if (db != null) {
                db.endTransaction();
                db.close();
            }
            if(mNotDaoCallback != null){
                mNotDaoCallback.onDelResult(isDeleteSuccess);
            }
        }
    }

    @Override
    public void listData() {
        SQLiteDatabase db = null;
        List<NoteData> noteDatas = new ArrayList<>();
        try {
            db = mDBHelp.getWritableDatabase();
            db.beginTransaction();
            Cursor cursor = db.query(Contants.DATA_TB_NAME,null,null,null,null,null,null);
            while (cursor.moveToNext()){
                NoteData noteData = new NoteData();
                int dataItemId = cursor.getInt(cursor.getColumnIndex(Contants.DATA_ID));
                noteData.setDataItemId(dataItemId);
                String dataContent = cursor.getString(cursor.getColumnIndex(Contants.DATA_CONTENT));
                noteData.setContent(dataContent);
                String dataTitle = cursor.getString(cursor.getColumnIndex(Contants.DATA_TITLE));
                noteData.setTitle(dataTitle);
                noteDatas.add(noteData);
            }
            cursor.close();
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db != null){
                db.endTransaction();
                db.close();
            }
            if(mNotDaoCallback != null){
                mNotDaoCallback.onNoteListLoad(noteDatas);
            }
        }

    }

    @Override
    public void setNoteDaoCallback(INoteDaoCallback callback) {
        this.mNotDaoCallback = callback;
    }

    @Override
    public void updateData(NoteData data) {
        SQLiteDatabase db = null;
        boolean isUpdateSuccess = false;
        try{
            db = mDBHelp.getWritableDatabase();
            db.beginTransaction();
            ContentValues values = new ContentValues();
            values.put(Contants.DATA_CONTENT,data.getContent());
            values.put(Contants.DATA_TITLE,data.getTitle());
            int update = db.update(Contants.DATA_TB_NAME,values,Contants.DATA_ID + "=?",new String[]{data.getDataItemId() + ""});
            isUpdateSuccess = true;
            db.setTransactionSuccessful();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(db != null){
                db.endTransaction();
                db.close();
            }
            if(mNotDaoCallback != null){
                mNotDaoCallback.onUpdateResult(isUpdateSuccess);
            }
        }
    }
}
