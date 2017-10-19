package com.liveteamvn.archmvp.base.eventbus;

import kotlin.Suppress;

/**
 * Created by liam on 10/16/2017.
 */

public class EventData {
    public EventData(String key, Object data) {
        this.key = key;
        this.data = data;
    }

    private String key;
    private Object data;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @SuppressWarnings("unchecked")
    public <T> T getData() {
        return (T) data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
