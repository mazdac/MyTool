package com.waterfairy.tool.wifisocket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.waterfairy.tool.R;

import java.util.List;

/**
 * Created by water_fairy on 2017/3/14.
 */

public class WifiSearchAdapter extends BaseAdapter {
    private List<String> ipAddresses;
    private Context context;

    public WifiSearchAdapter(Context context, List<String> ipAddresses) {
        this.context = context;
        this.ipAddresses = ipAddresses;
    }

    @Override
    public int getCount() {
        return ipAddresses.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private ViewHolder viewHolder;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.base_list_view_item, parent, false);
            convertView.setTag(new ViewHolder(convertView));
        }
        viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.textView.setText(ipAddresses.get(position));
        return convertView;
    }

    class ViewHolder {
        private TextView textView;

        public ViewHolder(View view) {
            textView = (TextView) view.findViewById(R.id.text);
        }
    }
}
