package com.example.notesave.data;

import com.example.notesave.bean.NoteData;

public interface INoteDao {

    void addData(NoteData data);

    void deleteData(NoteData data);

    void listData();

    void setNoteDaoCallback(INoteDaoCallback callback);

    void updateData(NoteData data);
}
