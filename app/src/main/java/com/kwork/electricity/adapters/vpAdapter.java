package com.kwork.electricity.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.kwork.electricity.DataClasses.User;
import com.kwork.electricity.R;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class vpAdapter extends PagerAdapter {
    private Context mContext;
    private User user;
    int[] viewNums;

    EditText EtSurname;
    EditText EtName;
    EditText EtPatronymic;
    EditText EtPhone;
    EditText EtEmail;

    public vpAdapter(Context context, int[] nums, User user) {
        this.mContext = context;
        viewNums= nums;
        this.user = user;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView;
        if (position==0) {
        itemView=inflater.inflate(R.layout.page1, container,
                    false);
            EtSurname =(EditText) itemView.findViewById(R.id.editTextTextPersonName);
            EtName =(EditText) itemView.findViewById(R.id.editTextTextPersonName2);
            EtPatronymic =(EditText) itemView.findViewById(R.id.editTextTextPersonName3);
            EtPhone =(EditText) itemView.findViewById(R.id.editTextPhone);
            EtEmail =(EditText) itemView.findViewById(R.id.editTextTextEmailAddress);
            Button but = itemView.findViewById(R.id.button);
            but.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (EtSurname.getText()!=null&&!EtSurname.getText().toString().equals(user.getSurname()))
                        user.addUserInfo("userSurname", EtSurname.getText().toString());
                    if (EtName.getText()!=null&&!EtName.getText().toString().equals(user.getName()))
                        user.addUserInfo("userName", EtName.getText().toString());
                    if (EtPatronymic.getText()!=null&&!EtPatronymic.getText().toString().equals(user.getPatronymic()))
                        user.addUserInfo("userPatronymic", EtPatronymic.getText().toString());
                    if (EtPhone.getText()!=null&&!EtPhone.getText().toString().equals(user.getPhone()))
                        user.addUserInfo("userPhone", EtPhone.getText().toString());
                    if (EtEmail.getText()!=null&&!EtEmail.getText().toString().equals(user.getEmail()))
                        user.addUserInfo("userEmail", EtEmail.getText().toString());
                }
            });
            user.loadUser(EtSurname,EtName,EtPatronymic,EtPhone,EtEmail);
        }
        else{
            itemView=inflater.inflate(R.layout.page2, container,
                    false);
            RecyclerView rv = itemView.findViewById(R.id.ordersView);
            MyOrdersAdapter adapter = new MyOrdersAdapter(mContext, Arrays.asList("aaaaaa", "bbbbbbb"));
            rv.setAdapter(adapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv.setLayoutManager(linearLayoutManager);
        }
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        return;
    }
}
