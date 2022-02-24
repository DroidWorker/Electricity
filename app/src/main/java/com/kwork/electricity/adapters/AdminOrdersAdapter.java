package com.kwork.electricity.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kwork.electricity.DataClasses.Order;
import com.kwork.electricity.OrderActivity;
import com.kwork.electricity.R;

import java.util.List;

public class AdminOrdersAdapter extends RecyclerView.Adapter<AdminOrdersAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Order> orders;
    Context ctx;

    private DatabaseReference mDatabase;

    public AdminOrdersAdapter(Context context, List<Order> orders) {
        ctx = context;
        this.orders = orders;
        this.inflater = LayoutInflater.from(context);
        mDatabase = FirebaseDatabase.getInstance("https://electroseti-9a632-default-rtdb.europe-west1.firebasedatabase.app/").getReference();

    }
    @Override
    public AdminOrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.admin_order_item, parent, false);
        return new AdminOrdersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdminOrdersAdapter.ViewHolder holder, int position) {

        Order order = orders.get(position);
        holder.orderID.setText("заказ №"+order.orderId);
        holder.serviceType.setText(order.serviceType);
        holder.brigadeStatus.setText("статус: "+order.status);
        holder.adress.setText("адрес: "+order.adress);
        holder.serviceDate.setText("дата оказания услуги: "+order.date);
        if (order.brigadeId!=null)
            holder.brigadeId.setText("бригада №"+order.brigadeId);
        else holder.brigadeId.setText("бригада № -");
        holder.root.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                createThreeButtonsAlertDialog(order.orderId, order.brigadeId);
                return true;
            }
        });
        mDatabase.child("reviews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap:snapshot.getChildren()
                     ) {
                    if (snap.getKey().equals(order.orderId)){
                        holder.review.setText(snap.child("text").getValue().toString());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mDatabase.child("users").child(order.userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holder.fio.setText(snapshot.child("userSurname").getValue().toString()+" "+snapshot.child("userName").getValue().toString()+" "+snapshot.child("userPatronymic").getValue().toString());
                holder.phone.setText(snapshot.child("userPhone").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView orderID, serviceType, brigadeStatus, serviceDate, adress, fio,  phone, brigadeId, review;
        ConstraintLayout root;
        ViewHolder(View view){
            super(view);
            root = view.findViewById(R.id.rootLayout2);
            orderID = view.findViewById(R.id.orderId2);
            serviceType = view.findViewById(R.id.serviceType2);
            brigadeStatus = view.findViewById(R.id.brigadeStatus2);
            serviceDate = view.findViewById(R.id.serviseDate2);
            adress = view.findViewById(R.id.adress);
            fio = view.findViewById(R.id.fio);
            phone = view.findViewById(R.id.phone);
            brigadeId = view.findViewById(R.id.brigadeId2);
            review = view.findViewById(R.id.review2);
        }
    }

    private void createThreeButtonsAlertDialog(String orderId, String brigadeId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("изменить статус заказа");
        builder.setNegativeButton("заказ отменен",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        mDatabase.child("orders").child(orderId).child("status").setValue("отменен");
                        mDatabase.child("orders").child(orderId).child("brigadeId").removeValue();
                        if (brigadeId!=null)
                            mDatabase.child("brigades").child(brigadeId).child("brigadeStatus").setValue("бригада свободна");
                    }
                });
        builder.setPositiveButton("заказ выполнен",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        mDatabase.child("orders").child(orderId).child("status").setValue("выполнен");
                        if (brigadeId!=null)
                            mDatabase.child("brigades").child(brigadeId).child("brigadeStatus").setValue("бригада свободна");
                        Order.uppointOrder();
                    }
                });
        builder.setNeutralButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                    }
                });

        builder.show();
    }
}

