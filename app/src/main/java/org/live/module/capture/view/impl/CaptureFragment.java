package org.live.module.capture.view.impl;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.tencent.rtmp.TXLiveConstants;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import org.live.R;
import org.live.common.constants.LiveConstants;
import org.live.common.constants.LiveKeyConstants;
import org.live.common.domain.MessageType;
import org.live.common.listener.BackHandledFragment;
import org.live.common.listener.NoDoubleClickListener;
import org.live.common.util.JsonUtils;
import org.live.common.util.NetworkUtils;
import org.live.common.util.ResponseModel;
import org.live.module.capture.listener.OnCaptureActivityListener;
import org.live.module.capture.listener.OnCaptureServiceStatusListener;
import org.live.module.capture.presenter.CapturePresenter;
import org.live.module.capture.presenter.impl.CapturePresenterImpl;
import org.live.module.capture.service.CaptureService;
import org.live.module.capture.util.WindowManagerUtil;
import org.live.module.capture.view.CaptureView;
import org.live.module.chat.service.AnchorChatService;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.domain.AppAnchorInfo;
import org.live.module.home.presenter.LiveRoomPresenter;
import org.live.module.home.view.custom.AnchorInfoDialogView;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.login.domain.MobileUserVo;
import org.live.module.publish.domain.LimitationVo;
import org.live.module.publish.listener.OnPublishActivityListener;
import org.live.module.publish.util.constant.PublishConstant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * 录屏模块
 * Created by KAM on 2017/3/10.
 */

public class CaptureFragment extends BackHandledFragment implements CaptureView {
    private static final String TAG = "Global";
    private View view;
    private static final int ALPHHA_DEFAULT_VALUE = 50; // 默认透明度
    private IconButtonOnClickListener onClickListener;
    private String rtmpUrl; // 推流地址
    private CaptureActivity captureActivity;
    private static boolean isPrivateMode = false; // 是否是隐私模式
    /**
     * 图标按钮
     **/
    private MaterialIconView cCaptureStatusButton = null; // 录屏状态转换按钮
    private MaterialIconView cCaptureSettingsButton = null; // 录屏参数设置按钮
    private MaterialIconView cCaptureCloseButton = null; // 录屏关闭按钮
    private boolean isCapturing = false;
    private CapturePresenter capturePresenter;
    private String pDefinitionInfos[] = {"标清", "高清", "超清"};

