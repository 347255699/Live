package org.live.module.home.view.custom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 用户信息列表视图适配器
 * Created by KAM on 2017/4/18.
 */

public class UserInfoListViewAdapter extends BaseAdapter {
    private Context context;
    private String[] labels; // 标签
    private String[] vals; // 数值

    public UserInfoListViewAdapter(Context context, String[] labels, String[] vals) {
        this.context = context;
        this.labels = labels;
        this.vals = vals;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(null, null);
        } // 填充视图
        return null;
    }
}
