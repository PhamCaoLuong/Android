package com.luong.note;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class FragmentMua extends Fragment implements ItemClickListener{
    View view;

    Context context;
    String id;
    ArrayList<String[]> arrayItems;
    BuyItemAdapter buyItemAdapter;
    int totalMoney;

    DatabaseHelper databaseHelper;

    TextView totalTextView;
    FloatingActionButton add_item_bnt;

    public FragmentMua() {
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.mua_fragment, container, false);

        arrayItems = new ArrayList<>();
        databaseHelper = new DatabaseHelper(context);
        totalTextView = view.findViewById(R.id.money_spend);
        add_item_bnt = view.findViewById(R.id.add_item_bought_btn);

        id = getArguments().getString("id");
        initView();


        return view;
    }

    public void getDataBuyItemDB() {
        try {
            Cursor c = databaseHelper.getData("SELECT * FROM cost_item WHERE id_note = " + id);

            int moneyIndex = c.getColumnIndex("cost");
            int nameIndex = c.getColumnIndex("name");

            c.moveToFirst();

            while (c != null) {
                totalMoney += Integer.parseInt(c.getString(moneyIndex));
                arrayItems.add(new String[]{id, c.getString(nameIndex), c.getString(moneyIndex)});
                c.moveToNext();
            }
        } catch (Exception e) {
            Log.i("Detail Note get item", "can't get data");
            e.printStackTrace();
        }
    }

    public void initView() {

        RecyclerView recyclerViewMon = view.findViewById(R.id.recycle_view_notes);

        recyclerViewMon.setHasFixedSize(true);


        LinearLayoutManager layoutManagerMon = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerViewMon.setLayoutManager(layoutManagerMon);
        recyclerViewMon.setItemAnimator(new DefaultItemAnimator());


        getDataBuyItemDB();

        //getDataDbs();

        buyItemAdapter = new BuyItemAdapter(arrayItems, context);
        buyItemAdapter.setClickListener(this);
        recyclerViewMon.setAdapter(buyItemAdapter);

        updateTotalMoneyText();

        setListener();
    }

    public void setListener() {
        add_item_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new dialog
                DialogAddBuyItem();
            }
        });
    }

    public void DialogAddBuyItem(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_item_bought);

        final EditText money = dialog.findViewById(R.id.money_item_dialog);

        final EditText nameItem = dialog.findViewById(R.id.name_item_dialog);

        Button mua_bnt = dialog.findViewById(R.id.mua_bnt);
        Button huy_btn = dialog.findViewById(R.id.huy_bnt);

        mua_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money_str = money.getText().toString();
                String nameItem_str = nameItem.getText().toString();

                if (money_str.equals("") || nameItem_str.equals(""))
                {
                    Toast.makeText(context, "Bạn chưa điền đủ dữ liệu kìa :v", Toast.LENGTH_SHORT).show();
                }
                else {
                    addItem(nameItem_str, money_str);
                    dialog.dismiss();
                }
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

    private void addItem(String nameItem, String money) {
        int itemIndex = -1;
        for (int i = 0; i < arrayItems.size(); i++) {
            if (arrayItems.get(i)[1].equals(nameItem)) {
                itemIndex = i;
                arrayItems.get(i)[2] = Integer.toString(Integer.parseInt(arrayItems.get(i)[2]) + Integer.parseInt(money));
            }
        }
        if (itemIndex == - 1) {
            Object[] oj = new Object[3];
            oj[0] = id;
            oj[1] = nameItem;
            oj[2] = money;
            databaseHelper.addData("cost_item", oj);
            arrayItems.add(new String[]{(String) oj[0], (String) oj[1], (String) oj[2]});
            buyItemAdapter.notifyItemInserted(arrayItems.size() - 1);
        }

        else {
            databaseHelper.updateData("cost_item", "cost = " + arrayItems.get(itemIndex)[2], "id_note = " + id + " AND name = '" + nameItem +"'");
            buyItemAdapter.notifyItemChanged(itemIndex);
        }

        totalMoney += Integer.parseInt(money);
        updateTotalMoneyText();
    }

    @Override
    public void onClickMember(View view, int position) {

    }

    @Override
    public void onClickMoney(View view, int position) {

    }

    @Override
    public void onClickItem(View view, int position) {
        Integer money = Integer.parseInt(arrayItems.get(position)[2]);
        totalMoney -= money;
        updateTotalMoneyText();
    }


    public void updateTotalMoneyText() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,##0");
        String numberAsString = decimalFormat.format(totalMoney);
        totalTextView.setText(numberAsString + "k");
        databaseHelper.updateData("note", "cost = " + Integer.toString(totalMoney), "ID = " + id);
    }
}
