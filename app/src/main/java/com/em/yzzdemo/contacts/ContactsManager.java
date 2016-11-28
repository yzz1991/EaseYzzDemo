package com.em.yzzdemo.contacts;

import android.content.Context;

import com.em.yzzdemo.bean.UserEntity;
import com.em.yzzdemo.sql.ContactsDao;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Geri on 2016/11/28.
 */

public class ContactsManager {

    private Map<String,UserEntity> mUserMap = new HashMap<>();
    private static ContactsManager instance;
    private Context mContext;

    private ContactsManager(Context context){
        mContext = context;
    }

    public static ContactsManager getInstance(Context context){
        if(instance == null){
            instance = new ContactsManager(context);
        }
        return instance;
    }

    public Map<String, UserEntity> getContactList() {
        if (mUserMap.isEmpty()) {
            mUserMap = ContactsDao.getInstance(mContext).getContactsList();
        }
        return mUserMap;
    }



}
