package com.em.yzzdemo.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.em.yzzdemo.utils.ConstantsUtils;
import com.em.yzzdemo.utils.SPUtil;

/**
 * Created by Geri on 2016/11/28.
 */

public class ContactsHelper extends SQLiteOpenHelper {
    private static String db_name = "chat.db";

    private static ContactsHelper instance;


    /**
     * 单例模式获取 获取数据库操作类实例
     *
     * @return 返回当前类的单例对象
     */
    public static ContactsHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ContactsHelper(context.getApplicationContext());
        }
        return instance;
    }

    public ContactsHelper(Context context) {
        super(context, getDBName(context), null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table contacts(id integer primary key autoincrement,userName varchar(20)," +
                "header varchar(20),nickName varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private static String getDBName(Context context) {
        String username = (String) SPUtil.get(context, ConstantsUtils.ML_SHARED_USERNAME, "");
        return username + db_name;
    }

    public void resetDBHelper() {
        instance = null;
    }
}
