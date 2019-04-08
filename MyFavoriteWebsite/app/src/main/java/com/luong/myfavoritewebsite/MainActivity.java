package com.luong.myfavoritewebsite;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
	ListView lvWeb;
    ArrayList<String> webList;
    ArrayAdapter webAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lvWeb = findViewById(R.id.lvWebList);

        webList = new ArrayList<>();
        webAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, webList);
        createData();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
				
				 //Open Dialog Box to get URL

                final android.app.Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.custom_dialog);
                dialog.setTitle("Insert URL");

                // set the custom dialog components - text, image and button
                TextView text =  (TextView) dialog.findViewById(R.id.textNot);
                text.setText("Insert favorit URL to keep favorit site");
                Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Insert to weblist(url.getText().toString());
                        EditText url = dialog.findViewById(R.id.url);
                        WebInfo webInfo = new WebInfo(url.getText().toString());
                        webList.add(webInfo.domainName);
                        webAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        lvWeb.setAdapter(webAdapter);

        lvWeb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                intent.putExtra("url", webList.get(position));
                startActivity(intent);
            }
        });

    }

    /** Add data to webList*/
    public void createData() {
        WebInfo webInfo = new WebInfo("https://www.google.com");
        webList.add(webInfo.domainName);
        webInfo = new WebInfo("https://www.udemy.com/");
        webList.add(webInfo.domainName);
        webInfo = new WebInfo("https://www.khanacademy.org");
        webList.add(webInfo.domainName);
        webInfo = new WebInfo("https://material.io/");
        webList.add(webInfo.domainName);
        webInfo = new WebInfo("https://www.w3schools.com/");
        webList.add(webInfo.domainName);
        Log.i("weblist", webList.toString());
        webAdapter.notifyDataSetChanged();
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
