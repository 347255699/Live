package org.live.common.util;

import java.lang.reflect.Type;
/**
 *  响应ajax的数据载体
 *
 * Created by Mr.wang on 2016/11/24.
 */
public interface ResponseModel<T> {

    /**
     * 获取状态码
     * @return
     */
     int getStatus() ;

    /**
     *  设置状态码
     * @param status
     */
     ResponseModel setStatus(int status) ;

    /**
     *  获取响应信息
     * @return
     */
     String getMessage() ;

    /**
     * 设置响应信息
     */
     ResponseModel setMessage(String message) ;

    /**
     *  获取数据模型
     * @return
     */
     T getData() ;

    /**
     * 设置数据模型
     * @param  data
     */
     ResponseModel setData(T data) ;

    /**
     *  响应错误
     * @return
     */
     ResponseModel error() ;

    /**
     * 响应错误
     * @param message 错误信息
     * @return
     */
     ResponseModel error(String message) ;

    /**
     *  响应成功
     * @return
     */
    ResponseModel success() ;

    /**
     * 响应成功
     * @param message 成功信息
     * @return
     */
    ResponseModel success(String message) ;

}
