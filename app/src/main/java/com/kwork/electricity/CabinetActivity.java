package com.kwork.electricity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.Adapter;

import com.kwork.electricity.adapters.vpAdapter;

public class CabinetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabinet);

        ViewPager vp = findViewById(R.id.vp2);
        PagerAdapter adapter = new vpAdapter(this, new int[]{1,2});
        vp.setAdapter(adapter);
    }
}