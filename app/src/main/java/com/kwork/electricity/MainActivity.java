package com.kwork.electricity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// ...
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
// Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_registration, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        if(currentUser != null){
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