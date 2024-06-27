package com.axm.verify.utils;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.axm.verify.entity.FieldConfig;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Js template util.
 *
 * @Author: AceXiamo
 * @ClassName: JSTemplateUtil
 * @Date: 2024 /6/27 16:30
 */
@UtilityClass
@Slf4j
public class JSTemplateUtil {

    private static final String FILE_NAME = "js_template.json";
    private static final String FILE_PATH = System.getProperty("user.dir") + "/" + FILE_NAME;

    /**
     * The Field config list.
     */
    public static Map<String, String> maps;

    /**
     * Init file.
     */
    public void initFile() {
        maps = new HashMap<>();
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                if (!file.createNewFile()) log.error("创建配置文件失败");
            } else {
                String fileContent = FileUtil.readString(file, Charset.defaultCharset());
                JSONObject json = JSON.parseObject(fileContent);
                for (String s : json.keySet()) {
                    String value = json.getString(s);
                    if (!value.isEmpty())
                        maps.put(s, value);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Add item.
     *
     * @param key   the key
     * @param value the value
     */
    public void addItem(String key, String value) {
        maps.put(key, value);
        FileUtil.writeString(JSON.toJSONString(maps), FILE_PATH, Charset.defaultCharset());
    }

    /**
     * Remove verify item.
     *
     * @param key the key
     */
    public void removeItem(String key) {
        maps.remove(key);
        FileUtil.writeString(JSON.toJSONString(maps), FILE_PATH, Charset.defaultCharset());
    }

}
