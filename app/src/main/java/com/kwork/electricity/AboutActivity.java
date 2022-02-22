package com.kwork.electricity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwork.electricity.utils.CheckInternet;

import java.util.ArrayList;

public class AboutActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    LinearLayout tvReviews;

    ArrayList<TextView> commentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("О НАС");
        tvReviews = findViewById(R.id.revievsLayout);

        mDatabase = FirebaseDatabase.getInstance("https://electroseti-9a632-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("reviews");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap:snapshot.getChildren()
                     ) {
                    TextView tv = new TextView(AboutActivity.this);
                    tv.setTextColor(Color.WHITE);
                    tv.setTextSize(18);
                    tv.setLineSpacing(3, 1);
                    tv.setText(Html.fromHtml("<b>"+snap.child("userName").getValue().toString()+"</b><br>"+snap.child("text").getValue().toString()+"<br>"));
                    commentList.add(tv);
                }
                for (TextView tv:commentList
                     ) {
                    tvReviews.addView(tv);
                }
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
        getMenuInflater().inflate(R.menu.action_bar_back, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        this.finish();
        return true;
    }
}