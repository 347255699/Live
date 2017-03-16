package org.live.module.capture.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 摄像头预览服务
 * Created by KAM on 2017/3/16.
 */

public class CameraPreviewService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
