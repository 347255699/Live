package org.live.common.util;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.async.http.NameValuePair;
import com.koushikdutta.async.http.body.UrlEncodedFormBody;

import org.json.JSONObject;
import org.live.common.constants.LiveConstants;
import org.live.common.constants.RequestMethod;

import java.util.ArrayList;
import java.util.Map;

/**
 * http请求工具类
 * Created by KAM on 2017/4/13.
 */

public class HttpRequestUtils {
    private static final String TAG = "Global";

    /**
     * 发起http请求
     *
     * @param url    请求地址
     * @param type   请求类型
     * @param params 待提交参数
     * @return
     */
    public static void requestHttp(String url, String type, Map<String, Object> params, final Handler handler, final int what) throws Exception {

        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (String key : params.keySet()) {
            String _key = key;
            String _val = (String) params.get(key);
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
        //Future<JSONObject> future =
        client.executeJSONObject(request, new AsyncHttpClient.JSONObjectCallback() {

            @Override
            public void onCompleted(Exception e, AsyncHttpResponse source, JSONObject result) {
                Message message = handler.obtainMessage();
                message.obj = result;
                message.what = what;
                handler.sendMessage(message); // 返回结果
                if(e != null){
                    String msg = (e.getMessage()==null)?"Login failed!":e.getMessage();
                    Log.e(TAG, msg);
                }
            }// 请求毁掉
        }); // 发送请求并返回预期参数
    }
}
