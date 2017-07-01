package com.limin.blog.dto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by devlimin on 2017/6/27.
         */
public class ValueObject {
    Map<String, Object> objs = new HashMap<>();
    public Object get(String key) {
        return objs.get(key);
    }
    public ValueObject set(String key, Object obj) {
        objs.put(key, obj);
        return this;
    }
}
