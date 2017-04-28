package org.live.module.capture.view.impl;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.flyco.animation.BaseAnimatorSet;
import com.flyco.animation.FadeExit.FadeExit;
import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.MaterialDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.suke.widget.SwitchButton;
import com.tencent.rtmp.TXLiveConstants;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import org.live.R;
import org.live.common.constants.LiveKeyConstants;
import org.live.common.listener.BackHandledFragment;
import org.live.common.listener.NoDoubleClickListener;
import org.live.common.util.NetworkUtils;
import org.live.module.capture.listener.OnCaptureServiceStatusListener;
import org.live.module.capture.presenter.CapturePresenter;
import org.live.module.capture.presenter.impl.CapturePresenterImpl;
import org.live.module.capture.service.CaptureService;
import org.live.module.capture.util.WindowManagerUtil;
import org.live.module.capture.view.CaptureView;
import org.live.module.play.view.impl.PlayActivity;
import org.live.module.play.view.impl.PlayFragment;
import org.live.module.publish.util.constant.PublishConstant;

import java.util.Map;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_capture, container);
        onClickListener = new IconButtonOnClickListener();
        captureActivity = (CaptureActivity) getActivity();
        capturePresenter = new CapturePresenterImpl(getActivity(), null, this);
        rtmpUrl = captureActivity.getRtmpUrl(); // 获取推流地址
        initUIElements();
        return view;
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
    }

    /**
     * 显示提示消息
     *
     * @param msg        消息内容
     * @param lengthType 消息显示时长
     */
    private void showToastMsg(String msg, int lengthType) {
        Toast.makeText(getActivity().getApplicationContext(), msg, lengthType).show();
    }

    @Override
    public void onShowQualitySettingsView(Map<String, Object> config) {
        DialogPlus dialog = DialogPlus.newDialog(getActivity())
                .setBackgroundColorResId(R.color.colorWall)
                .setContentHolder(new ViewHolder(R.layout.dialog_publish_settings))
                .setExpanded(true, 350).setGravity(Gravity.BOTTOM)
                .create();
        dialog.show();
        initQualitySettingsView(dialog.getHolderView(), config);
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
                            getActivity().unbindService(conn); // 解绑录屏服务
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
                        getActivity().unbindService(conn); // 解绑录屏服务
                    }
                    getActivity().finish();
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
        Spinner pDefinitionSpinner = (Spinner) qualitySettingsView.findViewById(R.id.spin_pulish_definition);

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
            final CaptureService captureService = ((CaptureService.CaptureBinder) service).getCaptureService(); //返回一个CaptureService对象
            //注册录屏服务状态通知回调接口
            captureService.setServiceStatusListener(new OnCaptureServiceStatusListener() {
                @Override
                public void onServiceStop(boolean isNormal) {
                    WindowManagerUtil.removeCaptureFABView(getActivity().getApplicationContext()); // 清除浮窗
                    showCapturePlayView();
                    if (isNormal) {
                        showToastMsg("录屏直播已关闭", Toast.LENGTH_SHORT);
                    } else {
                        showToastMsg("网络无法连接，录屏直播已关闭", Toast.LENGTH_SHORT);
                    }
                }

                @Override
                public void onServiceStart() {
                    Log.i(TAG, "onServiceStart");
                    WindowManagerUtil.createCaptureFABView(getActivity().getApplicationContext(), new NoDoubleClickListener() {
                        @Override
                        protected void onNoDoubleClick(View v) {
                            switch (v.getId()) {
                                case R.id.fab_capture_home:
                                    startActivity(new Intent(getActivity().getApplicationContext(), CaptureActivity.class)); // 前往主页
                                    break;
                                case R.id.fab_capture_lock:
                                    if (!isPrivateMode) {
                                        captureService.triggerPrivateMode(true);
                                    } else {
                                        captureService.triggerPrivateMode(false);
                                    }
                                    break;
                                case R.id.fab_capture_close:
                                    BaseAnimatorSet bas_in = new FlipVerticalSwingEnter();  //动画，弹出提示框的动画
                                    BaseAnimatorSet bas_out = new FadeExit();                //关闭提示框的动画
                                    final MaterialDialog dialog = new MaterialDialog(getActivity());
                                    dialog.content("确认关闭录屏直播?");
                                    dialog.btnNum(2).btnText("取消", "确认").btnTextColor(NormalDialog.STYLE_TWO);
                                    dialog.showAnim(bas_in)
                                            .dismissAnim(bas_out)
                                            .show();
                                    dialog.setOnBtnClickL(new OnBtnClickL() {
                                        @Override
                                        public void onBtnClick() {  // 确认
                                            dialog.dismiss();
                                        }
                                    }, new OnBtnClickL() {
                                        @Override
                                        public void onBtnClick() {  // 取消
                                            getActivity().unbindService(conn); // 解绑录屏服务
                                            dialog.dismiss();
                                        }
                                    });
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
}
