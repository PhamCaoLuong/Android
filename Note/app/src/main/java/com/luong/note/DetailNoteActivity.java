package com.luong.note;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DetailNoteActivity extends AppCompatActivity implements ItemClickListener{
    ArrayList<String[]> arrayMember;
    ArrayList<String[]> arrayMoney;

    MemberAdapter memberAdapter;
    MoneyAdapter moneyAdapter;

    int id;
    int totalMoney;
    DatabaseHelper databaseHelper;

    TextView totalTextView;
    TextView add_member;
    TextView add_money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_member_note);

        arrayMember = new ArrayList<>();
        arrayMoney = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);
        Log.i("Detail Note Activity ID", Integer.toString(id));

        totalTextView = findViewById(R.id.balance_detail);
        add_member = findViewById(R.id.add_member);
        add_money = findViewById(R.id.add_money);
        initView();

    }

    public void getDataMemberDB() {
        try {
            Cursor c = databaseHelper.getData("SELECT * FROM member_note WHERE id_note = " + Integer.toString(id));

            int idIndex = c.getColumnIndex("id_note");
            int placeIndex = c.getColumnIndex("name_member");

            c.moveToFirst();

            while (c != null) {

                arrayMember.add(new String[]{Integer.toString(id), c.getString(placeIndex)});
                c.moveToNext();
            }
        } catch (Exception e) {
            Log.i("Detail Note get Member", "can't get data");
            e.printStackTrace();
        }
    }
    public void getDataMoneyDB() {
        try {
            Cursor c = databaseHelper.getData("SELECT * FROM money_member_note WHERE id_note = " + Integer.toString(id));

            int moneyIndex = c.getColumnIndex("money");
            int placeIndex = c.getColumnIndex("name_member");

            c.moveToFirst();

            while (c != null) {
                totalMoney += Integer.parseInt(c.getString(moneyIndex));
                arrayMoney.add(new String[]{Integer.toString(id), c.getString(placeIndex), c.getString(moneyIndex)});
                c.moveToNext();
            }
        } catch (Exception e) {
            Log.i("Detail Note get Member", "can't get data");
            e.printStackTrace();
        }
    }

    public void initView() {
        RecyclerView recyclerViewMem = findViewById(R.id.member_detail_recyclerview);
        RecyclerView recyclerViewMon = findViewById(R.id.money_detail_recyclerview);
        recyclerViewMem.setHasFixedSize(true);
        recyclerViewMon.setHasFixedSize(true);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerViewMem.setLayoutManager(layoutManager);
        recyclerViewMem.setItemAnimator(new DefaultItemAnimator());

        LinearLayoutManager layoutManagerMon = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewMon.setLayoutManager(layoutManagerMon);
        recyclerViewMon.setItemAnimator(new DefaultItemAnimator());

        getDataMemberDB();

        getDataMoneyDB();

        //getDataDbs();

        memberAdapter = new MemberAdapter(arrayMember, this);
        memberAdapter.setClickListener(this);
        recyclerViewMem.setAdapter(memberAdapter);

        moneyAdapter = new MoneyAdapter(arrayMoney, this);
        moneyAdapter.setClickListener(this);
        recyclerViewMon.setAdapter(moneyAdapter);

        updateTotalMoneyText();

        setListener();
    }

    public void setListener() {
        add_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new dialog
                DialogAddMember();
            }
        });

        add_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // new dialog
                DialogAddMoney();
            }
        });
    }

    public void DialogAddMoney(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_money);

        final EditText money = dialog.findViewById(R.id.money_dialog);

        final AutoCompleteTextView nameMember = dialog.findViewById(R.id.name_money_dialog);

        ArrayList<String> member = new ArrayList<>();
        for(int i = 0; i < arrayMember.size(); i++) {
            member.add(arrayMember.get(i)[1]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, member);
        nameMember.setThreshold(0);
        nameMember.setAdapter(adapter);

        Button dong_tien_bnt = dialog.findViewById(R.id.dong_tien_bnt);
        Button huy_btn = dialog.findViewById(R.id.huy_bnt);

        dong_tien_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money_str = money.getText().toString();
                String nameMember_str = nameMember.getText().toString();

                if (money_str.equals("") || nameMember_str.equals(""))
                {
                    Toast.makeText(DetailNoteActivity.this, "Bạn chưa điền đủ dữ liệu kìa :v", Toast.LENGTH_SHORT).show();
                }
                boolean exits = false;

                for (int i = 0; i < arrayMember.size(); i++) {
                    if (arrayMember.get(i)[1].equals(nameMember_str)) {
                        exits = true;
                        break;
                    }
                }

                if (!exits) {
                    Toast.makeText(DetailNoteActivity.this, "Bạn này chưa trong nhóm :v", Toast.LENGTH_SHORT).show();
                }
                else {
                    addMoney(nameMember_str, money_str);
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

    private void addMoney(String nameMember, String money) {
        int memberIndex = -1;
        for (int i = 0; i < arrayMoney.size(); i++) {
            if (arrayMoney.get(i)[1].equals(nameMember)) {
                memberIndex = i;
                arrayMoney.get(i)[2] = Integer.toString(Integer.parseInt(arrayMoney.get(i)[2]) + Integer.parseInt(money));
            }
        }
        if (memberIndex == - 1) {
            Object[] oj = new Object[3];
            oj[0] = Integer.toString(id);
            oj[1] = nameMember;
            oj[2] = money;
            databaseHelper.addData("money_member_note", oj);
            arrayMoney.add(new String[]{(String) oj[0], (String) oj[1], (String) oj[2]});
            moneyAdapter.notifyItemInserted(arrayMoney.size() - 1);
        }

        else {
            databaseHelper.updateData("money_member_note", "money = " + arrayMoney.get(memberIndex)[2], "id_note = " + Integer.toString(id) + " AND name_member = '" + nameMember +"'");
            moneyAdapter.notifyItemChanged(memberIndex);
        }

        totalMoney += Integer.parseInt(money);
        updateTotalMoneyText();
    }

    public void DialogAddMember(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_member);

        final EditText nameMember = dialog.findViewById(R.id.name_dialog);

        Button them_member_bnt = dialog.findViewById(R.id.them_member_bnt);
        Button huy_btn = dialog.findViewById(R.id.huy_bnt);

        them_member_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameMember_str = nameMember.getText().toString();

                if (nameMember_str.equals(""))
                {
                    Toast.makeText(DetailNoteActivity.this, "Bạn chưa điền đủ dữ liệu kìa :v", Toast.LENGTH_SHORT).show();
                }
                boolean exits = false;

                for (int i = 0; i < arrayMember.size(); i++) {
                    if (arrayMember.get(i)[1].equals(nameMember_str)) {
                        exits = true;
                        break;
                    }
                }

                if (exits) {
                    Toast.makeText(DetailNoteActivity.this, "Bạn này đã có trong nhóm >.<", Toast.LENGTH_SHORT).show();
                }
                else {
                    addMember(nameMember_str);
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

    private void addMember(String nameMember) {
        Object []oj = new Object[3];
        oj[0] = Integer.toString(id);
        oj[1] = nameMember;
        boolean insertDB =  databaseHelper.addData("member_note", oj);
        if(insertDB) {
            Log.i("insert member", "insert successful");
        }

        arrayMember.add(new String[]{Integer.toString(id), nameMember});
        memberAdapter.notifyItemInserted(arrayMember.size() - 1);
    }

    @Override
    public void onClickMember(View view, int position) {
        String member = arrayMember.get(position)[1];

        for(int i = 0; i < arrayMoney.size(); i++) {
            if (arrayMoney.get(i)[1].equals(member)) {
                totalMoney -= Integer.parseInt(arrayMoney.get(i)[2]);
                arrayMoney.remove(i);
                moneyAdapter.notifyItemRemoved(i);
                break;
            }
        }
        updateTotalMoneyText();

        databaseHelper.deleteData("money_member_note", "id_note = " + Integer.toString(id) + " AND name_member = '" + member + "'");
    }

    @Override
    public void onClickMoney(View view, int position) {
        Integer money = Integer.parseInt(arrayMoney.get(position)[2]);
        totalMoney -= money;
        updateTotalMoneyText();
    }

    @Override
    public void onClickItem(View view, int position) {

    }

    public void updateTotalMoneyText() {
        DecimalFormat decimalFormat = new DecimalFormat("###,###,##0");
        String numberAsString = decimalFormat.format(totalMoney);
        totalTextView.setText(numberAsString + "k");
    }
}
