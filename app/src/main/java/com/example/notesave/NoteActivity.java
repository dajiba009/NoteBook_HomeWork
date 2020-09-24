package com.example.notesave;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.notesave.adapter.NoteListAdapter;
import com.example.notesave.bean.NoteData;
import com.example.notesave.interfaces.INoteCallback;
import com.example.notesave.interfaces.IWriteCallback;
import com.example.notesave.presenter.NotePresenter;
import com.example.notesave.presenter.WritePreseneter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.notesave.base.BaseApplication.getContext;

public class NoteActivity extends AppCompatActivity implements View.OnClickListener, NoteListAdapter.OnNoteItemOnClick, IWriteCallback, INoteCallback {


    private static final String TAG = "NoteActivity";
    private WritePreseneter mWritePreseneter;
    private RecyclerView mRecyclerView;
    private TextView mAddTv;
    private NoteListAdapter mAdapter;
    private List<NoteData> mNoteDataList = new ArrayList<>();
    private NotePresenter mNotePresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        register();
//        initData();
        initView();
        initEvent();
    }

    private void register() {
        mWritePreseneter = WritePreseneter.getInstance();
        mWritePreseneter.register(this);

        mNotePresenter = NotePresenter.getInstance();
        mNotePresenter.register(this);
    }

    private void initData() {
//        for(int i=0;i<10;i++){
//            NoteData noteData = new NoteData();
//            noteData.setTitle("Tiltle + " +i);
//            mNoteDataList.add(noteData);
//        }
        if(mNotePresenter != null){
            mNotePresenter.listNode();
        }
    }

    private void initEvent() {
        mAddTv.setOnClickListener(this);
    }

    private void initView() {
        mAddTv = findViewById(R.id.add_tv);
        mRecyclerView = findViewById(R.id.note_recycleview);
        mAdapter = new NoteListAdapter();
        mAdapter.setOnNoteItemOnClick(this);
        mAdapter.setNoteDataList(mNoteDataList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //每次onResume的时候访问一次数据库
        if(mNotePresenter != null){
            mNotePresenter.listNode();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_tv :
                //当新增的时候将当前的NoteData设置为空
                mWritePreseneter.setCurrentNoteData(null);
                Intent intent = new Intent(this,WriteNoteActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void clickItem(NoteData noteData) {
        Log.d(TAG,"點擊");
        if(mWritePreseneter != null){
            mWritePreseneter.setCurrentNoteData(noteData);
        }
        Intent intent = new Intent(this,WriteNoteActivity.class);
        startActivity(intent);
    }

    @Override
    public void longClickItem(final NoteData noteData) {
        Log.d(TAG,"長按");
        final String[] items={"删除"};
        android.app.AlertDialog.Builder listDialog = new android.app.AlertDialog.Builder(this);
        listDialog.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if(mNotePresenter != null){
                            mNotePresenter.deleteNode(noteData);
                        }
                        break;
                }
            }
        });
        listDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mWritePreseneter != null){
            mWritePreseneter.unregister(this);
        }
    }

    @Override
    public void onsaveResult(boolean isSuccess) {

    }

    @Override
    public void deleteNote(boolean isSuccess) {
        if(isSuccess){
            if(mNotePresenter != null){
                mNotePresenter.listNode();
            }
        }
    }

    @Override
    public void loadNote(List<NoteData> noteDatas) {
        mAdapter.setNoteDataList(noteDatas);
        mAdapter.notifyDataSetChanged();
    }
}
