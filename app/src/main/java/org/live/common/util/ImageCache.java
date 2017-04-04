package org.live.common.util;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 *  图片缓存
 * Created by Mr.wang on 2017/4/4.
 */

public class ImageCache {

    /**
     * 缓存类的实现
     */
    private static LruCache<String, Bitmap> imageLruCache ;

    private static ImageCache instance ;

    private ImageCache() {
        //拿可用内存的8分之一
        long maxMemory = Runtime.getRuntime().maxMemory() ;
        int cacheMemory = maxMemory / 8 > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)maxMemory / 8  ;
        imageLruCache = new LruCache<>(cacheMemory) ;
    }

    /**
     * 初始化
     * @return
     */
    private static ImageCache initial() {
        if(ImageCache.instance == null) {
            synchronized (ImageCache.class) {
                if(ImageCache.instance == null) {
                    ImageCache.instance = new ImageCache() ;
                }
            }
        }
        return instance ;
    }

    /**
     * 添加缓存
     * @param key
     * @param bitmap
     */
    public static void putCache(String key, Bitmap bitmap) {
        if(key == null || bitmap == null) {
            return ;
        }
        if(imageLruCache == null) initial() ;
        imageLruCache.put(key, bitmap) ;
    }

    /**
     * 获取缓存
     * @param key
     * @return
     */
    public static Bitmap getCache(String key) {
        if(key == null) return null ;
        if(imageLruCache == null) initial() ;
        return imageLruCache.get(key) ;
    }


}
