package com.kwork.electricity.DataClasses;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Order {
    public String userUid;
    public String orderId;
    public String adress;
    public String brigadeId;
    public String comment;
    public String date;
    public String serviceType;
    public String status;

    static public void uppointOrder(){
        ArrayList<String> freeBrigadesID = new ArrayList<>();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://electroseti-9a632-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        mDatabase.child("brigades").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap:snapshot.getChildren()
                     ) {
                    if (snap.child("brigadeStatus").getValue().toString().equals("бригада свободна"))
                        freeBrigadesID.add(snap.getKey());
                }
                setOrderToBrigade(freeBrigadesID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    static void setOrderToBrigade(ArrayList<String> freeBrigadesId){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://electroseti-9a632-default-rtdb.europe-west1.firebasedatabase.app/").getReference();
        mDatabase.child("orders").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Date currentDate = new Date();
                DateFormat dateFormat = new SimpleDateFormat("dd MM yyyy", Locale.getDefault());
                String dateText = dateFormat.format(currentDate);
                for (DataSnapshot snap:snapshot.getChildren()
                     ) {
                    if (freeBrigadesId.size()!=0){
                        if (snap.child("status").getValue().toString().equals("в ожидании")&&snap.child("date").getValue().toString().equals(dateText)){
                            mDatabase.child("orders").child(snap.getKey()).child("brigadeId").setValue(freeBrigadesId.get(freeBrigadesId.size()-1));
                            mDatabase.child("orders").child(snap.getKey()).child("status").setValue("бригада назначена");
                            mDatabase.child("brigades").child(freeBrigadesId.get(freeBrigadesId.size()-1)).child("brigadeStatus").setValue("заказ получен");
                            freeBrigadesId.remove(freeBrigadesId.size()-1);
                        }
                    }
                    else break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
