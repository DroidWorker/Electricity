package com.kwork.electricity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kwork.electricity.R;

public class Registration extends AppCompatActivity {
    boolean Namecorrect = false;
    boolean Emailcorrect = false;
    boolean Passwordcorrect = false;
    Context ctx;

    EditText etName;
    EditText etEmail;
    EditText etPassword;
    EditText etPasswordRepeat;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ctx = this;
        setTitle("РЕГИСТРАЦИЯ");
// ...
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
// Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(this, "registered", Toast.LENGTH_SHORT).show();
        }
        mDatabase = FirebaseDatabase.getInstance("https://electroseti-9a632-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

        mSettings = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);

        etName = findViewById(R.id.editTextName);
        etEmail = findViewById(R.id.editTextEmail);
        etPassword = findViewById(R.id.editTextPassword);
        etPasswordRepeat = findViewById(R.id.editTextPassword2);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void afterTextChanged(Editable editable) {
                if ((editable.toString()).matches("^([А-Я]?[а-яё]{1,23}|[A-Z]?[a-z]{1,23})$")){
                    Namecorrect=true;
                    etName.setTextColor(Color.WHITE);
                }
                else{
                    Namecorrect=false;
                    etName.setTextColor(Color.RED);
                }
            }
        });
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
                    tvClue.setVisibility(View.GONE);
                }
                else
                {
                    Passwordcorrect=false;
                    tvClue.setVisibility(View.VISIBLE);
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

    public void onAcceptClick(View view){
        if (Namecorrect&&Emailcorrect&&Passwordcorrect&&etPassword.getText().toString().equals(etPasswordRepeat.getText().toString())){
            mAuth.createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                SharedPreferences.Editor editor = mSettings.edit();
                                editor.putString("userName", etName.getText().toString());
                                editor.putString("userEmail", etEmail.getText().toString());
                                editor.apply();
                                FirebaseUser user = mAuth.getCurrentUser();
                                mDatabase.child("users").child(user.getUid()).child("userName").setValue(etName.getText().toString());
                                startActivity(new Intent(ctx, CabinetActivity.class));
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(ctx, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else{
            Toast.makeText(ctx, "пароли не совпдают", Toast.LENGTH_SHORT).show();
        }
    }
    public void onLoginClick(View view){
        startActivity(new Intent(this, EnterActivity.class));
    }
}