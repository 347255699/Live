package org.live.common.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

/**
 * 参数持久化服务类
 * Created by KAM on 2017/3/9.
 */

public class PreferencesService extends IntentService {
    private static final String TAG = "MainLog";
    private SharedPreferences publishPreferences = null;
    private Bundle bundle = null;

    public PreferencesService() {
        super("PreferencesService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "参数持久化服务启动！");
        this.bundle = intent.getExtras();
        this.publishPreferences = this.getSharedPreferences("push_config", Context.MODE_PRIVATE);
        putPreference();
    }

    /**
     * 持久化参数
     */
    private void putPreference() {
        if (bundle.size() > 0) {
            Log.i(TAG, "开始持久化数据，数据量为" + bundle.size() + "!");
            SharedPreferences.Editor editor = this.publishPreferences.edit();
            for (String key : bundle.keySet()) {
                Object objVal = bundle.get(key);
                if (objVal instanceof Integer) {
                    editor.putInt(key, (Integer) objVal);
                }
                if (objVal instanceof Float) {
                    editor.putFloat(key, (Float) objVal);
                }
                if (objVal instanceof String) {
                    editor.putString(key, (String) objVal);
                }
                if (objVal instanceof Boolean) {
                    editor.putBoolean(key, (Boolean) objVal);
                }
            }
            editor.commit();
        }
    }

}
