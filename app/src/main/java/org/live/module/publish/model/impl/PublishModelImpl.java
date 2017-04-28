package org.live.module.publish.model.impl;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import org.json.JSONException;
import org.json.JSONObject;
import org.live.common.constants.LiveConstants;
import org.live.common.constants.LiveKeyConstants;
import org.live.common.constants.RequestMethod;
import org.live.common.util.HttpRequestUtils;
import org.live.common.util.JsonUtils;
import org.live.common.util.ResponseModel;
import org.live.common.util.SimpleResponseModel;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.publish.domain.LimitationVo;
import org.live.module.publish.listener.OnPublishModelEventListener;
import org.live.module.publish.model.PublishModel;
import org.live.common.service.PreferencesService;
import org.live.module.publish.util.constant.PublishConstant;

import java.util.HashMap;
import java.util.List;
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
        initPublishConfig(); // 初始化推流配置
    }

    /**
     * 初始化操作
     */
    private void initPublishConfig() {
        if (config.size() > 0) {
            for (String key : config.keySet()) {
                Object obj = config.get(key);
                switch (key) {
                    case PublishConstant.CONFIG_TYPE_BEAUTY_VAL:
                        setBeautyFilter((Integer) obj, null); // 设置磨皮和美白
                        break;
                    case PublishConstant.CONFIG_TYPE_MICRO_PHONE_VAL:
                        if (!(boolean) config.get(PublishConstant.CONFIG_TYPE_IS_VOLUME_OFF)) {
                            setVolumeVal((Float) obj, null); // 设置音量
                        }
                        break;
                    case PublishConstant.CONFIG_TYPE_IS_VOLUME_OFF:
                        if ((boolean) obj) {
                            livePusher.setMicVolume(0.0f); // 设置为麦克风静音
                            livePusher.setBGMVolume(0.0f); // 设置为背景静
                        }
                        break;
                    case PublishConstant.CONFIG_TYPE_TOUCH_FOCUS:
                        livePushConfig.setTouchFocus((boolean) config.get(PublishConstant.CONFIG_TYPE_TOUCH_FOCUS));
                        livePusher.setConfig(livePushConfig); // 设置对焦模式
                        break;
                    case PublishConstant.CONFIG_TYPE_VIDEO_QUALITY:
                        livePusher.setVideoQuality((int) config.get(PublishConstant.CONFIG_TYPE_VIDEO_QUALITY)); // 设置清晰度
                    default:
                        break;
                }
            }
        }
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
        /*String cameraInfo = (!isFrontCamera) ? "前置摄像头" : "后置摄像头";*/
   /*     listener.onToastMsg(cameraInfo + "开启", Toast.LENGTH_SHORT); // 通知presenter摄像头切换信息并在ui提示*/
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
     * 设置推流参数
     *
     * @param configType 参数类型
     * @param value      参数值
     */
    @Override
    public void setPushConfig(String configType, Object value) {
        switch (configType) {
            case PublishConstant.CONFIG_TYPE_VIDEO_QUALITY:
                livePusher.setVideoQuality((Integer) value); // 设置清晰度
                config.put(PublishConstant.CONFIG_TYPE_VIDEO_QUALITY, value);
                break;
            case PublishConstant.CONFIG_TYPE_TOUCH_FOCUS:
                livePushConfig.setTouchFocus((boolean) value); // 设置对焦模式
                livePusher.setConfig(livePushConfig);
                config.put(PublishConstant.CONFIG_TYPE_TOUCH_FOCUS, value);
                break;
            default:
                break;
        }
    }

    /**
     * 获取推流参数
     *
     * @return Map<String, Object> 参数键值对
     */
    @Override
    public void getPushConfig() {
        Map<String, Object> pushConfig = new HashMap<String, Object>();
        pushConfig.put(PublishConstant.CONFIG_TYPE_TOUCH_FOCUS, config.get(PublishConstant.CONFIG_TYPE_TOUCH_FOCUS));
        pushConfig.put(PublishConstant.CONFIG_TYPE_VIDEO_QUALITY, config.get(PublishConstant.CONFIG_TYPE_VIDEO_QUALITY));
        listener.onSetPublishSettingsViewVal(pushConfig);
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
        Integer beauty = (Integer) config.get(PublishConstant.CONFIG_TYPE_BEAUTY_VAL);
        Integer whitening = (Integer) config.get(PublishConstant.CONFIG_TYPE_WHITENING_VAL);
        listener.onSetBeautyRangeBarVal(beauty, whitening); // 通知presenter所获取的美颜参数并更新ui
    }

    /**
     * 持久化推流参数至SharedPreferences
     */
    @Override
    public void refreshPreferences() {
        Intent intent = new Intent(context, PreferencesService.class);
        intent.putExtras(getBundle());
        context.startService(intent); // 开启参数持久化服务类
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

    /**
     * 获取音量数值
     */
    @Override
    public void getVolumeVal() {
        Float microPhoneVal = (Float) config.get(PublishConstant.CONFIG_TYPE_MICRO_PHONE_VAL);
        Float volumeVal = (Float) config.get(PublishConstant.CONFIG_TYPE_VOLUME_VAL);
        boolean isVolumeOff = (boolean) config.get(PublishConstant.CONFIG_TYPE_IS_VOLUME_OFF);
        listener.onSetVolumeSettingsViewVal(microPhoneVal * 10, volumeVal * 10, isVolumeOff); // 通知presenter所获取的音量数值并作出ui更新
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
        if ((boolean) config.get(PublishConstant.CONFIG_TYPE_IS_VOLUME_OFF)) {
            config.put(PublishConstant.CONFIG_TYPE_IS_VOLUME_OFF, false);
            listener.onSetVolumeOffSwitchButton(false);
        } // 关闭静音按钮

    }

    /**
     * 设置静音
     *
     * @param turnVolumeOff 当前是否处在静音状态(true|false)
     */
    @Override
    public void setVolumeOff(boolean turnVolumeOff) {
        if (turnVolumeOff) {
            // 开启静音
            livePusher.setMicVolume(0.0f);
            livePusher.setBGMVolume(0.0f);
        } else {
            // 关闭静音，恢复设置静音前的音量
            livePusher.setMicVolume((Float) config.get("microPhoneVal"));
            livePusher.setBGMVolume((Float) config.get("volumeVal"));
        }
        config.put("isVolumeOff", turnVolumeOff);
    }

    @Override
    public void getBlackListData() {
        AsyncHttpGet request = new AsyncHttpGet(LiveConstants.REMOTE_SERVER_HTTP_IP+ "/app/liveroom/limits?liveRoomId=" + HomeActivity.mobileUserVo.getLiveRoomVo().getRoomId()) ;
        AsyncHttpClient.getDefaultInstance().executeString(request, new AsyncHttpClient.StringCallback(){
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse source, String result) {
                Message message = responseHandler.obtainMessage();
                if(e != null) {
                    Log.e("Global", e.getMessage()) ;
                    return ;
                }
                ResponseModel<List<LimitationVo>> dataModel = JsonUtils.fromJson(result, new TypeToken<SimpleResponseModel<List<LimitationVo>>>(){}.getType()) ;
                if(dataModel == null) {
                    dataModel = new SimpleResponseModel<>() ;
                }
                message.obj = dataModel ;
                message.what = HomeConstants.LOAD_LIMITATION_SUCCESS_FLAG ;
                responseHandler.sendMessage(message) ;
            }
        }) ;
    }

    Handler responseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HomeConstants.LOAD_LIMITATION_SUCCESS_FLAG) {
                ResponseModel<List<LimitationVo>> dataModel = (ResponseModel<List<LimitationVo>>) msg.obj;
                listener.refreshBlackList(dataModel.getData()); // 刷新黑名单
            }
        }
    }; // 处理响应

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
                listener.onToastMsg("直播开始！", Toast.LENGTH_SHORT); // 提示直播准备开始信息
                break;
            case PUSH_ERR_NET_DISCONNECT:
               //listener.onToastMsg("网络断连,且经三次抢救无效,可以放弃治疗,更多重试请自行重启推流！", Toast.LENGTH_SHORT); // 提示网络断开
                listener.onStopPusher(); // 通知ui作出相应
                break;
            case PUSH_WARNING_NET_BUSY:
                listener.onToastMsg("网络状况不佳，可能影响你的粉丝观看哦！", Toast.LENGTH_SHORT); // 提示网络不佳
                break;
            default:
                break;
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

}
