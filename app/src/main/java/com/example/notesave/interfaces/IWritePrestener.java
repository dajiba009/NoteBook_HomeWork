package com.example.notesave.interfaces;

import com.example.notesave.bean.NoteData;

public interface IWritePrestener {

    void register(IWriteCallback callback);

    void unregister(IWriteCallback callback);

    void saveData(NoteData noteData);

    void setCurrentNoteData(NoteData noteData);

    NoteData getCurrentNotData();

    void onUpdateData(NoteData data);

}
