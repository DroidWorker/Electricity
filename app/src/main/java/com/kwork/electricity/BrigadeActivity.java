package com.kwork.electricity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwork.electricity.DataClasses.Brigade;
import com.kwork.electricity.adapters.BrigadesAdapter;
import com.kwork.electricity.utils.CheckInternet;

import java.util.ArrayList;
import java.util.List;

public class BrigadeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;

    List<Brigade> brigades = new ArrayList<Brigade>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brigade);
        setTitle("БРИГАДЫ");

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance("https://electroseti-9a632-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("brigades");

        RecyclerView rv = findViewById(R.id.brigadesList);
        BrigadesAdapter adapter = new BrigadesAdapter(this, brigades);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                brigades.add(new Brigade(snapshot.getKey().toString(), snapshot.child("brigadeStatus").getValue().toString()));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

    public void onBrigadeAdd(View view){
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.add_brigade_dialog, null);
        AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(this);
        mDialogBuilder.setView(promptsView);
        final EditText userInput = (EditText) promptsView.findViewById(R.id.input_text);
        final EditText userInput2 = (EditText) promptsView.findViewById(R.id.input_text2);
        userInput2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userInput2.getText().toString().matches("^(?=.*?[A-ZА-Я])(?=.*?[a-zа-я])(?=.*?[0-9]).{8,}$")){
                    userInput2.setBackgroundColor(Color.TRANSPARENT);
                }
                else
                    userInput2.setBackgroundColor(Color.RED);
            }
        });
        mDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                mDatabase.child(userInput.getText().toString()).child("brigadeStatus").setValue("бригада свободна");
                                mDatabase.child(userInput.getText().toString()).child("password").setValue(userInput2.getText().toString());
                            }
                        })
                .setNegativeButton("Отмена",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = mDialogBuilder.create();
        alertDialog.show();
    }
}