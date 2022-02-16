package com.kwork.electricity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
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

import com.kwork.electricity.R;

public class Registration extends AppCompatActivity {
    boolean Namecorrect = false;
    boolean Emailcorrect = false;
    boolean Passwordcorrect = false;
    Context ctx;

    EditText etPassword;
    EditText etPasswordRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ctx = this;
        setTitle("РЕГИСТРАЦИЯ");

        EditText etName = findViewById(R.id.editTextName);
        EditText etEmail = findViewById(R.id.editTextEmail);
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
            Toast.makeText(ctx, "AllOK", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(ctx, "пароли не совпдают", Toast.LENGTH_SHORT).show();
        }
    }
    public void onLoginClick(View view){
        startActivity(new Intent(this, EnterActivity.class));
    }
}