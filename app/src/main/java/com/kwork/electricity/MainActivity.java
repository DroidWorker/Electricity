package com.kwork.electricity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setTitle("Электро-сети");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_registration, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        startActivity(new Intent(this, Registration.class));
        return true;
    }

    public void onAboutClick(View view){
        startActivity(new Intent(this, AboutActivity.class));
    }
    public void onNewOrderClick(View view){
        startActivity(new Intent(this, NewOrderActivity.class));
    }
}