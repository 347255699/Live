package org.live.module.capture.presenter.impl;

import android.content.Context;

import org.live.module.capture.listener.OnCaptureModelEventListener;
import org.live.module.capture.model.CaptureModel;
import org.live.module.capture.model.impl.CaptureModelImpl;
import org.live.module.capture.presenter.CapturePresenter;
import org.live.module.capture.view.CaptureView;

/**
 * 录屏表示器业务实现类
 * Created by KAM on 2017/3/10.
 */

public class CapturePresenterImpl implements CapturePresenter, OnCaptureModelEventListener {
    private CaptureModel captureModel = null;
    private CaptureView captureView = null;

    public CapturePresenterImpl(Context context, CaptureView captureView) {
        this.captureView = captureView;
        this.captureModel = new CaptureModelImpl(context, this);
    }

    /**
     * 开始录屏和推流
     * @param rtmpUrl
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
    public void onCapturingAndPushing() {
        captureView.onShowCapturePauseIcon();
    }

    @Override
    public void onStopCaptureAndPusher() {
        captureView.onShowCapturePlayIcon();
    }

    @Override
    public void onShowToastMsg(String msg, int lengthType) {
        captureView.onShowToastMsg(msg, lengthType);
    }
}
