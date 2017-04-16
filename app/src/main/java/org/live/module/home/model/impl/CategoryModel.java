package org.live.module.home.model.impl;



import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.AsyncHttpRequest;
import com.koushikdutta.async.http.AsyncHttpResponse;

import org.live.common.constants.LiveConstants;
import org.live.common.util.JsonUtils;
import org.live.module.home.constants.HomeConstants;
import org.live.module.home.domain.CategoryDataModel;

/**
 * Created by Mr.wang on 2017/4/5.
 */

public class CategoryModel {

    public static final String TAG = "HOME" ;

    protected static String url = LiveConstants.REMOTE_SERVER_HTTP_IP +"/app/category" ;

    private Handler handler ;

    public CategoryModel(Handler handler) {
        this.handler = handler ;
    }

    public void loadCategoryData() {

        AsyncHttpRequest request = new AsyncHttpRequest(Uri.parse(url), "GET") ;
        AsyncHttpClient.getDefaultInstance().executeString(request, new AsyncHttpClient.StringCallback() {
            @Override
            public void onCompleted(Exception e, AsyncHttpResponse response, String result) {
                Message message = Message.obtain() ;
                if (e != null) {
                    e.printStackTrace();
                    message.what = HomeConstants.LOAD_CATEGORY_EXCEPTION_FLAG ;
                    handler.sendMessage(message) ;
                    return;
                }
                Log.d(TAG, "result---> " + result) ;
                CategoryDataModel dataModel = JsonUtils.fromJson(result, CategoryDataModel.class) ;
                message.obj = dataModel ;
                message.what = HomeConstants.LOAD_CATEGORY_SUCCESS_FLAG ;
                handler.sendMessage(message) ;
            }
        });
    }

}
