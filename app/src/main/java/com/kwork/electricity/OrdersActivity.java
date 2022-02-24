package com.kwork.electricity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwork.electricity.DataClasses.Order;
import com.kwork.electricity.adapters.AdminOrdersAdapter;
import com.kwork.electricity.utils.CheckInternet;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class OrdersActivity extends AppCompatActivity {
    private int mode;//mode 0- open orders of current brigade|mode 1 open all orders
    private String brigadeID;

    RecyclerView rv;

    List<Order> orders = new ArrayList<>();

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        setTitle("3АКАЗЫ");

        mDatabase = FirebaseDatabase.getInstance("https://electroseti-9a632-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        mode = getIntent().getIntExtra("mode", 0);
        brigadeID = getIntent().getStringExtra("brigadeID");
        rv = findViewById(R.id.ordersView);

        AdminOrdersAdapter adapter = new AdminOrdersAdapter(this, orders);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));

        if (mode==0){
            TextView tv = findViewById(R.id.ordersMode);
            if (brigadeID!=null)
                tv.setText("заказы бригады №"+brigadeID);
            else tv.setText("ошибка");
            mDatabase.child("orders").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap:snapshot.getChildren()
                    ) {
                        if (snap.child("brigadeId").getValue()!=null&&brigadeID.equals(snap.child("brigadeId").getValue().toString())){
                            Order order = new Order();
                            order.orderId = snap.getKey();
                            order.userUid = snap.child("userUID").getValue().toString();
                            order.adress = snap.child("address").getValue().toString();
                            order.status = snap.child("status").getValue().toString();
                            if (snap.child("brigadeId")!=null)
                                order.brigadeId = snap.child("brigadeId").getValue().toString();
                            order.serviceType = snap.child("serviceType").getValue().toString();
                            order.date = snap.child("date").getValue().toString();
                            order.comment = snap.child("comment").getValue().toString();

                            for (Order o:orders
                            ) {
                                if (o.orderId.equals(order.orderId)) {
                                    orders.remove(o);
                                    break;
                                }
                            }
                            orders.add(order);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            mDatabase.child("orders").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snap:snapshot.getChildren()
                    ) {
                            Order order = new Order();
                            order.orderId = snap.getKey();
                            order.adress = snap.child("address").getValue().toString();
                        order.userUid = snap.child("userUID").getValue().toString();
                            order.status = snap.child("status").getValue().toString();
                            if (order.status.equals("бригада назначена"))
                                order.brigadeId = snap.child("brigadeId").getValue().toString();
                            order.serviceType = snap.child("serviceType").getValue().toString();
                            order.date = snap.child("date").getValue().toString();
                            order.comment = snap.child("comment").getValue().toString();
                            orders.add(order);
                    }
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
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
}