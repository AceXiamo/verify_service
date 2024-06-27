package com.axm.verify.utils;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.axm.verify.entity.FieldConfig;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Field config util.
 *
 * @Author: AceXiamo
 * @ClassName: FieldConfigUtil
 * @Date: 2024 /6/24 00:51
 */
@Slf4j
@UtilityClass
public class FieldConfigUtil {

    private static final String FILE_NAME = "field_config.json";
    private static final String FILE_PATH = System.getProperty("user.dir") + "/" + FILE_NAME;

    /**
     * The Field config list.
     */
    public static Map<String, FieldConfig> fieldConfigs;

    /**
     * Init file.
     */
    public void initFile() {
        fieldConfigs = new HashMap<>();
        try {
            File file = new File(FILE_PATH);
            if (!file.exists()) {
                if (!file.createNewFile()) log.error("创建配置文件失败");
            } else {
                String fileContent = FileUtil.readString(file, Charset.defaultCharset());
                JSONArray jsonArray = JSONArray.parseArray(fileContent);
                List<FieldConfig> configs = jsonArray.toJavaList(FieldConfig.class);
                configs.forEach(config -> fieldConfigs.put(config.getKey(), config));
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Add verify item.
     *
     * @param config the config
     */
    public void addVerifyItem(FieldConfig config) {
        fieldConfigs.put(config.getKey(), config);
        FileUtil.writeString(JSON.toJSONString(fieldConfigs.values()), FILE_PATH, Charset.defaultCharset());
    }

    /**
     * Remove verify item.
     *
     * @param key the key
     */
    public void removeVerifyItem(String key) {
        fieldConfigs.remove(key);
        FileUtil.writeString(JSON.toJSONString(fieldConfigs.values()), FILE_PATH, Charset.defaultCharset());
    }

}
