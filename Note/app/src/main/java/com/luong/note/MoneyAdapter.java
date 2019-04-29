package com.luong.note;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MoneyAdapter extends RecyclerView.Adapter<MoneyAdapter.ViewHolder> {
    private ItemClickListener clickListener;
    ArrayList<String[]> moneys; // 0 is id_note, 1 is name, 2 is money
    Context context;
    DatabaseHelper databaseHelper;
    public MoneyAdapter(ArrayList<String[]> moneys, Context context) {
        this.moneys = moneys;
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.item_row, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.money.setText(moneys.get(i)[1] + " " + moneys.get(i)[2] + "k");
    }

    @Override
    public int getItemCount() {
        return moneys.size();
    }

    public void removeMoney(int position) {
        databaseHelper.deleteData("money_member_note", "id_note = " + moneys.get(position)[0] + " AND name_member = '" + moneys.get(position)[1] + "'");

        moneys.remove(position);
        notifyItemRemoved(position);
    }

    public void setClickListener(ItemClickListener itemClickListener){
        this.clickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView money;
        TextView delete;

        public ViewHolder( View itemView) {
            super(itemView);

            money = itemView.findViewById(R.id.content);
            delete = itemView.findViewById(R.id.delete_row);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // something new activity to detail note
                }
            });


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (clickListener != null) {
                        clickListener.onClickMoney(v, getAdapterPosition());
                        removeMoney(getAdapterPosition());
                    }

                }
            });
        }
    }
}
