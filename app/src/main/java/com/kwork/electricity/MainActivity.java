package com.kwork.electricity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private DatabaseReference mDatabase;
    private String adminUID = null;
    View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        root = findViewById(R.id.root);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_registration, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
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
        startActivity(new Intent(this, NewOrderActivity.class));
    }
}