package org.live.module.capture.model;

/**
 * 录屏业务模型
 * Created by KAM on 2017/3/10.
 */

public interface CaptureModel {

    /**
     * 开始录屏和直播
     *
     * @param rtmpUrl 推流地址
     */
    public void startScreenCaptureAndPublish(String rtmpUrl);

    /**
     * 停止录屏和直播
     */
    public void stopScreenCaptureAndPublish();

    /**
     * 设置隐私模式
     */
    public void triggerPrivateMode(boolean isPrivateMode);

    /**
     * 设置清晰度
     *
     * @param videoQuality
     */
    public void setVideoQuality(Integer videoQuality);
}
