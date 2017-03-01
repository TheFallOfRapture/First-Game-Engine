package com.fate.engine.events;

import com.fate.engine.entities.Component;

/**
 * Created by Fernando on 1/12/2017.
 */
public class Message {
    private String msg;

    public Message(String msg) {
        this.msg = msg;
    }

    public String getContents() {
        return msg;
    }
}
