package com.kwork.electricity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EnterActivity extends AppCompatActivity {
    boolean Emailcorrect = false;
    boolean Passwordcorrect = false;

    EditText etEmail;
    EditText etPassword;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        setTitle("ВХОД");
        mSettings = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        // ...
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
// Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(this, "registered", Toast.LENGTH_SHORT).show();
        }
        mDatabase = FirebaseDatabase.getInstance("https://electroseti-9a632-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        etEmail = findViewById(R.id.editTextEmailEnter);
        etPassword = findViewById(R.id.editTextPasswordEnter);
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if ((editable.toString()).matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")){
                    Emailcorrect=true;
                    etEmail.setTextColor(Color.WHITE);
                }
                else
                {
                    Emailcorrect=false;
                    etEmail.setTextColor(Color.RED);
                }
            }
        });
        TextView tvClue = findViewById(R.id.clue);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if ((editable.toString()).matches("^(?=.*?[A-ZА-Я])(?=.*?[a-zа-я])(?=.*?[0-9]).{8,}$")){
                    Passwordcorrect=true;
                    etPassword.setTextColor(Color.WHITE);
                }
                else
                {
                    Passwordcorrect=false;
                    etPassword.setTextColor(Color.RED);
                }
            }
        });
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

    String userName = "error";
    public void onLoginClick(View view){
        if (Emailcorrect&&Passwordcorrect){
            mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                DatabaseReference ref = mDatabase.child("users").child(user.getUid());
                                ref.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        userName = snapshot.child("userName").getValue().toString();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putString("userName", userName);
                                editor.putString("userEmail", etEmail.getText().toString());
                                editor.apply();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(EnterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else Toast.makeText(this, "некорректные данные", Toast.LENGTH_SHORT).show();
    }
}