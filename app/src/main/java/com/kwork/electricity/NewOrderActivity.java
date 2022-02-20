package com.kwork.electricity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewOrderActivity extends AppCompatActivity {

    Spinner typeofservice;
    Spinner dateofservice;
    EditText adress;
    EditText comment;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;

    List<String> services = new ArrayList<>();
    List<String> dateList = new ArrayList<>();
    long brigadesCount = 0;
    String freeBrigadeID = null;

    int serviceSelectedItem = 0;
    int dateSelectedItem = 0;
    int lastOrderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);setTitle("НОВЫЙ ЗАКАЗ");

        services.add("вид услуги");
        dateList.add("выберите  дату оказания услуги");

        typeofservice = findViewById(R.id.typeOfService);
        dateofservice = findViewById(R.id.dateOfService);
        adress = findViewById(R.id.textViewAdress);
        comment = findViewById(R.id.textViewAddComment);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance("https://electroseti-9a632-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        mDatabase.child("lastOrderId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lastOrderID = Integer.parseInt(snapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDatabase.child("brigades").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                brigadesCount = snapshot.getChildrenCount();
                for (DataSnapshot snap:snapshot.getChildren()
                     ) {
                    if (snap.child("brigadeStatus").getValue().toString().equals("бригада свободна")){
                        freeBrigadeID = snap.getKey();
                        break;
                    }
                    else freeBrigadeID = null;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayAdapter adapter = new ArrayAdapter<>(this, R.layout.spinner_item, services);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        typeofservice.setAdapter(adapter);

        mDatabase.child("services").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap:snapshot.getChildren()
                     ) {
                    services.add(snap.getKey());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ArrayAdapter adapterDate = new ArrayAdapter<>(this, R.layout.spinner_item, dateList);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        dateofservice.setAdapter(adapterDate);

        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
        String dateText = dateFormat.format(currentDate);
        mDatabase.child("dates").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    for (DataSnapshot snap : snapshot.getChildren()
                    ) {
                        if ((dateFormat.parse(snap.getKey())).before(currentDate)) {
                            mDatabase.child("dates").child(snap.getKey()).removeValue();
                        } else if (snap.getChildrenCount() < 5) {//4 orders + 1 initLine
                            dateList.add(snap.getKey());
                        }
                    }
                }
                catch (Exception ex ){
                    Log.e("err", ex.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    public void onApplyClick(View view){
        serviceSelectedItem = typeofservice.getSelectedItemPosition();
        dateSelectedItem = dateofservice.getSelectedItemPosition();
        if (serviceSelectedItem!=0&&dateSelectedItem!=0&&adress.getText().toString().length()>5){
            DatabaseReference ref = mDatabase.child("orders").child(String.valueOf(lastOrderID+1));
            if (freeBrigadeID!=null) {
                ref.child("status").setValue("бригада назначена");
                ref.child("brigadeId").setValue(freeBrigadeID);
            }
            else ref.child("status").setValue("в ожидании");
            ref.child("userUID").setValue(currentUser.getUid());
            ref.child("serviceType").setValue(services.get(serviceSelectedItem));
            ref.child("date").setValue(dateList.get(dateSelectedItem));
            ref.child("address").setValue(adress.getText().toString());
            ref.child("comment").setValue(comment.getText().toString());
            mDatabase.child("dates").child(dateList.get(dateSelectedItem)).child(String.valueOf(lastOrderID+1)).setValue("null");
            if (freeBrigadeID!=null)
                mDatabase.child("brigades").child(freeBrigadeID).child("brigadeStatus").setValue("заказ получен");
            mDatabase.child("lastOrderId").setValue(String.valueOf(lastOrderID+1));
            mDatabase.child("users").child(currentUser.getUid()).child("orders").child(String.valueOf(lastOrderID+1)).setValue("null");
            finish();
        }
        else
            Snackbar.make(view, "некорректные данные", Snackbar.LENGTH_LONG).show();
    }
}