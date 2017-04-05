package org.live.module.home.domain;

import java.util.List;

/**
 * Created by wangzhancheng on 2017/4/5.
 */

public class CategoryDataModel {

    private int status ;

    private String message ;

    private List<LiveCategoryVo> data ;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<LiveCategoryVo> getData() {
        return data;
    }

    public void setData(List<LiveCategoryVo> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "CategoryDataModel{" +
                "status=" + status +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
