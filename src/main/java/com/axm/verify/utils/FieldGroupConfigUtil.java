package com.axm.verify.utils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.axm.verify.entity.FieldConfig;

import cn.hutool.core.io.FileUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Field Group Config util.
 *
 * @Author: AceXiamo
 * @ClassName: FieldGroupConfigUtil
 * @Date: 2024 /6/27 15:38
 */
@UtilityClass
@Slf4j
public class FieldGroupConfigUtil {

    private static final String FILE_NAME = "field_group_config.json";
    private static final String FILE_PATH = System.getProperty("user.dir") + "/" + FILE_NAME;

    public static Map<String, String[]> maps;

    /**
     * Init file.
     */
    public void initFile() {
        maps = new HashMap<>();
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                if (!file.createNewFile())
                    log.error("创建配置文件失败");
            } else {
                String fileContent = FileUtil.readString(file, Charset.defaultCharset());
                JSONObject json = JSON.parseObject(fileContent);
                for (String s : json.keySet()) {
                    String value = json.getString(s);
                    if (!value.isEmpty())
                        maps.put(s, value.split(","));
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Add group item.
     *
     * @param config the config
     */
    public void addVerifyItem(String key, String[] value) {
        maps.put(key, value);
        Map<String, String> format = new HashMap<>();
        maps.forEach((k, v) -> format.put(k, String.join(",", v)));
        FileUtil.writeString(JSON.toJSONString(format), FILE_PATH, Charset.defaultCharset());
    }

    /**
     * Remove group item.
     *
     * @param key the key
     */
    public void removeVerifyItem(String key) {
        maps.remove(key);
        FileUtil.writeString(JSON.toJSONString(maps), FILE_PATH, Charset.defaultCharset());
    }

}
