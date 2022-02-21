package com.kwork.electricity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class OrderActivity extends AppCompatActivity {

    TextView type, date, status, comment;
    EditText review;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;
    SharedPreferences mSettings;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        intent = getIntent();
        setTitle("ЗАКАЗ №"+intent.getStringExtra("id"));

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance("https://electroseti-9a632-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        mSettings = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);

        type = findViewById(R.id.textViewType);
        date = findViewById(R.id.textViewDate);
        status = findViewById(R.id.textViewStatus);
        comment = findViewById(R.id.textViewComment);
        review = findViewById(R.id.editTextReview);

        type.setText(intent.getStringExtra("service"));
        date.setText("дата оказания услуги: "+intent.getStringExtra("date"));
        status.setText("статус: "+intent.getStringExtra("status"));
        if (intent.getStringExtra("comment")!=null)
            comment.setText("комментарий к заказу: "+intent.getStringExtra("comment"));
        else
            comment.setText("комментарий к заказу: отсутствует");
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

    public void onSendClick(View view){
        if (review.getText().toString().length()<20){
            Snackbar.make(view ,"комментарий не может быть меньше 20 символов", Snackbar.LENGTH_LONG).show();
        }
        else{
            DatabaseReference ref = mDatabase.child("reviews").child(intent.getStringExtra("id"));
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getChildrenCount()>1){
                        Snackbar.make(view, "вы уже оставили отзыв к этому заказу", Snackbar.LENGTH_LONG).show();
                    }
                    else {
                        ref.child("userID").setValue(currentUser.getUid());
                        ref.child("text").setValue(review.getText().toString());
                        ref.child("userName").setValue(mSettings.getString("userName", "user"));
                        Snackbar.make(view, "отзыв опубликован", Snackbar.LENGTH_LONG).show();
                        try {
                            Thread.sleep(500);
                            finish();
                        }
                        catch (Exception ex) {Log.e("err", ex.getMessage());}
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}