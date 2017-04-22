package org.live.module.home.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bumptech.glide.Glide;

import org.live.R;
import org.live.common.constants.LiveConstants;
import org.live.module.anchor.view.impl.AnchorInfoActivity;
import org.live.module.home.view.LiveView;
import org.live.module.login.domain.MobileUserVo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 主播直播模块
 * Created by KAM on 2017/4/22.
 */

public class LiveFragment2 extends Fragment implements LiveView {
    private View view;
    private MobileUserVo.LiveRoomInUserVo liveRoomInUserVo;
    private MobileUserVo mobileUserVo;
    /**
     * 主播信息列表
     */
    private ListView lAnchorInfoListView;
    /**
     * 主播封面视图
     */
    private ImageView lAnchorCoverImageView;
    private String[] labels = {"房间号", "房间名", "分类名称", "个性签名"}; // 主播信息标签
    private String[] vals; // 主播信息

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.liveRoomInUserVo = HomeActivity.mobileUserVo.getLiveRoomVo();
        this.mobileUserVo = HomeActivity.mobileUserVo;
        //Log.i("Global", liveRoomInUserVo.getRoomId());
        view = inflater.inflate(R.layout.fragment_live2, null);
        initUIElement();
        return view;
    }

    /**
     * 初始化UI控件
     */
    private void initUIElement() {
        lAnchorInfoListView = (ListView) view.findViewById(R.id.lv_anchor_info);
        lAnchorCoverImageView = (ImageView) view.findViewById(R.id.iv_anchor_cover);
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), getData(), R.layout.item_user_info, new String[]{"label", "val"}, new int[]{R.id.tv_user_info_label, R.id.tv_user_info_val});
        lAnchorInfoListView.setAdapter(adapter);
        lAnchorInfoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1 || position == 3) {
                    String val = "";
                    String key = "";
                    if (position == 1) {
                        val = liveRoomInUserVo.getRoomName();
                        key = "roomName";
                    }
                    if (position == 3) {
                        val = liveRoomInUserVo.getDescription();
                        key = "description";
                    }
                    Intent intent = new Intent();
                    intent.putExtra("key", key);
                    intent.putExtra("val", val);
                    startActivity(new Intent(getActivity(), AnchorInfoActivity.class)); // 跳转至主播信息修改窗口
                }
            }
        }); // 绑定列表选项
        Glide.with(this).load(LiveConstants.REMOTE_SERVER_HTTP_IP + liveRoomInUserVo.getRoomCoverUrl())
                .into(lAnchorCoverImageView); // 设置主播封面
    }


    /**
     * 获取主播数据
     */
    private List<Map<String, Object>> getData() {
        List<Map<String, Object>> data = new ArrayList<>();
        this.vals = new String[]{liveRoomInUserVo.getRoomNum(), liveRoomInUserVo.getRoomName(), liveRoomInUserVo.getCategoryName(), liveRoomInUserVo.getDescription()}; // 主播信息
        for (int i = 0; i < vals.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            String label = labels[i];
            String val = vals[i];
            map.put("label", label);
            map.put("val", val);
            data.add(map);
        }
        return data;
    }

    @Override
    public void closeRefreshing(boolean isAnchor) {

    }

    @Override
    public void showToast(String msg) {

    }
}
