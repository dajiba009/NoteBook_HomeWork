package com.example.notesave.interfaces;

import com.example.notesave.bean.NoteData;
import com.example.notesave.data.INoteDaoCallback;

public interface INotePresenter {

    void register(INoteCallback callback);

    void unregister(INoteCallback callback);

    void listNode();

    void deleteNode(NoteData noteData);

}
