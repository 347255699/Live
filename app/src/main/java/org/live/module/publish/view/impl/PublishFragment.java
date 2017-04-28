package org.live.module.publish.view.impl;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.appyvet.rangebar.RangeBar;

import org.live.R;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.suke.widget.SwitchButton;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.ui.TXCloudVideoView;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import org.live.common.constants.LiveConstants;
import org.live.common.domain.MessageType;
import org.live.common.listener.BackHandledFragment;
import org.live.common.listener.NoDoubleClickListener;
import org.live.common.util.JsonUtils;
import org.live.common.util.NetworkUtils;
import org.live.common.util.ResponseModel;
import org.live.module.chat.service.AnchorChatService;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.domain.AppAnchorInfo;
import org.live.module.home.presenter.LiveRoomPresenter;
import org.live.module.home.view.custom.AnchorInfoDialogView;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.login.domain.MobileUserVo;
import org.live.module.publish.domain.LimitationVo;
import org.live.module.publish.listener.OnPublishActivityListener;
import org.live.module.publish.presenter.PublishPresenter;
import org.live.module.publish.presenter.impl.PublishPresenterImpl;
import org.live.module.publish.util.constant.PublishConstant;
import org.live.module.publish.view.PublishView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * 推流模块
 * Created by KAM on 2017/2/27.
 */

public class PublishFragment extends BackHandledFragment implements PublishView {
    private static final String TAG = "PublishFragment";
    private View view = null;
    private IconButtonOnClickListener listener = null;
    private static final int ALPHHA_DEFAULT_VALUE = 50; // 默认透明度
    private TXCloudVideoView iPreviewVideoView = null; // 直播预览视图
    private PublishPresenter recorderPresenter = null;
    private String rtmpUrl = null; // 测试用例
    private DialogPlus dialog = null; // 对话框

    private String pDefinitionInfos[] = {"标清", "高清", "超清"};

    private boolean isRecording = false; // 正在直播
    private boolean isFrontCamera = true; // 开启前置摄像头
    private boolean isFlashOn = false; // 开启闪光灯

    private MaterialIconView iRecordingStatusButton = null; // 直播状态转换按钮
    private MaterialIconView iCameraSwitchButton = null; // 摄像头转换按钮
    private MaterialIconView iBeautyButton = null; // 美颜按钮
    private MaterialIconView iflashStatusButton = null; // 闪光灯转台转换按钮
    private MaterialIconView iVolumeSettingsButton = null; // 麦克风转台转换按钮
    private MaterialIconView iRecordSettingsButton = null; // 直播设置按钮
    private MaterialIconView iRecordCloseButton = null; // 主播界面关闭按钮

    private RangeBar iBeautySettingRangeBar = null; // 磨皮设置拉杆
    private RangeBar iWhiteningSettingRangeBar = null; // 美白设置拉杆
    private RangeBar iMicroPhoneSettingRangeBar = null; // 麦克风音量拉杆
    private RangeBar iVolumeSettingRangeBar = null; // 背景音量设置拉杆

    private SwitchButton iVolumeOffSwitchButton = null; // 静音开关
    private SwitchButton iTouchFocusSwitchButton = null; // 手动对焦开关

    private Spinner pDefinitionSpinner = null; // 清晰度下拉框
    private RelativeLayout pLiveRoomInfoRelativeLayout; // 直播间信息视图
    private MobileUserVo mobileUserVo; // 用户信息
    private ImageView pHeadImgImageView; // 用户头像视图
    private TextView pLiveRoomName; // 直播间名称
    private TextView pOnlineCount; // 在线人数
    private Button pBlockListButton;// 黑名单按钮
    private List<Map<String, Object>> data;

    private Handler handler;

