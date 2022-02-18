package com.kwork.electricity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.EditText;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.kwork.electricity.DataClasses.User;
import com.kwork.electricity.adapters.vpAdapter;

public class CabinetActivity extends AppCompatActivity {

    TabLayout tl;
    ViewPager vp;
    User user;

    EditText EtSurname;
    EditText EtName;
    EditText EtPatronymic;
    EditText EtPhone;
    EditText EtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cabinet);

        user = new User();

        tl = findViewById(R.id.tabLayout);
        vp = findViewById(R.id.vp2);

        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getId()==0){
                    vp.setCurrentItem(0);
                }
                else{
                    vp.setCurrentItem(1);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        PagerAdapter adapter = new vpAdapter(this, new int[]{1,2}, user);
        vp.setAdapter(adapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position==0){
                    tl.getTabAt(0).select();
                }
                else{
                    tl.getTabAt(1).select();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}