package com.example.notesave.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.notesave.R;
import com.example.notesave.bean.NoteData;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.InnerHold> {

    private static final String TAG = "NoteListAdapter";
    private List<NoteData> mNoteDataList = new ArrayList<>();
    private OnNoteItemOnClick mOnNoteItemOnClick = null;

    @NonNull
    @Override
    public NoteListAdapter.InnerHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);
        InnerHold hold = new InnerHold(view);
        return hold;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteListAdapter.InnerHold holder, final int position) {
        holder.itemView.setTag(position);
        TextView itmeTitleTv = holder.itemView.findViewById(R.id.itme_title);
        itmeTitleTv.setText(mNoteDataList.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnNoteItemOnClick != null){
                    mOnNoteItemOnClick.clickItem(mNoteDataList.get(position));
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnNoteItemOnClick != null) {
                    mOnNoteItemOnClick.longClickItem(mNoteDataList.get(position));
                }
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNoteDataList.size();
    }

    class InnerHold extends  RecyclerView.ViewHolder{

        public InnerHold(@NonNull View itemView) {
            super(itemView);
        }
    }

    public void setNoteDataList(List<NoteData> noteDatas){
        if(noteDatas != null){
            mNoteDataList.clear();
            Log.d(TAG,"noteDatas size ===> " + noteDatas.size());
            this.mNoteDataList.addAll(noteDatas);
        }
        notifyDataSetChanged();
    }

    public void setOnNoteItemOnClick(OnNoteItemOnClick onNoteItemOnClick){
        this.mOnNoteItemOnClick = onNoteItemOnClick;
    }

    public interface OnNoteItemOnClick{
        void clickItem(NoteData noteData);
        void longClickItem(NoteData noteData);
    }
}
