package org.live.common.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.tencent.rtmp.TXLiveConstants;

import org.live.module.publish.service.PublishPreferencesService;
import org.live.module.publish.util.constant.PublishConstant;

import java.util.Map;

/**
 * 应用全局初始化操作类
 * Created by KAM on 2017/3/3.
 */

public class CommonApplication extends Application {
    private static final String TAG = "MainLog";
    @Override
    public void onCreate() {
        super.onCreate();
        /** 初始化操作 **/
        Log.i(TAG, "开始初始化");
        initPublishConfig();
        Log.i(TAG, "初始化完毕");
    }

    /**
     * 推流参数初始化
     */
    private void initPublishConfig() {
        SharedPreferences publishPreferences = this.getSharedPreferences("push_config", Context.MODE_PRIVATE);
        Map<String, ?> publishConfig = publishPreferences.getAll(); // 取出所有参数
        Bundle bundle = new Bundle();
        if (publishConfig.size() == 0) {
            // SharedPreferences.Editor editor = publishPreferences.edit();
            String[] configTypes = {
                    PublishConstant.CONFIG_TYPE_BEAUTY_VAL,
                    PublishConstant.CONFIG_TYPE_WHITENING_VAL,
                    PublishConstant.CONFIG_TYPE_VIDEO_QUALITY,
                    PublishConstant.CONFIG_TYPE_MICRO_PHONE_VAL,
                    PublishConstant.CONFIG_TYPE_VOLUME_VAL,
                    PublishConstant.CONFIG_TYPE_IS_VOLUME_OFF,
                    PublishConstant.CONFIG_TYPE_TOUCH_FOCUS
            }; // 参数名称
            Object[] configVals = {
                    0,
                    0,
                    TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION,
                    1.0f,
                    1.0f,
                    false,
                    true
            }; // 默认参数值
            for (int i = 0; i < configTypes.length; i++) {
                String name = configTypes[i];
                Object obj = configVals[i];
                if (obj instanceof Integer) {
                    bundle.putInt(name, (Integer) obj);
                }
                if (obj instanceof Float) {
                    bundle.putFloat(name, (Float) obj);
                }
                if (obj instanceof String) {
                    bundle.putString(name, (String) obj);
                }
                if (obj instanceof Boolean) {
                    bundle.putBoolean(name, (Boolean) obj);
                }
            } // 封装参数
            Intent intent = new Intent(this, PublishPreferencesService.class);
            intent.putExtras(bundle);
            startService(intent); // 开启持久化服务类
        }
    }
}
