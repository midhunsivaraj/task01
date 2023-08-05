package com.midhun.task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FavAdapter extends ArrayAdapter<FavModal> {
    ArrayList<FavModal> favList;
    LayoutInflater vi;
    int Resource;
    ViewHolder holder;

    public FavAdapter(Context context, int resource, ArrayList<FavModal> objects) {
        super(context, resource, objects);
        vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resource = resource;
        favList = objects;
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
        holder.tvName.setText(favList.get(position).getFname());
        holder.tvFullname.setText(favList.get(position).getFfullname());
        return v;

    }

    static class ViewHolder {
        public TextView tvName;
        public TextView tvFullname;

    }
}
