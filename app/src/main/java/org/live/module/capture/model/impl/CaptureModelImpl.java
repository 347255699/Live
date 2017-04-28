package org.live.module.capture.model.impl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;

import org.live.R;
import org.live.common.service.PreferencesService;
import org.live.module.capture.listener.OnCaptureModelEventListener;
import org.live.module.capture.model.CaptureModel;
import org.live.module.publish.util.constant.PublishConstant;

import java.util.Map;

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
    private Map<String, Object> config;
    private Context context;

    public CaptureModelImpl(Context context, OnCaptureModelEventListener eventListener) {
        this.context = context;
        mLivePusher = new TXLivePusher(context);
        mLivePushConfig = new TXLivePushConfig();
        this.eventListener = eventListener;

        SharedPreferences preferences = context.getSharedPreferences("push_config", Context.MODE_PRIVATE);
        config = (Map<String, Object>) preferences.getAll(); // 取出所有推流配置参数
        mLivePusher.setVideoQuality((int) config.get(PublishConstant.CONFIG_TYPE_VIDEO_QUALITY)); // 设置清晰度
        //Resources res = context.getResources();
        //Bitmap bitmap = BitmapFactory.decodeResource(res, R.drawable.pause_publish); // 设置播放端等待时展示用的图片
        //mLivePushConfig.setPauseImg(bitmap);
        mLivePushConfig.setPauseImg(R.drawable.pause_publish, R.drawable.pause_publish);
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
    public void setVideoQuality(Integer videoQuality) {
        mLivePusher.setVideoQuality(videoQuality); // 设置清晰度
        config.put(PublishConstant.CONFIG_TYPE_VIDEO_QUALITY, videoQuality);
        Intent intent = new Intent(context, PreferencesService.class);
        intent.putExtras(getBundle());
        context.startService(intent); // 开启参数持久化服务类
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

    /**
     * 获取参数
     *
     * @return Bundle
     */
    private Bundle getBundle() {
        Bundle bundle = new Bundle();
        for (String key : config.keySet()) {
            Object obj = config.get(key);
            if (obj instanceof Integer) {
                bundle.putInt(key, (Integer) obj);
            }
            if (obj instanceof Float) {
                bundle.putFloat(key, (Float) obj);
            }
            if (obj instanceof String) {
                bundle.putString(key, (String) obj);
            }
            if (obj instanceof Boolean) {
                bundle.putBoolean(key, (Boolean) obj);
            }
        } // 封装参数

        return bundle;
    }
}
