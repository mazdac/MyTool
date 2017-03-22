package com.waterfairy.tool.wifisocket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.waterfairy.tool.R;
import com.waterfairy.wifi.listener.OnWifiDeviceSearchListener;
import com.waterfairy.wifi.manger.WifiManager;

import java.util.ArrayList;
import java.util.List;

public class SearchWifiDeviceActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "adaterWIfi";
    private ListView listView;
    private List<String> networkInterfaces;
    private WifiManager wifiManager;
    private WifiSearchAdapter baseAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_wifi_device);

        listView = (ListView) findViewById(R.id.list_view);
        networkInterfaces = new ArrayList<>();
        baseAdapter = new WifiSearchAdapter(this, networkInterfaces);
        listView.setAdapter(baseAdapter);
        wifiManager = WifiManager.getInstance();
        listView.setOnItemClickListener(this);
        search();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(1, Menu.FIRST, 1,"搜索");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        search();
        return super.onOptionsItemSelected(item);
    }

    public void search() {
        wifiManager.searchWifi(this, "sport_request", "sport_return", new OnWifiDeviceSearchListener() {
            @Override
            public void onSearch(final String ipAddress) {
                Log.i(TAG, "onSearch: 搜索到 wifi 服务器" + ipAddress);


            }

            @Override
            public void onRightSearch(final String ipAddress) {
                Log.i(TAG, "onSearch: 搜索到 指定 wifi 服务器" + ipAddress);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        networkInterfaces.add(ipAddress);
                        baseAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onSearchFinish() {
                Log.i(TAG, "onSearch: 搜索结束");
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String ipAddress = networkInterfaces.get(position);
        Intent intent = new Intent();
        intent.putExtra("ipAddress", ipAddress);
        setResult(RESULT_OK, intent);
        finish();

    }

}
