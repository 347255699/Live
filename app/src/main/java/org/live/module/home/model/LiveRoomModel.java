package org.live.module.home.model;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpGet;
import com.koushikdutta.async.http.AsyncHttpPost;
import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.AsyncHttpResponse;
import com.koushikdutta.async.http.BasicNameValuePair;
import com.koushikdutta.async.http.NameValuePair;
import com.koushikdutta.async.http.body.UrlEncodedFormBody;

import org.live.common.constants.LiveConstants;
import org.live.common.util.JsonUtils;
import org.live.common.util.ResponseModel;
import org.live.common.util.SimpleResponseModel;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.domain.LiveRoomVo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wang on 2017/4/21.
 */

public class LiveRoomModel {

    public static final String TAG = "HOME" ;

    protected static String url = LiveConstants.REMOTE_SERVER_HTTP_IP +"/app/liveroom" ;

    private Context context ;

    private Handler handler ;

    public LiveRoomModel(Context context, Handler handler) {
        this.context = context ;
        this.handler = handler ;
    }

    /**
     * 加载直播间的数据
     */
    public void loadLiveRoomData() {
        AsyncHttpRequest request = new AsyncHttpRequest(Uri.parse(url), "GET") ;
        AsyncHttpClient.getDefaultInstance().executeString(request, new AsyncHttpClient.StringCallback(){
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse source, String result) {
                Message message = Message.obtain() ;
                if(e != null) {
                    Log.e(TAG, e.getMessage()) ;
                    message.what = HomeConstants.LOAD_LIVE_ROOM_EXCEPTION_FLAG ;
                    handler.sendMessage(message) ;
                    return ;
                }
                Log.d(TAG, "result-->" + result) ;
                ResponseModel<List<LiveRoomVo>> dataModel = JsonUtils.fromJson(result, new TypeToken<SimpleResponseModel<List<LiveRoomVo>>>(){}.getType()) ;
                message.obj = dataModel ;
                message.what = HomeConstants.LOAD_LIVE_ROOM_SUCCESS_FLAG ;
                handler.sendMessage(message) ;
            }
        }) ;
    }

    /**
     * 根据直播分类id加载数据
     * @param categoryId
     */
    public void loadLiveRoomDataByCategoryId(String categoryId) {

        AsyncHttpGet request = new AsyncHttpGet(url+"?categoryId="+categoryId) ;
        AsyncHttpClient.getDefaultInstance().executeString(request, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse source, String result) {
                Message message = Message.obtain() ;
                if(e != null) {
                    Log.e(TAG, e.getMessage()) ;
                    message.what = HomeConstants.LOAD_LIVE_ROOM_EXCEPTION_FLAG ;
                    handler.sendMessage(message) ;
                    return ;
                }
                Log.d(TAG, "result-->" + result) ;
                ResponseModel<List<LiveRoomVo>> dataModel = JsonUtils.fromJson(result, new TypeToken<SimpleResponseModel<List<LiveRoomVo>>>(){}.getType()) ;
                message.obj = dataModel ;
                message.what = HomeConstants.LOAD_LIVE_ROOM_SUCCESS_FLAG ;
                handler.sendMessage(message) ;
            }
        }) ;


    }


}