    private LiveRoomPresenter liveRoomPresenter;
    private AnchorInfoDialogView anchorInfoDialogView;     //主播信息弹出框的包裹view
    private BlackListAdapter blackListAdapter; // 黑名单适配器
    private OnPublishActivityListener publishActivityListener;
    private int liftABanUserIndex; // 待解禁用户下标

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recorder, container, false);
        listener = new IconButtonOnClickListener(); // 初始化图标按钮监听事件
        recorderPresenter = new PublishPresenterImpl(this, getActivity());
        PublishActivity publishActivity = (PublishActivity) getActivity();
        this.rtmpUrl = publishActivity.getRtmpUrl(); // 推流地址
        this.mobileUserVo = HomeActivity.mobileUserVo; // 用户信息
        if (getActivity() instanceof OnPublishActivityListener) {
            publishActivityListener = (OnPublishActivityListener) getActivity();
        }
        initUIElements(); // 初始化ui控件
        recorderPresenter.startCameraPreview(); // 开始预览

        newHanderInstance();
        liveRoomPresenter = new LiveRoomPresenter(getActivity(), handler);

        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        registerReceiver(); // 注册广播接收器
    }

    @Override
    public void onStop() {
        super.onStop();
        recorderPresenter.refreshPreferences(); // 刷新参数并持久化
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(chatReceiver); // 注销广播接收器
    }

    /**
     * 初始化ui控件
     */
    private void initUIElements() {
        /** 各控件含义如上成员变量所释 **/
        iRecordingStatusButton = (MaterialIconView) view.findViewById(R.id.btn_recording_status);
        iRecordingStatusButton.getBackground().setAlpha(ALPHHA_DEFAULT_VALUE);
        iCameraSwitchButton = (MaterialIconView) view.findViewById(R.id.btn_camera_switch);
        iCameraSwitchButton.getBackground().setAlpha(ALPHHA_DEFAULT_VALUE);
        iBeautyButton = (MaterialIconView) view.findViewById(R.id.btn_beauty);
        iBeautyButton.getBackground().setAlpha(ALPHHA_DEFAULT_VALUE);
        iflashStatusButton = (MaterialIconView) view.findViewById(R.id.btn_flash_status);
        iflashStatusButton.getBackground().setAlpha(ALPHHA_DEFAULT_VALUE);
        iVolumeSettingsButton = (MaterialIconView) view.findViewById(R.id.btn_volume_settings);
        iVolumeSettingsButton.getBackground().setAlpha(ALPHHA_DEFAULT_VALUE);
        iRecordCloseButton = (MaterialIconView) view.findViewById(R.id.btn_record_close);
        iRecordCloseButton.getBackground().setAlpha(ALPHHA_DEFAULT_VALUE);
        iRecordSettingsButton = (MaterialIconView) view.findViewById(R.id.btn_record_settings);
        iRecordSettingsButton.getBackground().setAlpha(ALPHHA_DEFAULT_VALUE);
        pLiveRoomInfoRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl_recorder_live_room_info);
        pLiveRoomInfoRelativeLayout.getBackground().setAlpha(ALPHHA_DEFAULT_VALUE); // 设置透明度*/

        iRecordingStatusButton.setOnClickListener(listener);
        iCameraSwitchButton.setOnClickListener(listener);
        iBeautyButton.setOnClickListener(listener);
        iflashStatusButton.setOnClickListener(listener);
        iVolumeSettingsButton.setOnClickListener(listener);
        iRecordCloseButton.setOnClickListener(listener);
        iRecordSettingsButton.setOnClickListener(listener);

        iPreviewVideoView = (TXCloudVideoView) view.findViewById(R.id.vv_preview);
        pHeadImgImageView = (ImageView) view.findViewById(R.id.iv_recorder_head_img);
        pLiveRoomName = (TextView) view.findViewById(R.id.tv_recorder_live_room_name);
        pOnlineCount = (TextView) view.findViewById(R.id.tv_recrder_online_count);

        Glide.with(getActivity()).load(LiveConstants.REMOTE_SERVER_HTTP_IP + mobileUserVo.getHeadImgUrl())
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(pHeadImgImageView); // 设置头像
        pLiveRoomName.setText(mobileUserVo.getLiveRoomVo().getRoomName());

        pBlockListButton = (Button) view.findViewById(R.id.btn_recorder_black_list);
        pBlockListButton.getBackground().setAlpha(150);
        pBlockListButton.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                final DialogPlus dialog = DialogPlus.newDialog(getActivity()).setContentBackgroundResource(R.color.colorWhite)
                        .setContentHolder(new ViewHolder(R.layout.dialog_black_list))
                        .setHeader(R.layout.dialog_header)
                        .setContentHeight(650)
                        .setGravity(Gravity.CENTER)
                        .create();
                dialog.show();
                View dialogHeader = dialog.getHeaderView(); // 取得标题栏
                dialogHeader.findViewById(R.id.btn_header_cancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                }); // 绑定取消按钮
                TextView headerTitleTextView = (TextView) dialogHeader.findViewById(R.id.tv_header_title);
                headerTitleTextView.setText("黑名单"); // 设置标题

                View dialogView = dialog.getHolderView(); // 取得内容容器
                ListView blackList = (ListView) dialogView.findViewById(R.id.lv_black_list);
                getData(); // 获取数据
                blackListAdapter = new BlackListAdapter(getActivity());
                blackList.setAdapter(blackListAdapter);
            }
        }); // 黑名单按钮点击

        pLiveRoomInfoRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                liveRoomPresenter.loadAnchorInfoData(mobileUserVo.getUserId(), mobileUserVo.getLiveRoomVo().getRoomId());
            }
        });


    }

    /**
     * 注册服务
     */
    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AnchorChatService.ACTION); // 绑定意图
        filter.setPriority(Integer.MAX_VALUE); // 高优先级
        getActivity().registerReceiver(chatReceiver, filter); // 注册广播接收器
    }

    /**
     * 广播接收器
     */
    private BroadcastReceiver chatReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            org.live.common.domain.Message message = JsonUtils.fromJson(intent.getStringExtra("msg"), org.live.common.domain.Message.class);
            switch (message.getMessageType()) {
                case MessageType.USER_ENTER_CHATROOM_MESSAGE_TYPE:
                    String onlineCount = message.getContent();
                    refreshOnlineCount(onlineCount); // 刷新在线观看人数
                    break; // 用户进入直播间
                case MessageType.USER_EXIT_CHATROOM_MESSAGE_TYPE:
                    String onlineCount2 = message.getContent();
                    refreshOnlineCount(onlineCount2); // 刷新在线观看人数
                    break; // 用户进入直播间
                case MessageType.ANCHOR_EXIT_CHATROOM_MESSAGE_TYPE:
                    recorderPresenter.stopRtmpPublish();   //结束推流服务
                    break; // 主播离开直播间
                default:
                    break;
            }
        }
    };

    private void newHanderInstance() {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == HomeConstants.LOAD_ANCHOR_INFO_SUCCESS_FLAG) {   //加载主播信息成功
                    ResponseModel<AppAnchorInfo> dataModel = (ResponseModel) msg.obj;
                    if (dataModel.getStatus() == 1) {
                        AppAnchorInfo info = dataModel.getData();
                        showAnchorInfoDialog(info);
                    }
                }
            }
        };
    }

    /**
     * 弹出主播信息的弹出框
     *
     * @param info
     */
    private void showAnchorInfoDialog(AppAnchorInfo info) {
        if (anchorInfoDialogView == null)
            anchorInfoDialogView = new AnchorInfoDialogView(getActivity());
        anchorInfoDialogView.getReportView().setVisibility(View.GONE);     //隐藏举报
        anchorInfoDialogView.getAttentionHold().setVisibility(View.GONE);  //隐藏关注按钮
        anchorInfoDialogView.setValueAndShow(info);
    }


    /**
     * 获得数据
     */
    public void getData() {
        List<Map<String, Object>> data = new ArrayList<>();
        recorderPresenter.getBlackListData(); // 获取数据
        this.data = data;
    }

    /**
     * 显示直播暂停图标
     */
    @Override
    public void onShowPauseIconView() {
        iRecordingStatusButton.setIcon(MaterialDrawableBuilder.IconValue.PAUSE);
        isRecording = true;
    }

    /**
     * 显示直播开始图标
     */
    @Override
    public void onShowPlayIconView() {
        iRecordingStatusButton.setIcon(MaterialDrawableBuilder.IconValue.PLAY);
        isRecording = false;
        publishActivityListener.logoutService(); // 注销服务
        publishActivityListener.backLiving(); // 退出直播

    }

    /**
     * 显示提示信息
     *
     * @param msg
     * @param lengthType
     */
    @Override
    public void onShowToastMessage(String msg, Integer lengthType) {
        Toast.makeText(getActivity(), msg, lengthType).show();
    }

    /**
     * 显示闪光灯关闭图标
     */
    @Override
    public void onShowFlashOffIconView() {
        iflashStatusButton.setIcon(MaterialDrawableBuilder.IconValue.FLASH_OFF);
    }

    /**
     * 显示闪光灯开启图标
     */
    @Override
    public void onShowFlashIconView() {
        iflashStatusButton.setIcon(MaterialDrawableBuilder.IconValue.FLASH);
    }

    /**
     * 显示美颜设置对话框
     *
     * @param beauty
     * @param whitening
     */
    @Override
    public void onShowBeautyRangeBarView(Integer beauty, Integer whitening) {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = DialogPlus.newDialog(getActivity()).setBackgroundColorResId(R.color.colorWall)
                .setContentHolder(new ViewHolder(R.layout.dialog_beauty_settings))
                .setExpanded(true, 375).setGravity(Gravity.BOTTOM)
                .create();
        dialog.show();
        initBeautySettingsRangeBar(dialog.getHolderView(), beauty, whitening); // 监听并初始化拉杆

    }

    /**
     * 获取直播预览视图控件
     *
     * @return
     */
    @Override
    public TXCloudVideoView getPreviewVideoView() {
        return iPreviewVideoView;
    }

    /**
     * 显示音量设置对话框
     *
     * @param microphone 麦克风音量
     * @param volume     背景音量
     */
    @Override
    public void onShowVolumeSettingsView(Float microphone, Float volume, boolean isVolumeOff) {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = DialogPlus.newDialog(getActivity())
                .setBackgroundColorResId(R.color.colorWall)
                .setContentHolder(new ViewHolder(R.layout.dialog_volume_settings))
                .setExpanded(true, 550).setGravity(Gravity.BOTTOM)
                .create();
        dialog.show();
        initVolumeSettingsView(dialog.getHolderView(), microphone, volume, isVolumeOff);
    }

    @Override
    public void onRefreshVolumeOffSwitchVal(boolean isVolumeOff) {
        View currentDialogView = dialog.getHolderView();
        iVolumeOffSwitchButton = (SwitchButton) currentDialogView.findViewById(R.id.btn_volume_off_switch);
        iVolumeOffSwitchButton.setChecked(isVolumeOff);
    }

    @Override
    public void onShowPublishSettingsView(Map<String, Object> config) {
        if (dialog != null) {
            dialog.dismiss();
        }
        dialog = DialogPlus.newDialog(getActivity())
                .setBackgroundColorResId(R.color.colorWall)
                .setContentHolder(new ViewHolder(R.layout.dialog_publish_settings))
                .setExpanded(true, 350).setGravity(Gravity.BOTTOM)
                .create();
        dialog.show();
        initPublishSettingsView(dialog.getHolderView(), config);
    }

    /**
     * 刷新在线人数
     *
     * @param count
     */
    @Override
    public void refreshOnlineCount(String count) {
        pOnlineCount.setText(count);
    }

    @Override
    public void refreshBlackList(List<LimitationVo> limitationVos) {
        if (limitationVos.size() > 0) {
            for (LimitationVo limitationVo : limitationVos) {
                Map<String, Object> item = new HashMap<>();
                item.put("nickname", limitationVo.getNickname());
                item.put("account", limitationVo.getAccount());
                item.put("limitType", limitationVo.getLimitType());
                item.put("userId", limitationVo.getUserId());
                item.put("roomNum", limitationVo.getRoomNum());
                data.add(item);
            }
        } else {
            Map<String, Object> item = new HashMap<>();
            item.put("nickname", "无相关内容");
            item.put("account", "0");
            data.add(item);
        }

        blackListAdapter.notifyDataSetChanged(); // 刷新黑名单视图

    }

    @Override
    public boolean onBackPressed() {
        if (isRecording) {
            onShowToastMessage("正在直播，请先关闭直播...", Toast.LENGTH_SHORT);
            return true;
        }
        return false;
    }


    /**
     * 图标按钮点击事件监听
     */
    private class IconButtonOnClickListener extends NoDoubleClickListener {
        @Override
        protected void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.btn_recording_status:     //进行直播和结束直播的按钮
                    if (NetworkUtils.isConnected(getActivity())) {
                        if (!isRecording) {        //
                            if (rtmpUrl != null) {
                                recorderPresenter.startPusher(rtmpUrl);
                            } else {
                                onShowToastMessage("地址出错！", Toast.LENGTH_SHORT);
                            }
                        } else {
                            final MaterialDialog dialog = new MaterialDialog(getActivity());
                            dialog.content("您确定要结束直播吗？ ");
                            dialog.btnNum(2).btnText("取消", "确定").btnTextColor(NormalDialog.STYLE_TWO);
                            dialog.show();
                            dialog.setOnBtnClickL(new OnBtnClickL() {
                                @Override
                                public void onBtnClick() {  //取消
                                    dialog.dismiss();
                                }
                            }, new OnBtnClickL() {
                                @Override
                                public void onBtnClick() {  //确定
                                    dialog.dismiss();
                                    recorderPresenter.stopRtmpPublish();   //结束推流服务
                                }
                            });
                        }
                    } else {
                        onShowToastMessage("网络无法连接", Toast.LENGTH_SHORT);
                    }
                    break;
                case R.id.btn_camera_switch:
                    isFrontCamera = recorderPresenter.switchCamera(isFrontCamera);
                    if (isFrontCamera && isFlashOn) {
                        isFlashOn = false;
                        onShowFlashIconView();
                    }
                    break;
                case R.id.btn_beauty:
                    recorderPresenter.getBeautyAndWhiteningVal();
                    break;
                case R.id.btn_flash_status:
                    // 闪光灯状态
                    isFlashOn = recorderPresenter.switchFlashLight(isFlashOn);
                    break;
                case R.id.btn_volume_settings:
                    recorderPresenter.getVolumeVal();
                    break;
                case R.id.btn_record_close:     //关闭按钮
                    if (isRecording) {
                        onShowToastMessage("正在直播，不能退出当前界面！", Toast.LENGTH_SHORT);
                    } else {
                        getActivity().finish();
                    }
                    break;
                case R.id.btn_record_settings:
                    recorderPresenter.getPushConfig();
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 初始化美颜相关拉杆控件和绑定拉杆滑动事件监听
     *
     * @param rangeBarView 拉杆所在对话框视图
     * @param beauty       磨皮级别
     * @param whitening    美白级别
     */
    private void initBeautySettingsRangeBar(View rangeBarView, Integer beauty, Integer whitening) {
        iBeautySettingRangeBar = (RangeBar) rangeBarView.findViewById(R.id.rb_beauty_setting);
        iWhiteningSettingRangeBar = (RangeBar) rangeBarView.findViewById(R.id.rb_whitening_setting);

        iBeautySettingRangeBar.setSeekPinByIndex(beauty); // 设置磨皮默认值
        iWhiteningSettingRangeBar.setSeekPinByIndex(whitening); // 设置美白默认值
        iBeautySettingRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                Integer beautyVal = Integer.parseInt(rightPinValue);
                recorderPresenter.setBeautyFilter(beautyVal, null); // 设置磨皮
            }
        });
        iWhiteningSettingRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                Integer whiteningVal = Integer.parseInt(rightPinValue);
                recorderPresenter.setBeautyFilter(null, whiteningVal);// 设置美白
            }
        });
    }

    /**
     * 初始化音量设置对话框并绑定拉杆滑动和滑动按钮滑动事件
     *
     * @param volumeSettingsView 音量设置所在的对话框视图
     * @param microPhone         麦克风音量
     * @param volume             背景音量
     */
    private void initVolumeSettingsView(View volumeSettingsView, Float microPhone, Float volume, boolean isVolumeOff) {
        iVolumeOffSwitchButton = (SwitchButton) volumeSettingsView.findViewById(R.id.btn_volume_off_switch);
        iMicroPhoneSettingRangeBar = (RangeBar) volumeSettingsView.findViewById(R.id.rb_microphone_setting);
        iVolumeSettingRangeBar = (RangeBar) volumeSettingsView.findViewById(R.id.rb_volume_setting);
        float microPhoneVal = microPhone;
        float volumeVal = volume;
        iMicroPhoneSettingRangeBar.setSeekPinByIndex((int) microPhoneVal); // 设置麦克风音量默认值
        iVolumeSettingRangeBar.setSeekPinByIndex((int) volumeVal);// 设置背景音量默认值
        iVolumeOffSwitchButton.setChecked(isVolumeOff);
        iMicroPhoneSettingRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                float microPhoneVal = Float.parseFloat(rightPinValue);
                recorderPresenter.setVolumeVal(microPhoneVal / 10, null); // 设置麦克风音量
            }
        });
        iVolumeSettingRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                float volumeVal = Float.parseFloat(rightPinValue);
                recorderPresenter.setVolumeVal(null, volumeVal / 10); // 设置背景音量
            }
        });
        iVolumeOffSwitchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                recorderPresenter.setVolumeOff(isChecked); // 静音开关
            }
        });
    }

    /**
     * 初始化推流参数设置界面
     *
     * @param publishSettingsView
     * @param config
     */
    public void initPublishSettingsView(View publishSettingsView, Map<String, Object> config) {
        pDefinitionSpinner = (Spinner) publishSettingsView.findViewById(R.id.spin_pulish_definition);
        iTouchFocusSwitchButton = (SwitchButton) publishSettingsView.findViewById(R.id.btn_touch_focu_switch);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, pDefinitionInfos); // 建立Adapter并且绑定数据源
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pDefinitionSpinner.setAdapter(adapter); //绑定 Adapter到控件

        iTouchFocusSwitchButton.setChecked((boolean) config.get(PublishConstant.CONFIG_TYPE_TOUCH_FOCUS)); // 设置手动对焦按钮状态
        pDefinitionSpinner.setSelection(mapperSpinnerSelectionPosition((Integer) config.get(PublishConstant.CONFIG_TYPE_VIDEO_QUALITY))); // 设置当前选项
        pDefinitionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                recorderPresenter.setPushConfig(PublishConstant.CONFIG_TYPE_VIDEO_QUALITY, mapperVideoQuality(pos)); // 设置视频清晰度
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        }); // 监听下拉框
        iTouchFocusSwitchButton.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                recorderPresenter.setPushConfig(PublishConstant.CONFIG_TYPE_TOUCH_FOCUS, isChecked); // 设置手动对焦
            }
        }); // 监听手动对焦按钮
    }

    /**
     * 视频清晰度对应下拉框位置
     *
     * @param videoQuality 视频清晰度
     * @return
     */
    private int mapperSpinnerSelectionPosition(int videoQuality) {
        int defalutPosition = 1;
        switch (videoQuality) {
            case TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION:
                defalutPosition = 0;
                break;
            case TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION:
                defalutPosition = 1;
                break;
            case TXLiveConstants.VIDEO_QUALITY_SUPER_DEFINITION:
                defalutPosition = 2;
                break;
            default:
                break;
        }
        return defalutPosition;
    }

    /**
     * 下拉框位置对应的视频清晰度
     *
     * @param position 下拉框位置
     * @return
     */
    private int mapperVideoQuality(int position) {
        int definition = TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION;
        switch (position) {
            case 0:
                definition = TXLiveConstants.VIDEO_QUALITY_STANDARD_DEFINITION;
                break;
            case 1:
                definition = TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION;
                break;
            case 2:
                definition = TXLiveConstants.VIDEO_QUALITY_SUPER_DEFINITION;
                break;
            default:
                break;
        }
        return definition;
    }

