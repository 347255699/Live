package org.live.module.capture.presenter.impl;

import android.content.Context;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;

import org.live.module.capture.listener.OnCaptureModelEventListener;
import org.live.module.capture.model.CaptureModel;
import org.live.module.capture.model.impl.CaptureModelImpl;
import org.live.module.capture.presenter.CapturePresenter;
import org.live.module.capture.util.WindowManagerUtil;
import org.live.module.capture.util.constant.CaptureConstant;
import org.live.module.capture.view.CaptureView;

/**
 * 录屏表示器业务实现类
 * Created by KAM on 2017/3/10.
 */

public class CapturePresenterImpl implements CapturePresenter, OnCaptureModelEventListener {
    private CaptureModel captureModel;
    private Handler captureHandler;
    private CaptureView captureView = null;

    public CapturePresenterImpl(Context context, Handler handler) {
        if (WindowManagerUtil.captureFABView != null) {
            this.captureView = WindowManagerUtil.captureFABView;
        }
        this.captureHandler = handler;
        captureModel = new CaptureModelImpl(context, this);
    }

    /**
     * 开始录屏和推流
     *
     * @param rtmpUrl 推流地址
     */
    @Override
    public void startScreenCaptureAndPublish(String rtmpUrl) {
        captureModel.startScreenCaptureAndPublish(rtmpUrl);
    }

    /**
     * 停止录屏和推流
     */
    @Override
    public void stopScreenCaptureAndPublish() {
        captureModel.stopScreenCaptureAndPublish();
    }

    @Override
    public void triggerPrivateMode(boolean isPrivateMode) {
        captureModel.triggerPrivateMode(isPrivateMode);
    }

    @Override
    public void setVideoQuality(Integer videoQuality) {
        captureModel.setVideoQuality(videoQuality);
    }

    /**
     * 通知正在录屏直播
     */
    @Override
    public void onCapturingAndPushing() {
        sendHandlerMsg(CaptureConstant.CAPTURE_STATUS_SERVICE_START_NORMAL);
    }

    /**
     * 通知录屏直播已关闭
     *
     * @param isNormal true|false 是否正常关闭
     */
    @Override
    public void onStopCaptureAndPusher(boolean isNormal) {
        if (isNormal) {
            sendHandlerMsg(CaptureConstant.CAPTURE_STATUS_SERVICE_CLOSE_NORMAL);
        } else {
            sendHandlerMsg(CaptureConstant.CAPTURE_STATYS_SERVICE_CLOSE_ABNORMALITY);
        }

    }

    /**
     * 通知当前网络质量很差
     */
    @Override
    public void onNetBusy() {
        sendHandlerMsg(CaptureConstant.CAPTURE_STATUS_SERVICE_NET_BUSY);
    }

    @Override
    public void onPrivateModeStatus(boolean isPrivateMode) {
        if (isPrivateMode) {
            sendHandlerMsg(CaptureConstant.CAPTURE_SERVICE_IS_PRIVATE_MODE_TRUE);
        } else {
            sendHandlerMsg(CaptureConstant.CAPTURE_SERVICE_IS_PRIVATE_MODE_FALSE);
        }
    }

    /**
     * 发送消息至captureService
     *
     * @param msgType
     */
    private void sendHandlerMsg(int msgType) {
        Message msg = captureHandler.obtainMessage();
        msg.arg1 = msgType;
        captureHandler.sendMessage(msg); // 发送消息
    }
}
