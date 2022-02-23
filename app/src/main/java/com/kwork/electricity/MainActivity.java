package com.kwork.electricity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwork.electricity.utils.CheckInternet;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    SharedPreferences mSettings;
    private String adminUID = null;
    View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = findViewById(R.id.root);
        mSettings = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        if (mSettings.getString("brigade", null)!=null){
            Intent intent = new Intent(this, OrdersActivity.class);
            intent.putExtra("mode", 0);
            intent.putExtra("brigadeID", mSettings.getString("brigade", "null"));
            startActivity(intent);
            this.finish();
        }
// ...
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
// Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance("https://electroseti-9a632-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("adminID");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adminUID=snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
        getMenuInflater().inflate(R.menu.action_bar_registration, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        currentUser = mAuth.getCurrentUser();
        if (adminUID==null)
            Snackbar.make(root, "синхронизация с базой данных. Пожалуйста, подождите.", Snackbar.LENGTH_LONG).show();
        if(currentUser != null){
            if (currentUser.getUid().equals(adminUID)){
                startActivity(new Intent(this, AdminActivity.class));
            }
            else
                startActivity(new Intent(this, CabinetActivity.class));
        }
        else startActivity(new Intent(this, Registration.class));
        return true;
    }

    public void onAboutClick(View view){
        startActivity(new Intent(this, AboutActivity.class));
    }
    public void onNewOrderClick(View view){
        if (currentUser!=null) {
            if (mSettings.getBoolean("readyToOrder", false)){
                startActivity(new Intent(this, NewOrderActivity.class));
            }
            else Snackbar.make(view, "Пожалуйста, заполните всю информацию в личном кабинете", Snackbar.LENGTH_LONG).show();
        }
        else Snackbar.make(view, "Для оформления заказа необходимо зарегистрироваться", Snackbar.LENGTH_LONG).show();
    }
}