package org.live.module.capture.presenter;

/**
 * 录屏表示器业务接口
 * Created by KAM on 2017/3/10.
 */

public interface CapturePresenter {
    public void startScreenCaptureAndPublish(String rtmpUrl); // 开始录屏和推流

    public void stopScreenCaptureAndPublish(); // 停止录屏和推流
}
