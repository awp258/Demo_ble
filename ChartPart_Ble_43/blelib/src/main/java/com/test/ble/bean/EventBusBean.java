package com.test.ble.bean;

/**
 * Copyright (C), 2013-2019, 深圳市浩瀚卓越科技有限公司
 * Author: Abraham.ai@hohem-tech.com
 * Date: 2019/9/25 9:55
 * Description:
 * History:
 */
public class EventBusBean {

    public final String message;

    public static EventBusBean getInstance(String message) {
        return new EventBusBean(message);
    }
    private EventBusBean(String message) {
        this.message = message;
    }
}
