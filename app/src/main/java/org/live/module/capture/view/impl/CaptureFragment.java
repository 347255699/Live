package org.live.module.capture.view.impl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import org.live.R;
import org.live.common.constants.LiveKeyConstants;
import org.live.common.listener.BackHandledFragment;
import org.live.common.listener.NoDoubleClickListener;
import org.live.module.capture.listener.OnCaptureServiceStatusListener;
import org.live.module.capture.service.CaptureService;
import org.live.module.capture.util.WindowManagerUtil;

/**
 * 录屏模块
 * Created by KAM on 2017/3/10.
 */

public class CaptureFragment extends BackHandledFragment {
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_capture, container);
        onClickListener = new IconButtonOnClickListener();
        captureActivity = (CaptureActivity) getActivity();
        rtmpUrl = captureActivity.getRtmpUrl(); // 获取推流地址
        initUIElements();
        return view;
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
        cCaptureStatusButton.setIcon(MaterialDrawableBuilder.IconValue.PAUSE);
        captureActivity.getcCapturingStatusTextView().setText("正在录屏直播....");
    }

    /**
     * 更改控件状态为未录播状态
     */
    private void showCapturePlayView() {
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

    /**
     * 图标按钮点击事件监听
     */
    private class IconButtonOnClickListener extends NoDoubleClickListener {
        @Override
        protected void onNoDoubleClick(View v) {

            switch (v.getId()) {
                case R.id.btn_capture_status:
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
                    break;
                case R.id.btn_capture_settings: // 设置录屏参数
                    Log.i("MainLog", "capture_settings");
                    break;
                case R.id.btn_capture_close: // 关闭当前界面
                    getActivity().unbindService(conn); // 解绑录屏服务
                    getActivity().finish();
                    break;
                default:
                    break;
            }
        }
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
                                    Log.i(TAG, "close");
                                    break;
                                case R.id.fab_capture_camera_front:
                                    Log.i(TAG, "camera_front");
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
