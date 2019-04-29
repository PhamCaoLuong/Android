package com.luong.note;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{
    private ItemClickListener clickListener;
    ArrayList<NoteItem> noteItems;
    Context context;
    DatabaseHelper databaseHelper;

    public NoteAdapter(ArrayList<NoteItem> noteItems, Context context) {
        this.noteItems = noteItems;
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_note, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( ViewHolder viewHolder, int i) {
        viewHolder.place.setText(noteItems.get(i).getPlace());
        viewHolder.start_day.setText(noteItems.get(i).getStart_day());
        viewHolder.end_day.setText(noteItems.get(i).getEnd_day());
        viewHolder.cost.setText(noteItems.get(i).getCost());
    }

    @Override
    public int getItemCount() {
        return noteItems.size();
    }

    public void removeNote(int position) {
        databaseHelper.deleteData("note", "ID = " + noteItems.get(position).getId());

        noteItems.remove(position);
        notifyItemRemoved(position);
    }

    public void DialogDeleteNote(final int position){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_delete_note_custom);

        Button ok_xoa_btn = dialog.findViewById(R.id.ok_xoa_btn);
        Button huy_btn = dialog.findViewById(R.id.huy_bnt);

        ok_xoa_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeNote(position);
                dialog.dismiss();
            }
        });

        huy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void setClickListener(ItemClickListener itemClickListener){
        this.clickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView place;
        TextView start_day;
        TextView end_day;
        TextView cost;

        public ViewHolder( View itemView) {
            super(itemView);

            place = itemView.findViewById(R.id.place_text);
            start_day = itemView.findViewById(R.id.start_day_text);
            end_day = itemView.findViewById(R.id.end_day_text);
            cost = itemView.findViewById(R.id.cost_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // something new activity to detail note
                    clickListener.onClickItem(v, getAdapterPosition());
                    Intent intent = new Intent(context, ManageNoteAcivity.class);
                    intent.putExtra("id", Integer.toString(noteItems.get(getAdapterPosition()).getId()));
                    context.startActivity(intent);

                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //removeNote(getAdapterPosition());
                    DialogDeleteNote(getAdapterPosition());
                    return false;
                }
            });
        }
    }

}
