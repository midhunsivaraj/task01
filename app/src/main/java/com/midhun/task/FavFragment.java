package com.midhun.task;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.baoyz.widget.PullRefreshLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class FavFragment  extends Fragment {

    ListView lv;
    ArrayList<FavModal> cryptoList;
    FavAdapter adapter;
    PullRefreshLayout layout;

    public FavFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vw=inflater.inflate(R.layout.fragment_fav, container, false);
        lv = vw.findViewById(R.id.ListViewFav);

        cryptoList = new ArrayList<FavModal>();
        FetchFavourites();
        adapter = new FavAdapter(getActivity(), R.layout.row_item, cryptoList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getActivity(), DetailActivity.class);
                i.putExtra("key",cryptoList.get(position).getFname());
                startActivity(i);
            }
        });
        layout = vw.findViewById(R.id.swipeRefreshLayout);
        layout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);

        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("REFRESH", "STARTED");
                cryptoList.clear();
                cryptoList = new ArrayList<FavModal>();
                adapter.notifyDataSetChanged();
                FetchFavourites();
                adapter = new FavAdapter(getActivity(), R.layout.row_item, cryptoList);
                lv.setAdapter(adapter);
                layout.setRefreshing(false);
            }
        });

        return  vw;}

    private void FetchFavourites() {
        SharedPreferences favprefs = getActivity().getSharedPreferences("favdata",0);
        SharedPreferences fullprefs = getActivity().getSharedPreferences("fulldata",0);

        Map<String, ?> allEntries = favprefs.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            if(entry.getValue().toString().equals("true")){
                String favcdata = fullprefs.getString(entry.getKey(), "default value");
                try {
                    JSONObject jsonObject = new JSONObject(favcdata);
                    FavModal cmdl = new FavModal();

                    cmdl.setFname(jsonObject.getString("symbol"));
                    cmdl.setFfullname(jsonObject.getString("name"));
                    cryptoList.add(cmdl);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
