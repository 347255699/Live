package org.live.module.login.service;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.async.http.NameValuePair;
import com.koushikdutta.async.http.body.UrlEncodedFormBody;

import org.json.JSONObject;
import org.live.common.constants.RequestMethod;

import java.util.ArrayList;
import java.util.Map;

/**
 * http服务
 * Created by KAM on 2017/4/12.
 */

public class HttpService extends Service {
    private static final String TAG = "HttpService";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return new HttpServiceBinder();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    public class HttpServiceBinder extends Binder {

        /**
         * 发起请求
         *
         * @param url
         * @param type
         * @param params
         */
        public JSONObject request(String url, String type, Map<String, Object> params) {
            try {
               return requestHttp(url, type, params);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            return null;
        }

        /**
         * 发起http请求
         *
         * @param url    请求地址
         * @param type   请求类型
         * @param params 待提交参数
         * @return
         */
        private JSONObject requestHttp(String url, String type, Map<String, Object> params) throws Exception {
            ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                pairs.add(new BasicNameValuePair(key, (String) params.get(key)));
            } // 构建参数

            UrlEncodedFormBody writer = new UrlEncodedFormBody(pairs);
            AsyncHttpRequest request = null;

            switch (type) {
                case RequestMethod.GET:
                    request = new AsyncHttpRequest(Uri.parse(url), RequestMethod.GET);
                    break;
                case RequestMethod.POST:
                    request = new AsyncHttpRequest(Uri.parse(url), RequestMethod.POST);
                    break;
                case RequestMethod.DELETE:
                    request = new AsyncHttpRequest(Uri.parse(url), RequestMethod.POST);
                    pairs.add(new BasicNameValuePair("_method", RequestMethod.DELETE));
                    break;
                case RequestMethod.PUT:
                    request = new AsyncHttpRequest(Uri.parse(url), RequestMethod.POST);
                    pairs.add(new BasicNameValuePair("_method", RequestMethod.PUT));
                    break;
                case RequestMethod.PATH:
                    request = new AsyncHttpRequest(Uri.parse(url), RequestMethod.POST);
                    pairs.add(new BasicNameValuePair("_method", RequestMethod.PATH));
                    break;
            } // 调整请求类型以支持restfult风格

            if (request != null && pairs.size() > 0) {
                request.setBody(writer);
            }

            AsyncHttpClient client = AsyncHttpClient.getDefaultInstance();
            Future<JSONObject> future = client.executeJSONObject(request, new AsyncHttpClient.JSONObjectCallback() {
                @Override
                public void onCompleted(Exception e, AsyncHttpResponse response, JSONObject result) {
                    if (e != null) {
                        Log.e(TAG, e.getMessage());
                        return;
                    }

                } // 请求响应回调
            }); // 发送请求并返回预期参数

            return future.get();
        }

    }
}
