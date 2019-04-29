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

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
    private ItemClickListener clickListener;
    ArrayList<String[]> members; // 0 is id, 1 is name
    Context context;
    DatabaseHelper databaseHelper;
    public MemberAdapter(ArrayList<String[]> members, Context context) {
        this.members = members;
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
        viewHolder.member.setText(members.get(i)[1]);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public void removeMember(int position) {
        databaseHelper.deleteData("member_note", "id_note = " + members.get(position)[0] +
                                                                        " AND name_member = '" + members.get(position)[1] + "'");
//        databaseHelper.deleteData("DELETE FROM money_member_note WHERE id_note = " + members.get(position)[0] +
//                " AND name_member = " + members.get(position)[1]);

        members.remove(position);
        notifyItemRemoved(position);
    }

//    public void DialogDeleteMoney(final int position) {
//        final Dialog dialog = new Dialog(context);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.dialog_delete_note_custom);
//
//        Button ok_xoa_btn = dialog.findViewById(R.id.ok_xoa_btn);
//        Button huy_btn = dialog.findViewById(R.id.huy_bnt);
//
//        ok_xoa_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                removeMoney(position);
//                dialog.dismiss();
//            }
//        });
//
//        huy_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//
//        dialog.show();
//    }

    public void setClickListener(ItemClickListener itemClickListener){
        this.clickListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView member;
        TextView delete;

        public ViewHolder( View itemView) {
            super(itemView);

            member = itemView.findViewById(R.id.content);
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
                        clickListener.onClickMember(v, getAdapterPosition());
                        removeMember(getAdapterPosition());
                    }// còn phải sửa để sao cho khi xóa cập nhật được total money ở ngoài
                }
            });
        }
    }


}
