package com.em.yzzdemo.bean;

/**
 * Created by Geri on 2016/11/28.
 */

public class UserEntity {

    // 联系人对象的头，用来排序和根据字母查找
    public String header;
    // 联系人的username
    public String userName;
    // 联系人的昵称
    public String nickName;

    public UserEntity(String userName) {
        this.userName = userName;
    }

    public UserEntity() {
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return nickName == null ? userName : nickName;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o instanceof UserEntity) {
            return false;
        }
        return userName.equals(((UserEntity) o).getUserName());
    }

    @Override
    public int hashCode() {
        return 17 * userName.hashCode();
    }

}
