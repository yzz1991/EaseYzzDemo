package com.em.yzzdemo.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Geri on 2016/11/28.
 */

public class ContactsHelper extends SQLiteOpenHelper {
    public ContactsHelper(Context context) {
        super(context, "contacts.db", null, 0);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table contacts(id integer primary key autoincrement,userName varchar(20)," +
                "header varchar(20),nickName varchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
