package com.kwork.electricity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kwork.electricity.utils.CheckInternet;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        setTitle("АДМИНИСТРАТОР");
    }
    @Override
    public void onResume() {
        if (!CheckInternet.hasConnection(this)){
            startActivity(new Intent(this, NoInternetActivity.class));
        }
        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_back, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        this.finish();
        return true;
    }

    public void onBrigadeClick(View view){
        startActivity(new Intent(this, BrigadeActivity.class));
    }
    public void onServisesClick(View view){
        startActivity(new Intent(this, ServicesActivity.class));
    }
    public void onOrdersClick(View view){
        Intent intent = new Intent(this, OrdersActivity.class);
        intent.putExtra("mode", 1);
        startActivity(intent);
    }
    public void onReviewsClick(View view){
        startActivity(new Intent(this, ReviewsActivity.class));
    }
    public void onDatesClick(View view){
        startActivity(new Intent(this, DatesActivity.class));
    }
}