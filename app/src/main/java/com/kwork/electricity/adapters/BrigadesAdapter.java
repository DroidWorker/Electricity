package com.kwork.electricity.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kwork.electricity.DataClasses.Brigade;
import com.kwork.electricity.OrdersActivity;
import com.kwork.electricity.R;

import java.util.List;

public class BrigadesAdapter extends RecyclerView.Adapter<BrigadesAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Brigade> states;
    private final Context ctx;

    private Boolean isSelected = false;

    public BrigadesAdapter(Context context, List<Brigade> states) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
        this.ctx = context;
    }
    @Override
    public BrigadesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.brigade_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BrigadesAdapter.ViewHolder holder, int position) {
        Brigade brigade = states.get(position);
        holder.id.setText("бригада №"+brigade.getBrigadeId());
        holder.status.setText("статус: "+brigade.getBrigadeStatus());
        holder.showAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, OrdersActivity.class);
                intent.putExtra("brigadeID", brigade.getBrigadeId());
                intent.putExtra("mode", 0);//mode 0- open orders of current brigade|mode 1 open all orders
                ctx.startActivity(intent);
            }
        });
        holder.cl.setOnLongClickListener(new View.OnLongClickListener() {
            AlertDialog.Builder dialog = new AlertDialog.Builder(ctx);

            @Override
            public boolean onLongClick(View v) {
                if (!isSelected) {
                    holder.cl.setBackgroundColor(Color.RED);
                    isSelected = true;
                    createAlertDialog(holder.cl, brigade.getBrigadeId());
                    return true;
                }
                else
                    return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView id, status, showAll;
        ConstraintLayout cl;
        ViewHolder(View view){
            super(view);
            cl = view.findViewById(R.id.back);
            id = view.findViewById(R.id.textViewBrigadeID);
            status = view.findViewById(R.id.textViewBrigadeStatus);
            showAll = view.findViewById(R.id.textViewShowOrders);
        }
    }

    private void createAlertDialog(ConstraintLayout cl, String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("удалить бригаду?");
        builder.setPositiveButton("удалить",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        DatabaseReference mDatabase = FirebaseDatabase.getInstance("https://electroseti-9a632-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("brigades");
                        mDatabase.child(id).removeValue();
                        isSelected=false;
                    }
                });
        builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cl.setBackgroundColor(Color.TRANSPARENT);
                isSelected = false;
            }
        });
        builder.show();
    }
}
