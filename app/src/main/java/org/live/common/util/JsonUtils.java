package org.live.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Mr.wang on 2017/4/5.
 */
public class JsonUtils {

    /**
     * GSON解析
     */
    private static Gson GSON = new GsonBuilder().serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            //.excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting().create();

    private JsonUtils() {
    }

    /**
     * 获取gson的实例
     * @return
     */
    public static Gson obtainGson() {
        return GSON ;
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT) ;
    }

    public static String toJson(Object obj) {
       return GSON.toJson(obj) ;
    }

}
