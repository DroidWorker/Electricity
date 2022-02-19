package com.kwork.electricity.DataClasses;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User {
    private String surname;
    private String name;
    private String patronymic;
    private String phone;
    private String email;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser currentUser;

    public User(){
        // ...
// Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
// Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance("https://electroseti-9a632-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("users").child(currentUser.getUid());
    }

    public void loadUser(EditText surname, EditText name, EditText patronymic, EditText phone, EditText Email){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (surname!=null&&snapshot.child("userSurname").getValue()!=null){
                    User.this.surname = snapshot.child("userSurname").getValue().toString();
                    surname.setText(snapshot.child("userSurname").getValue().toString());
                }
                if (name!=null&&snapshot.child("userName").getValue()!=null){
                    User.this.name = snapshot.child("userName").getValue().toString();
                    name.setText(snapshot.child("userName").getValue().toString());
                }
                if (patronymic!=null&&snapshot.child("userPatronymic").getValue()!=null){
                    User.this.patronymic = snapshot.child("userPatronymic").getValue().toString();
                    patronymic.setText(snapshot.child("userPatronymic").getValue().toString());
                }
                if (phone!=null&&snapshot.child("userPhone").getValue()!=null){
                    User.this.phone = snapshot.child("userPhone").getValue().toString();
                    phone.setText(snapshot.child("userPhone").getValue().toString());
                }
                if (Email!=null&&snapshot.child("userEmail").getValue()!=null){
                    User.this.email = snapshot.child("userEmail").getValue().toString();
                    Email.setText(snapshot.child("userEmail").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", error.toString());
            }
        });
    }

    public void addUserInfo(String key, String value){
        mDatabase.child(key).setValue(value);
        Log.i("action","added");
    }

    public Boolean isInfoFull(){
        if (surname==null||name==null||patronymic==null||phone==null||email==null)
            return false;
        else return true;
    }
}
