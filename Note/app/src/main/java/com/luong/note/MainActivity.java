package com.luong.note;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ItemClickListener{
    ArrayList<NoteItem> arrayList;
    NoteAdapter noteAdapter;

    DatabaseHelper databaseHelper;

    int currentIndex = -1;

    int maxId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        databaseHelper = new DatabaseHelper(this);
        Log.i("db", "create db success");
        arrayList = new ArrayList<>();
        initView();

        FloatingActionButton add_note_bnt = findViewById(R.id.add_note_btn);
        add_note_bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogAddNote();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clearall) {
            int items = arrayList.size();
            arrayList.clear();
            if (noteAdapter != null) {
                noteAdapter.notifyItemRangeChanged(0, items);
            }
            databaseHelper.deleteData("note", "1 = 1");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getDataDbs() {
        try {
            Cursor c = databaseHelper.getData("SELECT * FROM note");

            int idIndex = c.getColumnIndex("ID");
            int placeIndex = c.getColumnIndex("place");
            int start_dayIndex = c.getColumnIndex("start_day");
            int end_dayIndex = c.getColumnIndex("end_day");
            int costIndex = c.getColumnIndex("cost");
            int memberIndex = c.getColumnIndex("member");

            c.moveToFirst();

            while (c != null) {
                Integer id = Integer.parseInt(c.getString(idIndex));
                int member = Integer.parseInt(c.getString(memberIndex));

                arrayList.add(new NoteItem(c.getString(placeIndex), c.getString(start_dayIndex), c.getString(end_dayIndex), member, c.getInt(costIndex), c.getInt(idIndex)));
                Log.i("aaa", arrayList.toString());
                maxId = id;

                c.moveToNext();
            }
        } catch (Exception e) {
            Log.i("in getDataDbs", "can't get data");
            e.printStackTrace();
        }
    }

    public void initView() {
        RecyclerView recyclerView = findViewById(R.id.recycle_view_notes);
        recyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

//        arrayList.add(new NoteItem("Vũng Tàu", "12/12/1998", "14/12/1998", 4));
//        arrayList.add(new NoteItem("Đà Nẵng", "1/1/2015", "5/1/2015", 5));
//        arrayList.add(new NoteItem("Vũng Tàu", "12/12/1998", "14/12/1998", 4));
//        arrayList.add(new NoteItem("Vũng Tàu", "12/12/1998", "14/12/1998", 4));
//        arrayList.add(new NoteItem("Vũng Tàu", "12/12/1998", "14/12/1998", 4));

        getDataDbs();

        noteAdapter = new NoteAdapter(arrayList, this);
        noteAdapter.setClickListener(this);
        recyclerView.setAdapter(noteAdapter);


    }

    public void addNote(String place_str, String start_day_str, String end_day_str, int member_int, int cost) {

        Object []oj = new Object[5];
        oj[0] = place_str;
        oj[1] = start_day_str;
        oj[2] = end_day_str;
        oj[3] = cost;
        oj[4] = member_int;
        boolean insertDB =  databaseHelper.addData("note", oj);
        if(insertDB) {
            Log.i("insert", "insert successful");
            maxId++;
        }

        NoteItem note = new NoteItem(place_str, start_day_str, end_day_str, member_int, cost, maxId);
        arrayList.add(note);
        noteAdapter.notifyItemInserted(arrayList.size() - 1);
    }

    public void DialogAddNote(){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_note);

        final EditText place = dialog.findViewById(R.id.place_dialog);
        final EditText member = dialog.findViewById(R.id.member_dialog);
        final EditText start_day = dialog.findViewById(R.id.start_day_dialog);
        final EditText end_day = dialog.findViewById(R.id.end_day_dialog);

        Button them_note_btn = dialog.findViewById(R.id.them_note_btn);
        Button huy_btn = dialog.findViewById(R.id.huy_bnt);

        them_note_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place_str = place.getText().toString();
                String start_day_str = start_day.getText().toString();
                String end_day_str = end_day.getText().toString();
                Integer member_int = 0;
                if (!member.getText().toString().equals("")) {
                    member_int = Integer.parseInt(member.getText().toString());
                }
                if (place_str.equals("") || start_day_str.equals("") || end_day_str.equals("") || member_int <= 0 )
                {
                    Toast.makeText(MainActivity.this, "Bạn chưa điền đủ dữ liệu kìa :v", Toast.LENGTH_SHORT).show();
                }
                else {
                    addNote(place_str, start_day_str, end_day_str, member_int, 0);
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

    @Override
    protected void onResume() {
        super.onResume();
        if(currentIndex != -1) {
            try {
                Cursor c = databaseHelper.getData("SELECT * FROM note WHERE ID = " + Integer.toString(arrayList.get(currentIndex).getId()));
                int costIndex = c.getColumnIndex("cost");
                c.moveToFirst();

                if (c != null) {
                    arrayList.get(currentIndex).setCost(Integer.parseInt(c.getString(costIndex)));
                    noteAdapter.notifyItemChanged(currentIndex);
                }

            } catch (Exception e) {
                Log.i("in get cost onResum", "can't get data");
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClickMember(View view, int position) {

    }

    @Override
    public void onClickMoney(View view, int position) {

    }

    @Override
    public void onClickItem(View view, int position) {
        currentIndex = position;
    }
}
