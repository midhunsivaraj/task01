package com.midhun.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CryptoAdapter extends ArrayAdapter<CryptoModal> {
    ArrayList<CryptoModal> cryptoList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public CryptoAdapter(Context context, int resource, ArrayList<CryptoModal> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        cryptoList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            holder.tvName = v.findViewById(R.id.name);
            holder.tvFullname = v.findViewById(R.id.fullname);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.tvName.setText(cryptoList.get(position).getName());
        holder.tvFullname.setText(cryptoList.get(position).getFullname());
        return v;

    }

    static class ViewHolder {
        public TextView tvName;
        public TextView tvFullname;

    }
}
