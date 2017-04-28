package org.live.module.capture.view;

import java.util.Map;

/**
 * 录屏视图响应接口
 * Created by KAM on 2017/3/10.
 */

public interface CaptureView {
    public void onShowPublishSettingsView(Map<String, Object> config); // 显示推流参数设置视图
}
