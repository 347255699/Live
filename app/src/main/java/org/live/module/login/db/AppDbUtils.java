package org.live.module.login.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 应用数据库工具类（sqLite）
 * Created by KAM on 2017/4/14.
 */

public class AppDbUtils {
    /**
     * sqLite数据库引用
     */
    private SQLiteDatabase writableDatabase;

    public AppDbUtils(Context context, String dbName, int version) {
        AppDbHelper appDbHelper = new AppDbHelper(context, context.getFilesDir().toString() + "/" + dbName, null, version);
        this.writableDatabase = appDbHelper.getWritableDatabase();
    }

    /**
     * 添加记录
     *
     * @param sql
     * @param params
     */
    public void save(String sql, Object[] params) {
        writableDatabase.execSQL(sql, params);
    }

    /**
     * 查询记录
     *
     * @param sql
     * @param params
     */
    public Cursor findOne(String sql, String[] params) {
        Cursor cursor = writableDatabase.rawQuery(sql, params);
        cursor.moveToFirst();
        return cursor;
    }

    /**
     * 释放资源
     */
    public void close() {
        if (writableDatabase != null) {
            writableDatabase.close();
        }
    }

    /**
     * 取得可进行读写的数据库操作对象
     *
     * @return
     */
    public SQLiteDatabase getReadableDatabase() {
        return this.writableDatabase;
    }

    /**
     * 删除单条记录
     *
     * @param sql
     * @param delBy 删除根据
     */
    public void delete(String sql, String delBy) {
        writableDatabase.execSQL(sql, new String[]{delBy});
    }
}
