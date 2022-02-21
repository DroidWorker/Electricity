package com.kwork.electricity.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.kwork.electricity.DataClasses.Order;
import com.kwork.electricity.OrderActivity;
import com.kwork.electricity.R;

import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Order> orders;
    Context ctx;

    MyOrdersAdapter(Context context, List<Order> orders) {
        ctx = context;
        this.orders = orders;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public MyOrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.myorder_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyOrdersAdapter.ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.orderID.setText("заказ №"+order.orderId);
        holder.serviceType.setText(order.serviceType);
        holder.brigadeStatus.setText("статус: "+order.status);
        holder.serviceDate.setText("дата оказания услуги: "+order.date);
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, OrderActivity.class);
                intent.putExtra("id",order.orderId);
                intent.putExtra("status", order.status);
                intent.putExtra("service",order.serviceType);
                intent.putExtra("date",order.date);
                if (order.comment!=null&&order.comment.length()>0)
                    intent.putExtra("comment",order.comment);
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView orderID, serviceType, brigadeStatus, serviceDate;
        ConstraintLayout root;
        ViewHolder(View view){
            super(view);
            root = view.findViewById(R.id.rootOrder);
            orderID = view.findViewById(R.id.orderId);
            serviceType = view.findViewById(R.id.serviceType);
            brigadeStatus = view.findViewById(R.id.brigadeStatus);
            serviceDate = view.findViewById(R.id.serviseDate);
        }
    }
}
