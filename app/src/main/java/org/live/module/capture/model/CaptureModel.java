package org.live.module.capture.model;

/**
 * 录屏业务模型
 * Created by KAM on 2017/3/10.
 */

public interface CaptureModel {

    public void startScreenCaptureAndPublish(String rtmpUrl); // 开始录屏和推流

    public void stopScreenCaptureAndPublish(); // 停止录屏和推流
}
