package com.em.yzzdemo.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.em.yzzdemo.bean.UserEntity;
import com.em.yzzdemo.utils.SqlDBConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Geri on 2016/11/28.
 */

public class ContactsSqlDao {
    private Context mContext;
    private SQLiteDatabase db;
    private List<UserEntity> userList;

    public ContactsSqlDao(Context context) {
        this.mContext = context;
        db = new ContactsHelper(mContext).getReadableDatabase();
        userList = new ArrayList<>();
    }

    //添加数据库
    public void saveUser(UserEntity userEntity){
        ContentValues values = new ContentValues();
        values.put(SqlDBConstants.COL_HEADER, userEntity.getHeader());
        values.put(SqlDBConstants.COL_USERNAME, userEntity.getUserName());
        values.put(SqlDBConstants.COL_NICKNAME, userEntity.getNickName());
        db.insert(SqlDBConstants.TB_USER,null,values);
    }

    //删除
    public void deleteUser(String username){
        db.delete(SqlDBConstants.TB_USER,SqlDBConstants.COL_USERNAME,new String[]{username});
    }

    //获取userlist
    public List<UserEntity> getUserList(){
        Cursor cursor = db.query(SqlDBConstants.TB_USER, null, null, null, null, null, null);
        userList.clear();
        UserEntity userEntity = null;
        while (cursor.moveToNext()){
            userEntity = new UserEntity();
            userEntity.setHeader(cursor.getString(cursor.getColumnIndex(SqlDBConstants.COL_HEADER)));
            userEntity.setUserName(cursor.getString(cursor.getColumnIndex(SqlDBConstants.COL_USERNAME)));
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex(SqlDBConstants.COL_NICKNAME)))){
                userEntity.setNickName(cursor.getString(cursor.getColumnIndex(SqlDBConstants.COL_NICKNAME)));
            }
        }
        return userList;
    }
}
