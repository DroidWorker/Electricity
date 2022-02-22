package com.kwork.electricity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.kwork.electricity.utils.CheckInternet;

public class NoInternetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
    }

    public void updateClick(View view){
        if (CheckInternet.hasConnection(this)){
            finish();
        }
        else
            Snackbar.make(view, "всё ещё не появился(", Snackbar.LENGTH_LONG).show();
    }
}