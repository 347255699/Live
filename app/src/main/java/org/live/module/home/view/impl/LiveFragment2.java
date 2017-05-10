package org.live.module.home.view.impl;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.live.R;
import org.live.common.constants.LiveConstants;
import org.live.common.constants.LiveKeyConstants;
import org.live.common.listener.NoDoubleClickListener;
import org.live.common.util.BackThread;
import org.live.module.anchor.presenter.AnchorInfoPresenter;
import org.live.module.anchor.presenter.impl.AnchorInfoPresenterImpl;
import org.live.module.anchor.view.AnchorInfoView;
import org.live.module.anchor.view.impl.AnchorInfoActivity;
import org.live.module.capture.view.impl.CaptureActivity;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.listener.OnHomeActivityEventListener;
import org.live.module.home.view.LiveView;
import org.live.module.login.domain.MobileUserVo;
import org.live.module.publish.view.impl.PublishActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 主播直播模块
 * Created by KAM on 2017/4/22.
 */

public class LiveFragment2 extends Fragment {
    private View view;
    private MobileUserVo.LiveRoomInUserVo liveRoomInUserVo;
    private List<Map<String, Object>> data;
    /**
     * 房间封面更换按钮
     */
    private LinearLayout lLiveRoomCoverLinearLayout;
    /**
     * 主播信息列表
     */
    private ListView lAnchorInfoListView;
    /**
     * 主播封面视图
     */
    private ImageView lAnchorCoverImageView;

    /**
     * 单选按钮组
     */
    private RadioGroup lLivingTypeRadioGroup;
    /**
     * 开始直播按钮
     */
    private Button lStartLivingButton;

    private String[] labels = {"房间号", "房间名", "分类名称", "个性签名"}; // 主播信息标签
    private String[] vals; // 主播信息

    private OnHomeActivityEventListener homeActivityEventListener;
    private SimpleAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.liveRoomInUserVo = HomeActivity.mobileUserVo.getLiveRoomVo();
        if (getActivity() instanceof OnHomeActivityEventListener) {
            this.homeActivityEventListener = (OnHomeActivityEventListener) getActivity();
        }
        view = inflater.inflate(R.layout.fragment_live2, null);
        initUIElement();
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    /**
     * 初始化UI控件
     */
    private void initUIElement() {
        lAnchorInfoListView = (ListView) view.findViewById(R.id.lv_anchor_info);
        lAnchorCoverImageView = (ImageView) view.findViewById(R.id.iv_anchor_cover);
        lLiveRoomCoverLinearLayout = (LinearLayout) view.findViewById(R.id.ll_live_cover);
        lLivingTypeRadioGroup = (RadioGroup) view.findViewById(R.id.rg_living_type);
        lStartLivingButton = (Button) view.findViewById(R.id.btn_live_start_living);
        getData();
        adapter = new SimpleAdapter(getActivity(), data, R.layout.item_user_info, new String[]{"label", "val"}, new int[]{R.id.tv_user_info_label, R.id.tv_user_info_val});
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
                    Intent intent = new Intent(getActivity(), AnchorInfoActivity.class);
                    intent.putExtra("key", key);
                    intent.putExtra("val", val);
                    startActivity(intent); // 跳转至主播信息修改窗口
                }
            }
        }); // 绑定列表选项
        Glide.with(this).load(LiveConstants.REMOTE_SERVER_HTTP_IP + liveRoomInUserVo.getRoomCoverUrl())
                .into(lAnchorCoverImageView); // 设置主播封面

        lAnchorCoverImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), LiveRoomCoverActivity.class)); // 跳转至直播间封面查看窗口
            }
        }); // 绑定房间封面点击事件
        lLiveRoomCoverLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHeadImgChooseTypeDialog();
            }
        }); // 绑定房间封面选项点击事件
        lStartLivingButton.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                homeActivityEventListener.checkLiveRoomIsBan();
            }
        }); // 绑定开始直播按钮

    }

    /**
     * 显示头像获取方式对话框
     */
    private void showHeadImgChooseTypeDialog() {
        final DialogPlus dialog = DialogPlus.newDialog(getActivity()).setContentBackgroundResource(R.color.colorWhite2)
                .setContentHolder(new ViewHolder(R.layout.dialog_me_choose_type))
                .setGravity(Gravity.CENTER)
                .create();
        dialog.show();
        final View dialogView = dialog.getHolderView();
        dialogView.findViewById(R.id.btn_me_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivityEventListener.chooseImgFromGallery(HomeConstants.GALLERY_RESULT_CODE + HomeConstants.LIVE_ROOM_COVER);
                dialog.dismiss();
            }
        });// 从相册中选取
        dialogView.findViewById(R.id.btn_me_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivityEventListener.chooseImgFromCamera(HomeConstants.CAMERA_RESULT_CODE + HomeConstants.LIVE_ROOM_COVER, "roomCover.jpg");
                dialog.dismiss();
            }
        });// 拍摄照片
    }

    /**
     * 获取主播数据
     */
    private void getData() {
        data = new ArrayList<>(4);
        this.vals = new String[]{HomeActivity.mobileUserVo.getLiveRoomVo().getRoomNum(), HomeActivity.mobileUserVo.getLiveRoomVo().getRoomName(), HomeActivity.mobileUserVo.getLiveRoomVo().getCategoryName(), HomeActivity.mobileUserVo.getLiveRoomVo().getDescription()}; // 主播信息
        for (int i = 0; i < vals.length; i++) {
            Map<String, Object> map = new HashMap<>();
            String label = labels[i];
            String val = vals[i];
            map.put("label", label);
            map.put("val", val);
            data.add(map);
        }
    }

    /**
     * 获取房间封面视图
     *
     * @return
     */
    public ImageView getAnchorCoverImageView() {
        return this.lAnchorCoverImageView;
    }

    /**
     * 刷新数据
     */
    public void refreshData() {
        data.clear();
        this.vals = new String[]{HomeActivity.mobileUserVo.getLiveRoomVo().getRoomNum(), HomeActivity.mobileUserVo.getLiveRoomVo().getRoomName(), HomeActivity.mobileUserVo.getLiveRoomVo().getCategoryName(), HomeActivity.mobileUserVo.getLiveRoomVo().getDescription()}; // 主播信息
        for (int i = 0; i < vals.length; i++) {
            Map<String, Object> map = new HashMap<>(1);
            String label = labels[i];
            String val = vals[i];
            map.put("label", label);
            map.put("val", val);
            data.add(map);
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 进入直播间
     */
    public void intoLiveRoom(boolean isBan) {
        if (isBan) {
            int rbId = lLivingTypeRadioGroup.getCheckedRadioButtonId();
            int type = (rbId == R.id.rb_capture) ? HomeConstants.LIVING_TYPE_CAPTURE : HomeConstants.LIVING_TYPE_GENERAL;
            Intent intent;
            if (type == HomeConstants.LIVING_TYPE_CAPTURE) {
                intent = new Intent(getActivity(), CaptureActivity.class); // 录屏直播
            } else {
                intent = new Intent(getActivity(), PublishActivity.class); // 普通直播
            }
            intent.putExtra(LiveKeyConstants.Global_URL_KEY, liveRoomInUserVo.getLiveRoomUrl()); // 推流地址
            startActivity(intent); // 进入直播间
        } else {
            Toast.makeText(getActivity(), "您已被禁播", Toast.LENGTH_SHORT).show();
        }
    }
}
