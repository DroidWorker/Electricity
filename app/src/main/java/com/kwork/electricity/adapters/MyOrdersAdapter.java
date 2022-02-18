package com.kwork.electricity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.kwork.electricity.R;

import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<String> states;

    MyOrdersAdapter(Context context, List<String> states) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public MyOrdersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.myorder_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyOrdersAdapter.ViewHolder holder, int position) {
        holder.nameView.setText(states.get(position));
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameView;
        ViewHolder(View view){
            super(view);
            nameView = view.findViewById(R.id.textView5);
        }
    }
}
