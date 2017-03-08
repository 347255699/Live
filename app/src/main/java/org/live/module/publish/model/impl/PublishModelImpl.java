package org.live.module.publish.model.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.live.module.publish.listener.OnPublishModelEventListener;
import org.live.module.publish.model.PublishModel;

import java.util.Map;

import static com.tencent.rtmp.TXLiveConstants.*;

/**
 * 主播模块相关业务实现类
 * Created by KAM on 2017/3/2.
 */

public class PublishModelImpl implements PublishModel, ITXLivePushListener {
    private TXLivePusher livePusher = null;
    private TXLivePushConfig livePushConfig = null;
    private Context context = null;
    private SharedPreferences preferences = null;
    private Map<String, Object> config = null; // 当前推流参数缓存map,离开主播界面时将所有参数持久化至SharedPreferences
    private OnPublishModelEventListener listener = null; // model层事件监听类，由presenter实现。将用于驱动view的ui作出相应的响应

    public PublishModelImpl(Context context, OnPublishModelEventListener listener) {
        /** 数据初始化 **/
        this.listener = listener;
        this.context = context;
        livePusher = new TXLivePusher(context);
        livePusher.setPushListener(this);
        livePushConfig = new TXLivePushConfig();
        livePusher.setConfig(livePushConfig); // 设置推流默认参数
        preferences = context.getSharedPreferences("push_config", Context.MODE_PRIVATE);
        config = (Map<String, Object>) preferences.getAll(); // 取出所有推流配置参数
    }

    /**
     * 开始预览
     *
     * @param previewVideoView
     */
    @Override
    public void startCameraPreview(TXCloudVideoView previewVideoView) {
        livePusher.startCameraPreview(previewVideoView);
    }

    /**
     * 开始推流
     *
     * @param rtmpUrl
     */
    @Override
    public void startPusher(String rtmpUrl) {
        if (!livePusher.isPushing()) {
            livePusher.startPusher(rtmpUrl);
            listener.onPushing(); // 通知presenter正在推流并更新ui
        }
    }

    /**
     * 设置美颜
     *
     * @param beauty
     * @param whitening
     */
    @Override
    public void setBeautyFilter(Integer beauty, Integer whitening) {
        if (beauty != null) {
            config.put("beautyVal", beauty); // 缓存磨皮参数
            livePusher.setBeautyFilter(beauty, (Integer) config.get("whiteningVal")); // 磨皮
        }

        if (whitening != null) {
            config.put("whiteningVal", whitening); // 缓存美白参数
            livePusher.setBeautyFilter((Integer) config.get("beautyVal"), whitening); // 美白
        }
    }

    /**
     * 设置滤镜
     *
     * @param bmp
     */
    @Override
    public void setFilter(Bitmap bmp) {
        // 未定功能
    }

    /**
     * 切换摄像头
     *
     * @param isFrontCamera
     * @return
     */
    @Override
    public boolean switchCamera(boolean isFrontCamera) {
        livePusher.switchCamera();
        boolean result = (isFrontCamera) ? false : true; // 更新摄像头切换信息
        String cameraInfo = (!isFrontCamera) ? "前置摄像头" : "后置摄像头";
        listener.onToastMsg(cameraInfo + "开启", Toast.LENGTH_SHORT); // 通知presenter摄像头切换信息并在ui提示
        return result;
    }

    /**
     * 切换闪光灯状态
     *
     * @param flashTurnOn
     * @return
     */
    @Override
    public boolean switchFlashLight(boolean flashTurnOn) {
        boolean result = flashTurnOn;
        boolean currentFlash = !flashTurnOn; //currentFlash为true表示打开，否则表示关闭
        if (!livePusher.turnOnFlashLight(currentFlash)) {
            // 当前摄像头为前置摄像头，无闪光灯可用
            listener.onToastMsg("打开闪光灯失败:绝大部分手机不支持前置闪光灯!", Toast.LENGTH_SHORT); // 通知presenter无闪光灯可用并在ui显示提示信息
        } else {
            result = currentFlash; // 更新闪光灯切换信息
            if (result) {
                listener.onCloseFlash();
            } else {
                listener.onOpenFlash();
            } // 通知presenter做出ui更新
        }
        return result;
    }

    /**
     * 批量设置推流参数
     *
     * @param configs 相关参数集合
     */
    @Override
    public void setPushConfig(Map<Integer, Object> configs) {
        // 未定参数
    }

    /**
     * 后台推流
     */
    @Override
    public void pausePusher() {
        livePusher.pausePusher(); // 通知 SDK 进入“后台推流模式”了
        // onPausemCaptureView()
    }

    /**
     * 前台推流
     */
    @Override
    public void resumePusher() {
        livePusher.resumePusher();  // 通知 SDK 重回前台推流
        // onResumeCaptureView()
    }

    @Override
    public void pushNetBusyWarning() {
        // 将废弃
    }

    /**
     * 响应屏幕旋转事件
     *
     * @param mobileRotation
     */
    @Override
    public void onDisplayRotationChanged(int mobileRotation) {
        int pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
        switch (mobileRotation) {
            case Surface.ROTATION_0:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_DOWN;
                break;
            case Surface.ROTATION_90:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_RIGHT;
                break;
            case Surface.ROTATION_270:
                pushRotation = TXLiveConstants.VIDEO_ANGLE_HOME_LEFT;
                break;
            default:
                break;
        }
        //通过设置config是设置生效（可以不用重新推流，腾讯云是少数支持直播中热切换分辨率的云商之一）
        livePusher.setRenderRotation(0);
        livePushConfig.setHomeOrientation(pushRotation);
        livePusher.setConfig(livePushConfig);
    }

