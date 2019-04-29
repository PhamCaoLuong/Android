package com.luong.note;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class ManageNoteAcivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    String idNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        Intent intent = getIntent();
        idNote = intent.getStringExtra("id");

        ViewPageAdapter viewPageAdapter = new ViewPageAdapter(getSupportFragmentManager());

        FragmentQuy quyFragment = new FragmentQuy();
        Bundle bundle = new Bundle();
        bundle.putString("id", idNote);
        quyFragment.setArguments(bundle);
        quyFragment.setContext(this);

        FragmentMua mua = new FragmentMua();
        mua.setArguments(bundle);
        mua.setContext(this);

        FragmentTong tong = new FragmentTong();
        tong.setArguments(bundle);
        tong.setContext(this);

        viewPageAdapter.addFragment(quyFragment, "Quỹ");
        viewPageAdapter.addFragment(mua, "Chi tiêu");
        viewPageAdapter.addFragment(tong, "Tính tiền");

        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);


    }
}
