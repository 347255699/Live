package org.live.module.publish.view.custom;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.live.R;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by KAM on 2017/4/25.
 */

public class BlackListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    public List<Map<String, String>> data; // 数据源
    private Context context;

    public BlackListAdapter(Context context, List<Map<String, String>> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup arg2) {
        if (view == null) {
            view = inflater.inflate(R.layout.item_black_list, null);
        }

        TextView nickname = (TextView) view.findViewById(R.id.tv_black_list_nickname);
        Button btn = (Button) view.findViewById(R.id.btn_black_list_relieve);


        return view;
    }
}
