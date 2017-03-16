package org.live.module.capture.model.impl;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;

import org.live.R;
import org.live.module.capture.listener.OnCaptureModelEventListener;
import org.live.module.capture.model.CaptureModel;

import static com.tencent.rtmp.TXLiveConstants.PUSH_ERR_NET_DISCONNECT;
import static com.tencent.rtmp.TXLiveConstants.PUSH_EVT_PUSH_BEGIN;
import static com.tencent.rtmp.TXLiveConstants.PUSH_WARNING_NET_BUSY;

/**
 * 录屏业务模型实现类
 * Created by KAM on 2017/3/10.
 */

public class CaptureModelImpl implements CaptureModel, ITXLivePushListener {
    private TXLivePusher mLivePusher = null;
    private TXLivePushConfig mLivePushConfig = null;
    private OnCaptureModelEventListener eventListener = null;

    public CaptureModelImpl(Context context, OnCaptureModelEventListener eventListener) {
        mLivePusher = new TXLivePusher(context);
        mLivePushConfig = new TXLivePushConfig();
        this.eventListener = eventListener;
        Resources res= context.getResources();

        Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.pause_publish); // 设置播放端等待时展示用的图片
        mLivePushConfig.setPauseImg(bitmap);
        mLivePusher.setConfig(mLivePushConfig);
        mLivePusher.setPushListener(this); // 设置监听
    }

    /**
     * 开始录屏和推流
     *
     * @param rtmpUrl
     */
    @Override
    public void startScreenCaptureAndPublish(String rtmpUrl) {
        mLivePusher.startPusher(rtmpUrl);
        mLivePusher.startScreenCapture();
    }

    /**
     * 停止录屏和推流
     */
    @Override
    public void stopScreenCaptureAndPublish() {
        mLivePusher.stopScreenCapture();
        mLivePusher.setPushListener(null);
        mLivePusher.stopPusher();
        eventListener.onStopCaptureAndPusher(true); // 通知表示器已经正常关闭录屏直播
    }

    /**
     * 设置隐私模式
     *
     * @param isPrivateMode true|false 是否为隐私模式
     * @return
     */
    @Override
    public void triggerPrivateMode(boolean isPrivateMode) {
        if (isPrivateMode) {
            mLivePusher.pausePusher(); // 开启隐私模式
        } else {
            mLivePusher.resumePusher(); // 关闭隐私模式
        }
        eventListener.onPrivateModeStatus(isPrivateMode); // 通知表示器当前是否是隐私模式
    }

    @Override
    public void onPushEvent(int i, Bundle bundle) {
        switch (i) {
            case PUSH_EVT_PUSH_BEGIN:
                eventListener.onCapturingAndPushing(); // 通知表示器正在录屏直播
                break;
            case PUSH_ERR_NET_DISCONNECT:
                eventListener.onStopCaptureAndPusher(false); // 通知表示器意外关闭录屏直播，网络断开
                break;
            case PUSH_WARNING_NET_BUSY:
                eventListener.onNetBusy(); // 通知表示器当前网络差
                break;
            default:
                break;
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }
}
