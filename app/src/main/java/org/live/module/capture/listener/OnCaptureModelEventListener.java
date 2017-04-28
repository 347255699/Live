package org.live.module.capture.listener;

import java.util.Map;

/**
 * 录屏模型层事件监听(驱动presenter层做出相应的响应)
 * Created by KAM on 2017/3/10.
 */

public interface OnCaptureModelEventListener {
    /**
     * 通知当前正在录屏和直播
     */
    public void onCapturingAndPushing();

    /**
     * 通知当前录屏直播关闭
     *
     * @param isNormal true|false 是否正常关闭
     */
    public void onStopCaptureAndPusher(boolean isNormal);

    /**
     * 通知当前网络质量很差
     */
    public void onNetBusy();

    /**
     * 通知当前是否为隐私模式
     *
     * @param isPrivateMode
     */
    public void onPrivateModeStatus(boolean isPrivateMode);

    void onShowQualitySettingsView(Map<String, Object> config); // 显示推流参数设置视图
}
