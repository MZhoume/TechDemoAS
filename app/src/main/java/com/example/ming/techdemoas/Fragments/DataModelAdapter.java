package com.example.ming.techdemoas.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ming.techdemoas.R;
import com.example.ming.techdemoas.Services.DataModel;

import java.util.List;

public class DataModelAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<DataModel> mDataModels;

    private LayoutInflater mInflater;

    public DataModelAdapter(Context context, List<DataModel> models) {
        mContext = context;
        mDataModels = models;

        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDataModels.size();
    }

    @Override
    public Object getItem(int position) {
        return mDataModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.data_model_display, parent);

            LinearLayout linearNames = (LinearLayout) convertView.findViewById(R.id.linearDMNames);
            LinearLayout linearValues = (LinearLayout) convertView.findViewById(R.id.linearDMValues);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;

            int i = 10;

            for (String n : mDataModels.get(position).getNames()) {
                TextView txt = new TextView(mContext);
                txt.setText(n);
                txt.setId(i++);
                linearNames.addView(txt, params);
            }

            i = 20;
            for (double d : mDataModels.get(position).getValues()) {
                TextView txt = new TextView(mContext);
                txt.setText(String.valueOf(d));
                txt.setId(i++);
                linearValues.addView(txt, params);
            }
        } else {
            int i = 20;
            for (double d : mDataModels.get(position).getValues()) {
                TextView txt = (TextView) convertView.findViewById(i++);
                txt.setText(String.valueOf(d));
            }
        }

        return convertView;
    }
}
