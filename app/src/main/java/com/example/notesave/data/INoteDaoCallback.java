package com.example.notesave.data;

import com.example.notesave.bean.NoteData;

import java.util.List;

public interface INoteDaoCallback {

    void onAddResult(boolean isSuccess);

    void onDelResult(boolean isSuccess);

    void onNoteListLoad(List<NoteData> result);

    void onUpdateResult(boolean isSuccess);
}
