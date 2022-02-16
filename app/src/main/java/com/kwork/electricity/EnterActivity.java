package com.kwork.electricity;

import androidx.appcompat.app.AppCompatActivity;

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

public class EnterActivity extends AppCompatActivity {
    boolean Emailcorrect = false;
    boolean Passwordcorrect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter);
        setTitle("ВХОД");

        EditText etEmail = findViewById(R.id.editTextEmailEnter);
        EditText etPassword = findViewById(R.id.editTextPasswordEnter);
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

    public void onLoginClick(View view){
        if (Emailcorrect&&Passwordcorrect){
            Toast.makeText(this, "AllOK", Toast.LENGTH_SHORT).show();
        }
    }
}