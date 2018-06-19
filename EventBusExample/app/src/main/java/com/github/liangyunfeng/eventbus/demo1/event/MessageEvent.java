package com.github.liangyunfeng.eventbus.demo1.event;

/**
 * Created by yunfeng.l on 2018/6/14.
 */

public class MessageEvent {
    private String message;

    public MessageEvent(String msg) {
        message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
