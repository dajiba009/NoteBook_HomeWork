package com.example.notesave.presenter;

import com.example.notesave.base.BaseApplication;
import com.example.notesave.bean.NoteData;
import com.example.notesave.data.INoteDaoCallback;
import com.example.notesave.data.NoteDao;
import com.example.notesave.interfaces.INoteCallback;
import com.example.notesave.interfaces.INotePresenter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class NotePresenter implements INotePresenter, INoteDaoCallback {

    private List<INoteCallback> mINoteCallbacks = new ArrayList<>();
    private static NotePresenter mNotePresenter;
    private final NoteDao mNoteDato;

    public static NotePresenter getInstance(){
        if(mNotePresenter == null){
            synchronized (NotePresenter.class){
                if(mNotePresenter == null){
                    mNotePresenter = new NotePresenter();
                }
            }
        }
        return mNotePresenter;
    }


    private NotePresenter(){
        mNoteDato = NoteDao.getInstance();
        mNoteDato.setNoteDaoCallback(this);
    }

    @Override
    public void register(INoteCallback callback) {
        if(!mINoteCallbacks.contains(callback)){
            mINoteCallbacks.add(callback);
        }
    }

    @Override
    public void unregister(INoteCallback callback) {
        mINoteCallbacks.remove(callback);
    }

    @Override
    public void listNode() {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                if(mNoteDato != null){
                    synchronized (mNoteDato){
                        mNoteDato.listData();
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void deleteNode(final NoteData noteData) {
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> emitter) throws Exception {
                if(mNoteDato != null){
                    synchronized (mNoteDato){
                        mNoteDato.deleteData(noteData);
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }


    @Override
    public void onAddResult(boolean isSuccess) {

    }

    @Override
    public void onDelResult(final boolean isSuccess) {
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                for (INoteCallback iNoteCallback : mINoteCallbacks) {
                    iNoteCallback.deleteNote(isSuccess);
                }
            }
        });
    }

    @Override
    public void onNoteListLoad(final List<NoteData> result) {
        BaseApplication.getHandler().post(new Runnable() {
            @Override
            public void run() {
                for (INoteCallback iNoteCallback : mINoteCallbacks) {
                    iNoteCallback.loadNote(result);
                }
            }
        });
    }

    @Override
    public void onUpdateResult(boolean isSuccess) {

    }
}
