package com.yx.crawler.common;

import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * Author:Sophie
 * Created: 2019/6/6
 */
@ToString
public class DataSet {

    private Map<String ,Object > data=new HashMap<>();

    public void putData(String key,Object value){
        this.data.put(key, value);
    }
    public Object getData(String key){
        return this.data.get(key);
    }

    public Map<String, Object> getData() {
        return new HashMap<>(this.data);
    }
}
