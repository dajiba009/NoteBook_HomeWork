package com.example.notesave.interfaces;

import com.example.notesave.bean.NoteData;

import java.util.List;

public interface INoteCallback {

    void deleteNote(boolean isSuccess);

    void loadNote(List<NoteData> noteDatas);

}
