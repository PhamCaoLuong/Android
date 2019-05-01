package com.luong.note;

import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentTong extends Fragment {

    View view;
    Context context;
    TongAdapter tongAdapter;

    String id;
    ArrayList<String> listMember;
    ArrayList<Integer> listMoneyOfMember;
    ArrayList<String[]> listCalculate;

    Integer totalMoney;

    DatabaseHelper databaseHelper;



    public FragmentTong() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        initView();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.tong_fragment, container, false);
        id = getArguments().getString("id");

        initView();

        return view;
    }

    public void getData() {
        // get total cost
        try {
            Cursor c = databaseHelper.getData("SELECT * FROM note WHERE ID = " + id);

            int moneyIndex = c.getColumnIndex("cost");

            c.moveToFirst();

            if (c != null) {
                totalMoney = Integer.parseInt(c.getString(moneyIndex));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // get member



        // get money of each member pay
        try {
            Cursor c = databaseHelper.getData("SELECT * FROM money_member_note WHERE id_note = " + id);

            int nameIndex = c.getColumnIndex("name_member");
            int moneyIndex = c.getColumnIndex("money");

            c.moveToFirst();

            while (c != null) {
                listMember.add(c.getString(nameIndex));
                listMoneyOfMember.add(Integer.parseInt(c.getString(moneyIndex)));

                c.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // caculate money need more
        if(listMember.size() > 0) {
            calculateMoney();
        }


    }

    public void calculateMoney() {
        int average = totalMoney / listMember.size();


        for(int i = 0; i < listMember.size(); i++) {
            int money = listMoneyOfMember.get(i);
            if (money > average) {
                listCalculate.add(new String[]{listMember.get(i), "-1", Integer.toString(money - average)}); // -1 is nhận lại
            }
            else if (money < average) {
                listCalculate.add(new String[]{listMember.get(i), "1", Integer.toString(average - money)}); // 1 is đóng thêm
            }
            else {
                listCalculate.add(new String[]{listMember.get(i), "0", Integer.toString(money)}); // 0 is đóng đủ
            }
        }

    }

    public void initView() {

        listMember = new ArrayList<>();
        listCalculate = new ArrayList<>();
        listMoneyOfMember = new ArrayList<>();
        databaseHelper = new DatabaseHelper(context);

        RecyclerView recyclerView = view.findViewById(R.id.list_tong);

        recyclerView.setHasFixedSize(true);


        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getData();

        tongAdapter = new TongAdapter(listCalculate, context);
        recyclerView.setAdapter(tongAdapter);
    }
}


class TongAdapter extends RecyclerView.Adapter<TongAdapter.ViewHolder> {
    ArrayList<String[]> moneys; // 0 is name, 1 is Nhan lai (-1) or dong them (1) or k gi (0), 2 cost
    Context context;

    final String NHANLAI = " nhận lại ";
    final String DONGTHEM = " đóng thêm ";
    final String DU = " đã đóng đủ";

    public TongAdapter(ArrayList<String[]> moneys, Context context) {
        this.moneys = moneys;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.calculate_row, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        if (Integer.parseInt(moneys.get(i)[1]) == -1) {
            viewHolder.name.setText(moneys.get(i)[0]);
            viewHolder.status.setText(NHANLAI);
            viewHolder.money.setText( moneys.get(i)[2] + "k");
            viewHolder.money.setTextColor(Color.parseColor("#40CE0E"));
        }

        else if (Integer.parseInt(moneys.get(i)[1]) == 0) {
            viewHolder.name.setText(moneys.get(i)[0]);
            viewHolder.status.setText(DU);
        }
        else {
            viewHolder.name.setText(moneys.get(i)[0]);
            viewHolder.status.setText(DONGTHEM);
            viewHolder.money.setText( moneys.get(i)[2] + "k");
            viewHolder.money.setTextColor(Color.parseColor("#F44336"));
        }

    }

    @Override
    public int getItemCount() {
        return moneys.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView money;
        TextView status;
        TextView name;

        public ViewHolder( View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            status = itemView.findViewById(R.id.status);
            money = itemView.findViewById(R.id.money);
        }
    }
}


