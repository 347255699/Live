package org.live.common.util;


import com.google.gson.reflect.TypeToken;

import org.live.module.home.domain.LiveRoomVo;

import java.lang.reflect.Type;

/**
 *
 *
 * Created by Mr.wang on 2016/11/24.
 */
public class  SimpleResponseModel<T> implements ResponseModel<T> {

    /**
     *  响应状态. 常用： 1.成功，0 .失败。  需要其他状态码自行判断使用即可。
     */
    private int status ;

    /**
     * 响应信息
     */
    private String message ;

    /**
     * 响应数据载体，单条
     */
    private T data ;


    public SimpleResponseModel(){}

    public SimpleResponseModel(T data) {
        this.data = data ;
    }

    public SimpleResponseModel(int status, String message, T data) {

        this.setStatus(status);
        this.setMessage(message) ;
        this.setData(data) ;
    }





    @Override
    public int getStatus() {
        return status ;
    }

    @Override
    public SimpleResponseModel setStatus(int status) {
        this.status = status ;
        return this ;
    }

    @Override
    public String getMessage() {
        return this.message ;
    }

    @Override
    public SimpleResponseModel setMessage(String message) {
          this.message = message ;
          return this ;
    }

    @Override
    public T getData() {
        return this.data ;
    }

    @Override
    public SimpleResponseModel setData(T data) {
        this.data = data ;
        return this ;
    }

    @Override
    public ResponseModel error() {

        status = 0 ;
        return this ;
    }

    @Override
    public SimpleResponseModel error(String message) {

        status = 0 ;
        this.message = message ;
        return this;
    }

    @Override
    public SimpleResponseModel success() {
        this.status = 1 ;
        return this ;
    }

    @Override
    public SimpleResponseModel success(String message) {

        this.status = 1 ;
        this.message = message ;
        return this;
    }

}
