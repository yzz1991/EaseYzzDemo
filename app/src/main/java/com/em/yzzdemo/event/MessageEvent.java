package com.em.yzzdemo.event;

import com.hyphenate.chat.EMMessage;

/**
 * Created by Geri on 2016/12/14.
 */

public class MessageEvent {
    private EMMessage message;
    private EMMessage.Status status;

    public MessageEvent() {
    }

    public EMMessage getMessage() {
        return message;
    }

    public void setMessage(EMMessage message) {
        this.message = message;
    }

    public EMMessage.Status getStatus() {
        return status;
    }

    public void setStatus(EMMessage.Status status) {
        this.status = status;
    }
}
