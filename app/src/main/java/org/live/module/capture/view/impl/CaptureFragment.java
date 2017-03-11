package org.live.module.capture.view.impl;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import org.live.R;
import org.live.common.listener.BackHandledFragment;
import org.live.common.listener.NoDoubleClickListener;
import org.live.module.capture.presenter.CapturePresenter;
import org.live.module.capture.presenter.impl.CapturePresenterImpl;
import org.live.module.capture.view.CaptureView;

/**
 * 录屏模块
 * Created by KAM on 2017/3/10.
 */

public class CaptureFragment extends BackHandledFragment implements CaptureView {
    private View view = null;
    private static final int ALPHHA_DEFAULT_VALUE = 50; // 默认透明度
    private IconButtonOnClickListener listener = null;
    private CapturePresenter presenter = null;
    private String rtmpUrl = null; // 推流地址
    private CaptureActivity captureActivity = null;
    private boolean isCapturing = false; // 录屏状态

    /**
     * 图标按钮
     **/
    private MaterialIconView cCaptureStatusButton = null; // 录屏状态转换按钮
    private MaterialIconView cCaptureSettingsButton = null; // 录屏参数设置按钮
    private MaterialIconView cCaptureCloseButton = null; // 录屏关闭按钮

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.fragment_capture, container);
        this.listener = new IconButtonOnClickListener();
        this.presenter = new CapturePresenterImpl(getActivity(), this);
        this.captureActivity = (CaptureActivity) getActivity();
        this.rtmpUrl = this.captureActivity.getRtmpUrl(); // 获取推流地址
        initUIElements();
        return this.view;
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
        cCaptureStatusButton.setOnClickListener(listener);
        cCaptureSettingsButton.setOnClickListener(listener);
        cCaptureCloseButton.setOnClickListener(listener);
    }

    @Override
    public void onShowCapturePauseIcon() {
        isCapturing = true;
        cCaptureStatusButton.setIcon(MaterialDrawableBuilder.IconValue.PAUSE);
        captureActivity.getcCapturingStatusTextView().setText("正在录屏直播....");
    }

    @Override
    public void onShowCapturePlayIcon() {
        isCapturing = false;
        cCaptureStatusButton.setIcon(MaterialDrawableBuilder.IconValue.PLAY);
        captureActivity.getcCapturingStatusTextView().setText("录屏直播未开始...");
    }

    @Override
    public void onShowToastMsg(String msg, int lengthType) {
        Toast.makeText(getActivity(), msg, lengthType).show();
    }

    /**
     * 图标按钮点击事件监听
     */
    private class IconButtonOnClickListener extends NoDoubleClickListener {
        @Override
        protected void onNoDoubleClick(View v) {
            switch (v.getId()) {
                case R.id.btn_capture_status: // 开始录屏
                    if (isCapturing) {
                        presenter.stopScreenCaptureAndPublish();
                    } else {
                        presenter.startScreenCaptureAndPublish(rtmpUrl);
                        Log.i("MainLog", rtmpUrl);
                    }
                    break;
                case R.id.btn_capture_settings: // 设置录屏参数
                    Log.i("MainLog", "capture_settings");
                    break;
                case R.id.btn_capture_close: // 关闭录屏
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
}
