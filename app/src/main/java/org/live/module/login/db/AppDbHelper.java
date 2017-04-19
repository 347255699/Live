package org.live.module.login.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库辅助类
 * Created by KAM on 2017/4/13.
 */

public class AppDbHelper extends SQLiteOpenHelper {
    public AppDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * 数据库初始化，创建相关数据表；该方法只有在数据库第一次创建时才会调用
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        String mobile_user = "create table live_mobile_user(" +
                "user_id varchar(64)," +
                "account varchar(64)," +
                "password varchar(64)," +
                "nickname varchar(64)," +
                "head_img_url varchar(100)," +
                "email varchar(64)," +
                "mobile_number varchar(64)," +
                "real_name varchar(64)," +
                "sex char(1)," +
                "birthday varchar(64)," +
                "anchor_flag boolean" +
                ")";
        String live_room = "create table live_room(" +
                "category_id varchar(64)," +
                "category_name varchar(64)," +
                "room_id varchar(64)," +
                "room_num varchar(64)," +
                "room_name varchar(64)," +
                "room_cover_url varchar(64)," +
                "live_room_url varchar(64),"+
                "room_label varchar(64)," +
                "ban_live_flag boolean," +
                "description text" +
                ")";
        db.execSQL(mobile_user);
        db.execSQL(live_room); // 创建表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
