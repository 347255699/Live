package org.live.module.capture.presenter;

/**
 * 录屏表示器业务接口
 * Created by KAM on 2017/3/10.
 */

public interface CapturePresenter {
    /**
     * 开始录屏直播
     * @param rtmpUrl 推流地址
     */
    public void startScreenCaptureAndPublish(String rtmpUrl);

    /**
     * 停止录屏直播
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
