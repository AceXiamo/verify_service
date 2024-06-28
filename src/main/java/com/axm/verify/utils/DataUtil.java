package com.axm.verify.utils;

import com.alibaba.fastjson2.JSONObject;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Data util.
 *
 * @Author: AceXiamo
 * @ClassName: DataUtil
 * @Date: 2024 /6/28 18:03
 */
@UtilityClass
@Slf4j
public class DataUtil {

    private final ThreadLocal<JSONObject> dataThreadLocal = new ThreadLocal<>();

    /**
     * Sets init data.
     *
     * @param data the data
     */
    public void setInitData(JSONObject data) {
        dataThreadLocal.set(data);
    }

    /**
     * Get object.
     *
     * @param key the key
     * @return the object
     */
    public Object get(String key) {
        JSONObject json = dataThreadLocal.get();
        return dataThreadLocal.get().get(key);
    }

    public String replaceSalDynamicKey(String sql) {
        JSONObject json = dataThreadLocal.get();
        for (String key : json.keySet()) {
            String template = "#" + key;
            if (sql.contains(template)) {
                sql = sql.replace(template, json.getString(key));
            }
        }
        return sql;
    }

}