/*    *//**
     * 监听系统参数变化
     *
     * @param newConfig
     *//*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        int mobileRotation = this.getActivity().getWindowManager().getDefaultDisplay().getRotation();
        recorderPresenter.onDisplayRotationChanged(mobileRotation);
    }*/


    /**
     * 旋转屏幕时更改推流方向
     */
    public void onDisplayRotationChanged(int mobileRotation) {
        recorderPresenter.onDisplayRotationChanged(mobileRotation);  // 自动旋转打开，Activity随手机方向旋转之后，只需要改变推流方向
    }

    /**
     * 黑明单适配器
     */
    public class BlackListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private Context context;

        public BlackListAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
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
            TextView nicknameTextView = (TextView) view.findViewById(R.id.tv_black_list_nickname);
            Button btn = (Button) view.findViewById(R.id.btn_black_list_relieve);
            Map<String, Object> item = data.get(position);
            String nickname = (String) item.get("nickname");
            String account = (String) item.get("account");
            if (position == 0) {
                if (account.equals("0")) {
                    nicknameTextView.setText(nickname);
                    btn.setVisibility(View.GONE);
                    return view;
                }
            }
            int limitType = (int) item.get("limitType");
            nicknameTextView.setText(nickname); // 设置昵称
            String btnTxt = (limitType == PublishConstant.BLACK_LIST_LIMIT_TYPE_SHUTUP) ? "解除禁言" : "解除踢出";
            btn.setText(btnTxt); // 设置按钮内容
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    liftABanUserIndex = position; // 缓存待解禁用户下标
                    Map<String, Object> item = data.get(position);
                    int limitType = (int) item.get("limitType"); // 解禁类型
                    String roomNum = mobileUserVo.getLiveRoomVo().getRoomNum(); // 房间号
                    String userAccount = (String) item.get("account"); // 待解禁用户账号
                    String userNickname = (String) item.get("nickname"); // 取得待发消息内容项

                    org.live.common.domain.Message message = new org.live.common.domain.Message();
                    message.setAccount(userAccount);
                    message.setFromChatRoomNum(roomNum);
                    if (limitType == PublishConstant.BLACK_LIST_LIMIT_TYPE_SHUTUP) {
                        message.setMessageType(MessageType.RELIEVE_SHUTUP_USER_MESSAGE_TYPE);
                    } else {
                        message.setMessageType(MessageType.RELIEVE_KICKOUT_USER_MESSAGE_TYPE);
                    }
                    message.setDestination(roomNum + "-" + userAccount);
                    message.setNickname(userNickname); // 构建消息

                    publishActivityListener.getChatReceiveServiceBinder().sendMsg(message); // 发送消息
                    data.remove(position);
                    if (data.size() == 0) {
                        Map<String, Object> item2 = new HashMap<>();
                        item2.put("nickname", "无相关内容");
                        item2.put("account", "0");
                        data.add(item2);
                    }
                    blackListAdapter.notifyDataSetChanged(); // 刷新视图

                }
            }); // 解禁按钮
            return view;

        }
    }

}
