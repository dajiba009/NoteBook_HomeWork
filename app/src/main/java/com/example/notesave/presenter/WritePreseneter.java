package com.example.notesave.presenter;

import com.example.notesave.base.BaseApplication;
import com.example.notesave.bean.NoteData;
import com.example.notesave.data.INoteDaoCallback;
import com.example.notesave.data.NoteDao;
import com.example.notesave.interfaces.IWriteCallback;
import com.example.notesave.interfaces.IWritePrestener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class WritePreseneter implements IWritePrestener, INoteDaoCallback {

    private static WritePreseneter mWritePreseneter;
    private List<IWriteCallback> callbacks = new ArrayList<>();
    private final NoteDao mNoteDao;
    private NoteData mCurrentNoteData;

    private WritePreseneter(){
        mNoteDao = NoteDao.getInstance();
        mNoteDao.setNoteDaoCallback(this);
    }

    public static WritePreseneter getInstance(){
        if(mWritePreseneter == null){
            synchronized (WritePreseneter.class){
                if (mWritePreseneter == null) {
                    mWritePreseneter = new WritePreseneter();
                }
            }
        }
        return mWritePreseneter;
    }


    @Override
    public void register(IWriteCallback callback) {
        if(!callbacks.contains(callback)){
            callbacks.add(callback);
        }
    }

    @Override
    public void unregister(IWriteCallback callback) {
        callbacks.remove(callback);
    }

    @Override
    public void saveData(NoteData noteData) {
        //插入记录
        addData(noteData);
    }

    @Override
    public void setCurrentNoteData(NoteData noteData) {
        if(noteData != null){
            mCurrentNoteData = noteData;
        }else {
            mCurrentNoteData = null;
        }
    }

    @Override
    public NoteData getCurrentNotData() {
        if(mCurrentNoteData != null){
            return mCurrentNoteData;
        }else {
            return null;
        }
    }

    @Override
    public void onUpdateData(final NoteData data) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                if(mNoteDao != null){
                    synchronized (mNoteDao){
                        mNoteDao.updateData(data);
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    private void addData(final NoteData noteData){
        Observable.create(new ObservableOnSubscribe<Object>() {

            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                if(mNoteDao != null){
                    synchronized (mNoteDao){
                        mNoteDao.addData(noteData);
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    //===============================================================NoteDao的回调 start=============================================
    @Override
    public void onAddResult(final boolean isSuccess) {

            BaseApplication.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    for (IWriteCallback callback : callbacks) {
                    callback.onsaveResult(isSuccess);
                    }
                }
            });
    }

    @Override
    public void onDelResult(boolean isSuccess) {

    }

    @Override
    public void onNoteListLoad(List<NoteData> result) {
    }

    @Override
    public void onUpdateResult(boolean isSuccess) {

    }
    //===============================================================NoteDao的回调 end=============================================
}
