package org.live.module.home.model.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.live.common.constants.LiveConstants;
import org.live.common.constants.RequestMethod;
import org.live.common.util.HttpRequestUtils;
import org.live.common.util.JsonUtils;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.listener.LiveModelListener;
import org.live.module.home.model.LiveModel;
import org.live.module.home.view.impl.HomeActivity;
import org.live.module.login.domain.MobileUserVo;

/**
 * 直播模块逻辑处理实现
 * Created by KAM on 2017/4/22.
 */

public class LiveModelImpl implements LiveModel {
    private Context context;
    private LiveModelListener modelListener;

    public LiveModelImpl(Context context, LiveModelListener modelListener) {
        this.context = context;
        this.modelListener = modelListener;
    }

    /**
     * 检查是否是主播
     */
    @Override
    public void checkIsAnchor(String userId) {
        try {
            HttpRequestUtils.requestHttp(LiveConstants.REMOTE_SERVER_HTTP_IP + "/app/anchor?userId=" + userId, RequestMethod.GET, null, responseHandler, HomeConstants.HTTP_RESPONSE_RESULT_CHECK_IS_ANCHOR_CODE);
        } catch (Exception e) {
            Log.e("Global", e.getMessage());
        }
    }


    /**
     * 响应结果处理
     */
    private Handler responseHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == HomeConstants.HTTP_RESPONSE_RESULT_CHECK_IS_ANCHOR_CODE) {
                JSONObject jsonObject = (JSONObject) msg.obj;
                try {
                    if (jsonObject != null) {
                        if (jsonObject.getInt("status") == 1) {
                            MobileUserVo mobileUserVo = JsonUtils.fromJson(jsonObject.getJSONObject("data").toString(), MobileUserVo.class);
                            modelListener.closeRefreshing(mobileUserVo.isAnchorFlag()); // 关闭刷新视图并刷新当前视图
                            HomeActivity.mobileUserVo = mobileUserVo; // 更新数据

                        } else {
                            modelListener.showToast(jsonObject.getString("message"));
                        }
                    } else {
                        modelListener.showToast("系统繁忙");
                    }
                } catch (JSONException e) {
                    Log.e("Global", e.getMessage());
                }
            }
        }
    };

}
