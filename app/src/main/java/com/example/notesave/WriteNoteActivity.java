package com.example.notesave;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.notesave.bean.NoteData;
import com.example.notesave.interfaces.IWriteCallback;
import com.example.notesave.presenter.WritePreseneter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WriteNoteActivity extends AppCompatActivity implements View.OnClickListener, IWriteCallback {

    private static final String TAG = "WriteNoteActivity";
    private EditText mEditText;
    private View mEnsureTv;
    private View mCancelTv;
    private WritePreseneter mWritePreseneter;
    private NoteData mCurrentNoteData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setContentView(R.layout.activity_writenote);
        initPresenter();
        initView();
        iniEvent();
    }

    private void initPresenter() {
        mWritePreseneter = WritePreseneter.getInstance();
        mWritePreseneter.register(this);
        this.mCurrentNoteData = mWritePreseneter.getCurrentNotData();
    }

    private void iniEvent() {
        mEnsureTv.setOnClickListener(this);
        mCancelTv.setOnClickListener(this);
    }

    private void initView() {
        mEditText = findViewById(R.id.write_et);
        mEditText.setFocusable(true);
        mEditText.setFocusableInTouchMode(true);
        mEditText.requestFocus();
        if(mCurrentNoteData != null){
            mEditText.setText(mCurrentNoteData.getContent());
        }

        mEnsureTv = findViewById(R.id.ensure_tv);
        mCancelTv = findViewById(R.id.cancel_tv);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ensure_tv:
                writeToDB();
                finish();
                break;
            case R.id.cancel_tv:
                finish();
                break;
        }
    }

    private void writeToDB(){
        String text = mEditText.getText().toString();
        NoteData data = new NoteData();
        String title;
        char[] chars = text.toCharArray();
        if(chars.length > 10) {
            StringBuffer sbf = new StringBuffer();
            for (int i = 0; i < 10; i++) {
                sbf.append(chars[i]);
            }
            Log.d(TAG, "note's title ===> " + sbf);
            Log.d(TAG, "text ====> " + text);
            title = sbf.toString();
        }else {
            title = text;
        }

        if(mCurrentNoteData != null){
            mCurrentNoteData.setContent(text);
            mCurrentNoteData.setTitle(title);
            data = mCurrentNoteData;
            if(mWritePreseneter != null){
                //將notedata的序列id存進去
                mWritePreseneter.onUpdateData(data);
            }
        }else {
            data.setTitle(title);
            data.setContent(text);
            if(mWritePreseneter != null){
                //將notedata的序列id存進去
                mWritePreseneter.saveData(data);
            }
        }
    }

    @Override
    public void onsaveResult(boolean isSuccess) {
        Toast.makeText(this, "保存" + (isSuccess?"成功":"失败"), Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWritePreseneter != null) {
            mWritePreseneter.unregister(this);
        }
    }
}