    private MobileUserVo mobileUserVo; // 用户信息
    private ImageView pHeadImgImageView; // 用户头像视图
    private TextView pLiveRoomName; // 直播间名称
    private TextView pOnlineCount; // 在线人数
    private RelativeLayout pLiveRoomInfoRelativeLayout; // 直播间信息视图
    private Button pBlockListButton;// 黑名单按钮
    private Handler handler;
    private List<Map<String, Object>> data;
    private LiveRoomPresenter liveRoomPresenter;
    private AnchorInfoDialogView anchorInfoDialogView;     //主播信息弹出框的包裹view
    private CaptureFragment.BlackListAdapter blackListAdapter; // 黑名单适配器
    private int liftABanUserIndex; // 待解禁用户下标
    private OnCaptureActivityListener captureActivityListener;
    private CaptureService.CaptureBinder captureBinder; // 录屏服务引用

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_capture, container, false);
        onClickListener = new IconButtonOnClickListener();
        captureActivity = (CaptureActivity) getActivity();
        if (getActivity() instanceof OnCaptureActivityListener) {
            this.captureActivityListener = (OnCaptureActivityListener) getActivity();
        }
        capturePresenter = new CapturePresenterImpl(getActivity(), null, this);
        rtmpUrl = captureActivity.getRtmpUrl(); // 获取推流地址
        this.mobileUserVo = HomeActivity.mobileUserVo; // 用户信息
        initUIElements();
        newHanderInstance();
        liveRoomPresenter = new LiveRoomPresenter(getActivity(), handler);
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        registerReceiver(); // 注册广播接收器
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

    @Override
    public void onStart() {
        if (CaptureService.isCapturing != isCapturing) {
            if (CaptureService.isCapturing) {
                showCapturePauseView();
            } else {
                showCapturePlayView();
            }
        }
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(chatReceiver); // 注销广播接收器
    }

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
     * 初始化控件
     */
    private void initUIElements() {
        /** 各按钮意义如本类开始处成员变量含义 **/
        cCaptureStatusButton = (MaterialIconView) view.findViewById(R.id.btn_capture_status);
        cCaptureSettingsButton = (MaterialIconView) view.findViewById(R.id.btn_capture_settings);
        cCaptureCloseButton = (MaterialIconView) view.findViewById(R.id.btn_capture_close);

        cCaptureStatusButton.getBackground().setAlpha(ALPHHA_DEFAULT_VALUE);
        cCaptureSettingsButton.getBackground().setAlpha(ALPHHA_DEFAULT_VALUE);
        cCaptureCloseButton.getBackground().setAlpha(ALPHHA_DEFAULT_VALUE);

        /** 监听按钮点击事件 **/
        cCaptureStatusButton.setOnClickListener(onClickListener);
        cCaptureSettingsButton.setOnClickListener(onClickListener);
        cCaptureCloseButton.setOnClickListener(onClickListener);

        pHeadImgImageView = (ImageView) view.findViewById(R.id.iv_capture_head_img);
        pLiveRoomName = (TextView) view.findViewById(R.id.tv_capture_live_room_name);
        pOnlineCount = (TextView) view.findViewById(R.id.tv_capture_online_count);
        pLiveRoomInfoRelativeLayout = (RelativeLayout) view.findViewById(R.id.rl_capture_live_room_info);
        pLiveRoomInfoRelativeLayout.getBackground().setAlpha(ALPHHA_DEFAULT_VALUE); // 设置透明度*/

        Glide.with(getActivity()).load(LiveConstants.REMOTE_SERVER_HTTP_IP + mobileUserVo.getHeadImgUrl())
                .bitmapTransform(new CropCircleTransformation(getActivity()))
                .into(pHeadImgImageView); // 设置头像
        pLiveRoomName.setText(mobileUserVo.getLiveRoomVo().getRoomName());

        pBlockListButton = (Button) view.findViewById(R.id.btn_capture_black_list);
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
     * 更改控件状态为录播状态
     */
    private void showCapturePauseView() {
        isCapturing = true;
        cCaptureStatusButton.setIcon(MaterialDrawableBuilder.IconValue.PAUSE);
        captureActivity.getcCapturingStatusTextView().setText("正在录屏直播....");
    }

    /**
     * 更改控件状态为未录播状态
     */
    private void showCapturePlayView() {
        isCapturing = false;
        cCaptureStatusButton.setIcon(MaterialDrawableBuilder.IconValue.PLAY);
        captureActivity.getcCapturingStatusTextView().setText("录屏直播未开始...");
        captureActivityListener.logoutService(); // 解绑聊天服务
        captureActivityListener.backLiving(); // 退出录屏直播
    }

    /**
     * 显示提示消息
     *
     * @param msg        消息内容
     * @param lengthType 消息显示时长
     */
    private void showToastMsg(String msg, int lengthType) {
        Toast.makeText(getActivity(), msg, lengthType).show();
    }

    @Override
    public void onShowQualitySettingsView(Map<String, Object> config) {
        DialogPlus dialog = DialogPlus.newDialog(getActivity())
                .setBackgroundColorResId(R.color.colorWall)
                .setContentHolder(new ViewHolder(R.layout.dialog_quality_settings))
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .setGravity(Gravity.BOTTOM)
                .create();
        dialog.show();
        initQualitySettingsView(dialog.getHolderView(), config);
    }

    /**
     * 获得数据
     */
    public void getData() {
        List<Map<String, Object>> data = new ArrayList<>();
        capturePresenter.getBlackListData(); // 获取数据
        this.data = data;
    }

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

    /**
     * 图标按钮点击事件监听
     */
    private class IconButtonOnClickListener extends NoDoubleClickListener {
        @Override
        protected void onNoDoubleClick(View v) {

            switch (v.getId()) {
                case R.id.btn_capture_status:
                    if (NetworkUtils.isConnected(getActivity())) {
                        cCaptureStatusButton.setEnabled(false);
                        Intent intent = new Intent(getActivity(), CaptureService.class);
                        if (!CaptureService.isCapturing) {
                            if (null != rtmpUrl) {
                                intent.putExtra(LiveKeyConstants.Global_URL_KEY, rtmpUrl);
                            }
                            getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE); // 绑定录屏服务
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
                                    if (captureBinder != null) {
                                        getActivity().unbindService(conn); // 解绑录屏服务
                                        captureBinder = null; // 释放资源
                                    }
                                }
                            });
                        }
                        cCaptureStatusButton.setEnabled(true);
                    } else {
                        showToastMsg("网络无法连接...", Toast.LENGTH_SHORT);
                    }
                    break;
                case R.id.btn_capture_settings: // 设置录屏参数
                    capturePresenter.showVideoQualitySettingView();
                    break;
                case R.id.btn_capture_close: // 关闭当前界面
                    if (CaptureService.isCapturing) {
                        showToastMsg("正在录屏直播，不能退出当前界面！", Toast.LENGTH_SHORT); // 提示未退出录屏直播
                    } else {
                        captureActivityListener.logoutService(); // 注销服务
                        getActivity().finish();
                    }
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 初始化推流参数设置界面
     *
     * @param qualitySettingsView
     * @param config
     */
    public void initQualitySettingsView(View qualitySettingsView, Map<String, Object> config) {
        Spinner pDefinitionSpinner = (Spinner) qualitySettingsView.findViewById(R.id.spin_quality_definition);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, pDefinitionInfos); // 建立Adapter并且绑定数据源
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pDefinitionSpinner.setAdapter(adapter); //绑定 Adapter到控件


        pDefinitionSpinner.setSelection(mapperSpinnerSelectionPosition((Integer) config.get(PublishConstant.CONFIG_TYPE_VIDEO_QUALITY))); // 设置当前选项
        pDefinitionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                capturePresenter.setVideoQuality(mapperVideoQuality(pos)); // 设置视频清晰度
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback
            }
        }); // 监听下拉框

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

    /**
     * 监听返回键
     *
     * @return
     */
    @Override
    public boolean onBackPressed() {
        return false;
    }

    /**
     * 服务连接实体
     */
    private ServiceConnection conn = new ServiceConnection() {
        /**
         * 服务断开连接时回调
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        /**
         * 服务建立连接时回调
         * @param name
         * @param service 当前服务实体
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            captureBinder = (CaptureService.CaptureBinder) service;
            final CaptureService captureService = ((CaptureService.CaptureBinder) service).getCaptureService(); //返回一个CaptureService对象
            //注册录屏服务状态通知回调接口
            captureService.setServiceStatusListener(new OnCaptureServiceStatusListener() {
                @Override
                public void onServiceStop(boolean isNormal) {
                    if (WindowManagerUtil.captureFABView != null) {
                        WindowManagerUtil.removeCaptureFABView(getActivity()); // 清除浮窗
                    }
                    showCapturePlayView();
                /*    if (isNormal) {
                        showToastMsg("录屏直播已关闭", Toast.LENGTH_SHORT);
                    } else {
                        showToastMsg("网络无法连接，录屏直播已关闭", Toast.LENGTH_SHORT);
                    }*/
                }

                @Override
                public void onServiceStart() {
                    Log.i(TAG, "onServiceStart");
                    WindowManagerUtil.createCaptureFABView(getActivity(), new NoDoubleClickListener() {
                        @Override
                        protected void onNoDoubleClick(View v) {
                            switch (v.getId()) {
                                case R.id.fab_capture_home:
                                    // startActivity(new Intent(getActivity().getApplicationContext(), CaptureActivity.class)); // 前往主页
                                    break;
                                case R.id.fab_capture_lock:
                                    if (!isPrivateMode) {
                                        captureService.triggerPrivateMode(true);
                                    } else {
                                        captureService.triggerPrivateMode(false);
                                    }
                                    break;
                                case R.id.fab_capture_close:
                                    WindowManagerUtil.removeCaptureFABView(getActivity()); // 清除浮窗
                                    break;
                                default:
                                    break;
                            }
                        }
                    }); // 创建浮窗
                    showCapturePauseView(); // 置换当前视图为录屏状态
                    showToastMsg("开始录屏直播", Toast.LENGTH_SHORT);
                }

                @Override
                public void onServiceNetBusy() {

                    showToastMsg("网络质量差", Toast.LENGTH_SHORT); // 提示网络质量差
                }

                @Override
                public void onPrivateModeStatus(boolean isPrivateMode) {
                    CaptureFragment.isPrivateMode = isPrivateMode;
                    if (isPrivateMode) {
                        WindowManagerUtil.captureFABView.cPrivateLockFAB.setIcon(MaterialDrawableBuilder.IconValue.LOCK);
                    } else {
                        WindowManagerUtil.captureFABView.cPrivateLockFAB.setIcon(MaterialDrawableBuilder.IconValue.LOCK_OPEN);
                    }
                }
            });
        }
    };

    /**
     * 黑名单适配器
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

                    captureActivityListener.getChatReceiveServiceBinder().sendMsg(message); // 发送消息
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
                    if (captureBinder != null) {
                        getActivity().unbindService(conn); // 解绑录屏服务
                        captureBinder = null; // 释放资源
                    }

                    break; // 主播离开直播间
                default:
                    break;
            }
        }
    };
}