    /**
     * 停止推流
     */
    @Override
    public void stopRtmpPublish() {
        livePusher.stopPusher();            //停止推流
        livePusher.setPushListener(null);   //解绑 listener
        listener.onStopPusher(); // 通知presenter已停止推流并做出ui更新
    }

    /**
     * 停止摄像头预览
     */
    @Override
    public void stopCameraPreview() {
        livePusher.stopCameraPreview(true);
    }

    /**
     * 获取当前美颜参数
     */
    @Override
    public void getBeautyAndWhiteningVal() {
        boolean hasBeautyVal = config.containsKey("beautyVal");
        boolean hasWhiteningVal = config.containsKey("whiteningVal");
        if (!hasBeautyVal) {
            config.put("beautyVal", 0);
        }
        if (!hasWhiteningVal) {
            config.put("whiteningVal", 0);
        }
        Integer beauty = (Integer) config.get("beautyVal");
        Integer whitening = (Integer) config.get("whiteningVal");
        listener.onSetBeautyRangeBarVal(beauty, whitening); // 通知presenter所获取的美颜参数并更新ui
    }

    /**
     * 持久化推流参数至SharedPreferences
     */
    @Override
    public void refreshPreferences() {
        // 未实现
    }

    /**
     * 获取音量数值
     */
    @Override
    public void getVolumeVal() {
        boolean hasMicroPhoneVal = config.containsKey("microPhoneVal");
        boolean hasVolumeVal = config.containsKey("volumeVal");
        boolean hasIsVolumeOff = config.containsKey("isVolumeOff");
        Float microPhoneVal = null;
        Float volumeVal = null;

        if (!hasMicroPhoneVal) {
            config.put("microPhoneVal", 400.0f);
        }
        if (!hasVolumeVal) {
            config.put("volumeVal", 200.0f);
        }
        if (!hasIsVolumeOff) {
            config.put("isVolumeOff", false);
        }
        microPhoneVal = (Float) config.get("microPhoneVal");
        volumeVal = (Float) config.get("volumeVal");
        Log.i("PublishFragment", "初始值：" + microPhoneVal + ":" + volumeVal + ":" + config.get("isVolumeOff"));
        listener.onSetVolumeSettingsViewVal(microPhoneVal, volumeVal); // 通知presenter所获取的音量数值并作出ui更新
    }

    /**
     * 设置音量值
     *
     * @param microPhone 麦克风音量
     * @param volume     背景音量
     */
    @Override
    public void setVolumeVal(Float microPhone, Float volume) {
        if (microPhone != null) {
            config.put("microPhoneVal", microPhone);
            livePusher.setMicVolume(microPhone);
        }
        if (volume != null) {
            config.put("volumeVal", volume);
            livePusher.setBGMVolume(volume);
        }
    }

    /**
     * 设置静音
     *
     * @param turnVolumeOff 当前是否处在静音状态(true|false)
     */
    @Override
    public boolean setVolumeOff(boolean turnVolumeOff) {
        boolean currentVolumeOff = !turnVolumeOff; // 更改状态
        if (currentVolumeOff) {
            // 开启静音
            livePusher.setMicVolume(0.0f);
            livePusher.setBGMVolume(0.0f);
            listener.onRefreshVolumeSettingsViewVal(View.GONE); // 通知presenter所获取的音量数值并作出ui更新
            Log.i("PublishFragment", "开启静" + (Float) config.get("microPhoneVal") + ":" + (Float) config.get("volumeVal"));
        } else {
            // 关闭静音，恢复设置静音前的音量
            livePusher.setMicVolume((Float) config.get("microPhoneVal"));
            livePusher.setMicVolume((Float) config.get("volumeVal"));
            Log.i("PublishFragment", "关闭静音：" + (Float) config.get("microPhoneVal") + ":" + (Float) config.get("volumeVal"));
            listener.onRefreshVolumeSettingsViewVal(View.VISIBLE); // 通知presenter所获取的音量数值并作出ui更新
        }
        config.put("isVolumeOff", currentVolumeOff);
        return currentVolumeOff; // 返回当前状态
    }

    /**
     * 绑定推流过程中相关事件的监听
     *
     * @param i      事件标识
     * @param bundle
     */
    @Override
    public void onPushEvent(int i, Bundle bundle) {
        switch (i) {
            case PUSH_EVT_PUSH_BEGIN:
                listener.onToastMsg("准备开始直播你的美艳动人吧！baby!", Toast.LENGTH_LONG); // 提示直播准备开始信息
                break;
            case PUSH_ERR_NET_DISCONNECT:
                listener.onToastMsg("网络断连,且经三次抢救无效,可以放弃治疗,更多重试请自行重启推流", Toast.LENGTH_LONG); // 提示网络断开
                listener.onStopPusher(); // 通知ui作出相应
                break;
            case PUSH_WARNING_NET_BUSY:
                listener.onToastMsg("网络状况不佳，可能影响你的粉丝观看哦！", Toast.LENGTH_LONG); // 提示网络不佳
                break;
            default:
                break;
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

}
