package com.axm.verify.utils;

import java.util.List;
import java.util.Set;

import cn.hutool.core.util.StrUtil;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import com.alibaba.fastjson2.JSONObject;
import com.axm.verify.entity.FieldConfig;
import com.axm.verify.service.FieldVerifyService;

import cn.hutool.extra.spring.SpringUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Verify util.
 *
 * @Author: AceXiamo
 * @ClassName: VerifyUtil
 * @Date: 2024 /6/24 01:15
 */
@UtilityClass
@Slf4j
public class VerifyUtil {

    private final String JS_VALUE_CODE = "#value_str";

    /**
     * Regex.
     *
     * @param value  the value
     * @param config the config
     */
    public void regex(String value, FieldConfig config) {
        if (config.getRegex() == null) return;
        if (!value.matches(config.getRegex())) {
            ErrorUtil.addError(config.getKey() + " 字段不符合正则表达式：" + config.getRegex());
        }
    }

    /**
     * Value in.
     *
     * @param value  the value
     * @param config the config
     */
    public void valueIn(String value, FieldConfig config) {
        if (config.getValueIn() == null) return;
        boolean contains = false;
        for (String s : config.getValueIn()) {
            if (s.equals(value)) {
                contains = true;
                break;
            }
        }
        if (!contains) {
            ErrorUtil.addError(config.getKey() + " 字段值不在指定范围内");
        }
    }

    /**
     * verify by js
     *
     * @param value  the value
     * @param config the config
     */
    public void verifyByJs(String value, FieldConfig config) {
        if (config.getJavascript() == null) return;
        String script = config.getJavascript().replace(JS_VALUE_CODE, value);
        String resultStr = null;
        try {
            Context cx = Context.enter();
            Scriptable scope = cx.initStandardObjects();
            Object result = cx.evaluateString(scope, script + "\n result", "<cmd>", 1, null);
            resultStr = Context.toString(result);
        } catch (Exception e) {
            log.error("执行js脚本失败", e);
        } finally {
            Context.exit();
        }
        if (!"1".equals(resultStr)) {
            ErrorUtil.addError(resultStr);
        }
    }

    /**
     * Verify object.
     *
     * @param jsonObject the json object
     * @param config     the config
     */
    public void verifyObject(JSONObject jsonObject, FieldConfig config) {
        for (String key : jsonObject.keySet()) {
            FieldConfig fieldConfig = null;
            try {
                for (FieldConfig child : config.getChildren()) {
                    if (child.getKey().equals(key)) fieldConfig = child;
                }
                if (fieldConfig == null) continue;
                FieldVerifyService fieldVerifyService = SpringUtil.getBean(fieldConfig.getType() + "VerifyService");
//                fieldVerifyService.verify(key, jsonObject.getString(key), fieldConfig);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }

    /**
     * Field verify string [ ].
     *
     * @param verifyFields the verify fields
     * @param object       the object
     * @return the string [ ]
     */
    public String[] fieldVerify(String verifyFields, JSONObject object) {
        String[] list = verifyFields.split(",");
        Set<String> keys = object.keySet();
        for (String key : list) {
            if (!keys.contains(key)) {
                ErrorUtil.addError("缺少字段：" + key);
            }
        }
        return list;
    }

    /**
     * Required handle.
     *
     * @param link   the link
     * @param object the object
     * @param keys   the keys
     */
    public void requiredHandle(String link, JSONObject object, List<String> keys) {
        for (String key : keys) {
            if (FieldConfigUtil.fieldConfigs.get(key).getRequired() != null && FieldConfigUtil.fieldConfigs.get(key).getRequired()) {
                if (object.get(key) == null) {
                    if (StrUtil.isNotEmpty(link)) {
                        ErrorUtil.addError(link + "." + key + " 为必填项");
                    } else {
                        ErrorUtil.addError(key + " 为必填项");
                    }
                }
            }
        }
    }

}
