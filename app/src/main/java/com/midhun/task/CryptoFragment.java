package com.midhun.task;

import static android.content.Context.MODE_PRIVATE;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.baoyz.widget.PullRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CryptoFragment extends Fragment {

    ListView lv;
    ArrayList<CryptoModal> cryptoList;
    CryptoAdapter adapter;
    PullRefreshLayout layout;
    Button loadmore;

    public CryptoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vw=inflater.inflate(R.layout.fragment_crypto, container, false);
        lv = vw.findViewById(R.id.ListViewCrypto);
        loadmore = vw.findViewById(R.id.more);
        layout = vw.findViewById(R.id.swipeRefreshLayout);
        layout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("REFRESH", "STARTED");
                cryptoList.clear();
                cryptoList = new ArrayList<CryptoModal>();
                adapter.notifyDataSetChanged();
                new JSONAsynTask().execute("https://pro-api.coinmarketcap.com/v1/cryptocurrency/map?start=1&limit=25");
                adapter = new CryptoAdapter(getActivity(), R.layout.row_item, cryptoList);
                lv.setAdapter(adapter);
                layout.setRefreshing(false);
            }
        });
        cryptoList = new ArrayList<CryptoModal>();
        new JSONAsynTask().execute("https://pro-api.coinmarketcap.com/v1/cryptocurrency/map?start=1&limit=25");
        adapter = new CryptoAdapter(getActivity(), R.layout.row_item, cryptoList);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent i = new Intent(getActivity(), DetailActivity.class);
                i.putExtra("key",cryptoList.get(position).getName());
                startActivity(i);
            }
        });


        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, final int totalItemCount) {

                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    Log.e("Scroll", "total="+totalItemCount);
                    loadmore.setVisibility(View.VISIBLE);
                    loadmore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            loadmore.setVisibility(View.GONE);
                            new JSONAsynTask().execute("https://pro-api.coinmarketcap.com/v1/cryptocurrency/map?start="+totalItemCount+"&limit=25");
                        }
                    });
                }
                else{
                    loadmore.setVisibility(View.GONE);
                }
            }
        });

        return  vw;
    }

    class JSONAsynTask extends AsyncTask<String, Void, Boolean> {
        ProgressDialog dialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading, please wait");
            dialog.setTitle("Connecting CoinMarketCap");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);


                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("X-CMC_PRO_API_KEY", "2592e201-7cb0-41b4-81d5-abacc60ac4ee");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                String stringResult = buffer.toString();

                if (urlConnection.getResponseCode() == 200) {

                    JSONObject jsonObject = new JSONObject(stringResult);
                    JSONArray jarray = jsonObject.getJSONArray("data");
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("fulldata", MODE_PRIVATE).edit();

                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject object = jarray.getJSONObject(i);

                        CryptoModal cmdl = new CryptoModal();

                        cmdl.setName(object.getString("symbol"));
                        cmdl.setFullname(object.getString("name"));
                        editor.putString(object.getString("symbol"), object.toString());
                        editor.commit();
                        cryptoList.add(cmdl);
                    }
                    return true;

                } else {

                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return false;

        }

        protected void onPostExecute(Boolean result) {

            dialog.dismiss();
            adapter.notifyDataSetChanged();
            //SharedPreferences preferences = getActivity().getSharedPreferences("fulldata",0);
            //String myBackgroundPreference = preferences.getString("BTC", "default value");
            if(result == false)
                Toast.makeText(getActivity(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();

        }
    }
}