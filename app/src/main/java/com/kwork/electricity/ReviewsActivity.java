package com.kwork.electricity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwork.electricity.utils.CheckInternet;

import java.util.ArrayList;

public class ReviewsActivity extends AppCompatActivity {
    LinearLayout tvReviews;
    private DatabaseReference mDatabase;
    ArrayList<TextView> commentList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        tvReviews = findViewById(R.id.rlayout);

        mDatabase = FirebaseDatabase.getInstance("https://electroseti-9a632-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("reviews");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap:snapshot.getChildren()
                ) {
                    TextView tv = new TextView(ReviewsActivity.this);
                    tv.setTextColor(Color.WHITE);
                    tv.setTextSize(18);
                    tv.setLineSpacing(3, 1);
                    tv.setHint(snap.getKey());
                    tv.setText(Html.fromHtml("<b>"+snap.child("userName").getValue().toString()+"</b><br>"+snap.child("text").getValue().toString()+"<br>"));
                    tv.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            tv.setBackgroundColor(Color.RED);
                            createTwoButtonsAlertDialog(tv);
                            return true;
                        }
                    });
                    commentList.add(tv);
                }
                if(tvReviews.getChildCount()==0)
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

    private void createTwoButtonsAlertDialog(TextView tv) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReviewsActivity.this);
        builder.setTitle("удалить отзыв?");
        builder.setNegativeButton("нет",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        tv.setBackgroundColor(Color.TRANSPARENT);
                    }
                });
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        mDatabase.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot snap : snapshot.getChildren()
                                ) {
                                    if (snap.getKey().equals(tv.getHint())){
                                        mDatabase.child(snap.getKey()).removeValue();
                                        Snackbar.make(tv, "удалено", Snackbar.LENGTH_LONG).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
        builder.show();
    }
}