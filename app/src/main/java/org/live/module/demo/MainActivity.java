package org.live.module.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.live.R;
import org.live.common.constants.LiveKeyConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";
    private ListView mDemoListView = null;

    private static final String[] M_ITEM_TITLES = {
            "推流界面",
            "录屏界面",
            "播放界面",
            "播放demo"
    }; // 定义列表标题
    private static final String[] M_ITEM_ACTIVITY_NAMES = {
            "org.live.module.publish.view.impl.PublishActivity",
            "org.live.module.capture.view.impl.CaptureActivity",
            "org.live.module.play.view.impl.PlayActivity",
            "org.live.module.demo.PlayActivity"
    }; // 定义目标界面Activity
    private static final String[] M_PARAMS = {
            "rtmp://123.207.19.234/live/stream01",
            "rtmp://123.207.19.234/live/stream01",
            "rtmp://123.207.19.234/live/stream01",
            "rtmp://123.207.19.234/live/stream01"
    }; // 定义需要携带至目标界面Activity的参数，若无携带参数则添加null


    private ListItemClickListener listener = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listener = new ListItemClickListener(); // 初始化监听类
        initUIElements();

    }

    /**
     * 初始化控件
     */
    private void initUIElements() {
        mDemoListView = (ListView) findViewById(R.id.lv_demo);

        SimpleAdapter adapter = new SimpleAdapter(this,
                getData(),//数据源
                R.layout.item_listview_demo,//显示布局
                new String[]{"content"}, //数据源的属性字段
                new int[]{R.id.tv_demo_item_name}); //布局里的控件id
        //添加并且显示
        mDemoListView.setAdapter(adapter);
        mDemoListView.setOnItemClickListener(listener);
    }

    /**
     * 获取数据
     *
     * @return
     */
    private List<HashMap<String, String>> getData() {

        List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < M_ITEM_TITLES.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("content", M_ITEM_TITLES[i]);
            list.add(map);
        }
        return list;
    }

    /**
     * 列表项监听类
     */
    private class ListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {

            Log.i("MainLog", "item clicked!");
            String className = M_ITEM_ACTIVITY_NAMES[position]; // 目标Activity类名
            String url = M_PARAMS[position]; // 目标Activity所需要的参数
            try {
                Class<?> clazz = Class.forName(className);
                Object object = clazz.newInstance();
                Intent intent = new Intent(MainActivity.this, object.getClass());
                intent.putExtra(LiveKeyConstants.Global_URL_KEY, url); // 携带参数, 目标Activity用LiveKeyConstants.Global_URL_KEY取出参数
                startActivity(intent); // 跳转到目标界面
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
