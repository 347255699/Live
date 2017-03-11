package org.live.module.capture.model.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

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
    private OnCaptureModelEventListener listener = null;

    public CaptureModelImpl(Context context, OnCaptureModelEventListener listener) {
        this.mLivePusher = new TXLivePusher(context);
        this.mLivePushConfig = new TXLivePushConfig();
        this.listener = listener;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pause_publish); // 设置等待图片
        this.mLivePushConfig.setPauseImg(bitmap);
        this.mLivePusher.setConfig(mLivePushConfig);
        this.mLivePusher.setPushListener(this); // 设置监听
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
        listener.onCapturingAndPushing(); // 通知表示器正在录屏直播
    }

    /**
     * 停止录屏和推流
     */
    @Override
    public void stopScreenCaptureAndPublish() {
        mLivePusher.stopScreenCapture();
        mLivePusher.setPushListener(null);
        mLivePusher.stopPusher();
        listener.onStopCaptureAndPusher(); // 通知表示器已停止录屏和直播
    }

    @Override
    public void onPushEvent(int i, Bundle bundle) {
        switch (i) {
            case PUSH_EVT_PUSH_BEGIN:
                listener.onShowToastMsg("开始录屏直播", Toast.LENGTH_LONG); // 提示直播准备开始信息
                break;
            case PUSH_ERR_NET_DISCONNECT:
                listener.onShowToastMsg("网络断连,且经三次抢救无效,可以放弃治疗,更多重试请自行重启推流", Toast.LENGTH_LONG); // 提示网络断开
                listener.onStopCaptureAndPusher(); // 通知ui作出相应
                break;
            case PUSH_WARNING_NET_BUSY:
                listener.onShowToastMsg("网络状况不佳，可能影响你的粉丝观看哦！", Toast.LENGTH_LONG); // 提示网络不佳
                break;
            default:
                break;
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }
}
