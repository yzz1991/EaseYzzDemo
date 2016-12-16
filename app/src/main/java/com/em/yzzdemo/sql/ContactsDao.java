package com.em.yzzdemo.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.em.yzzdemo.bean.UserEntity;
import com.em.yzzdemo.utils.DBConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Geri on 2016/11/28.
 */

public class ContactsDao {
    private Context mContext;
    private SQLiteDatabase db;
    private static ContactsDao instance;

    private ContactsDao(Context context){
        this.mContext = context;
        db = ContactsHelper.getInstance(context).getReadableDatabase();
    }

    public static ContactsDao getInstance(Context context){
        if(instance == null){
            instance = new ContactsDao(context);
        }
        return instance;
    }

    //添加数据库
    public void saveUser(UserEntity userEntity){
        ContentValues values = new ContentValues();
        values.put(DBConstants.COL_HEADER, userEntity.getHeader());
        values.put(DBConstants.COL_USERNAME, userEntity.getUserName());
        values.put(DBConstants.COL_NICKNAME, userEntity.getNickName());
        db.insert(DBConstants.TB_USER,null,values);
    }

    //删除
    public void deleteUser(String username){
        db.delete(DBConstants.TB_USER,DBConstants.COL_USERNAME,new String[]{username});
    }

    //获取userlist
    public Map<String,UserEntity> getContactsList(){
        Cursor cursor = db.query(DBConstants.TB_USER, null, null, null, null, null, null);
        Map<String,UserEntity> userMap = new HashMap<>();
        UserEntity userEntity = null;
        while (cursor.moveToNext()){
            userEntity = new UserEntity();
            userEntity.setHeader(cursor.getString(cursor.getColumnIndex(DBConstants.COL_HEADER)));
            userEntity.setUserName(cursor.getString(cursor.getColumnIndex(DBConstants.COL_USERNAME)));
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(DBConstants.COL_NICKNAME)))){
                userEntity.setNickName(cursor.getString(cursor.getColumnIndex(DBConstants.COL_NICKNAME)));
            }
            userMap.put(userEntity.getUserName(),userEntity);
        }
        return userMap;
    }

    public void resetDatabase(){
        instance = null;
    }
}
