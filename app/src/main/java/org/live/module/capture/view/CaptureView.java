package org.live.module.capture.view;

import org.live.module.publish.domain.LimitationVo;

import java.util.List;
import java.util.Map;

/**
 * 录屏视图响应接口
 * Created by KAM on 2017/3/10.
 */

public interface CaptureView {
    void onShowQualitySettingsView(Map<String, Object> config); // 显示推流参数设置视图

    /**
     * 刷新当前在线观看人数
     *
     * @param count
     */
    public void refreshOnlineCount(String count);

    public void refreshBlackList(List<LimitationVo> limitationVos); // 刷新黑名单
}
