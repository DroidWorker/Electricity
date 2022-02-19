package com.kwork.electricity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class OrdersActivity extends AppCompatActivity {
    private int mode;//mode 0- open orders of current brigade|mode 1 open all orders
    private String brigadeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        setTitle("3АКАЗЫ");

        mode = getIntent().getIntExtra("mode", 0);
        brigadeID = getIntent().getStringExtra("brigadeID");

        if (mode==0){
            TextView tv = findViewById(R.id.ordersMode);
            if (brigadeID!=null)
                tv.setText("заказы бригады №"+brigadeID);
            else tv.setText("ошибка");
        }
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
}