package com.kwork.electricity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.kwork.electricity.R;

public class vpAdapter extends PagerAdapter {
    private Context mContext;
    int[] viewNums;

    public vpAdapter(Context context, int[] nums) {
        this.mContext = context;
        viewNums= nums;

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
        }
        else{
            itemView=inflater.inflate(R.layout.page2, container,
                    false);
        }
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        return;
    }
}
