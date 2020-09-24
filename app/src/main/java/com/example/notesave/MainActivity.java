package com.example.notesave;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.notesave.base.BaseApplication;

public class MainActivity extends AppCompatActivity {


    private static final String NOTE = "node";
    private static final String NOTE_STATE = "node_state";
    private boolean isOpende = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = BaseApplication.getContext().getSharedPreferences(NOTE, Context.MODE_PRIVATE);
        isOpende = sharedPreferences.getBoolean(NOTE_STATE,false);

        if(!isOpende){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(NOTE_STATE,true);
            editor.commit();
            BaseApplication.getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this,NoteActivity.class);
                    startActivity(intent);
                    finish();
                }
            },5000);
        }else {
            Intent intent = new Intent(MainActivity.this,NoteActivity.class);
            startActivity(intent);
            finish();
        }


//        BaseApplication.getHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(MainActivity.this,NoteActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        },5000);
    }
}