package com.axm.verify.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
import com.axm.verify.entity.FieldConfig;
import com.axm.verify.service.FieldVerifyService;
import com.axm.verify.utils.ErrorUtil;
import com.axm.verify.utils.FieldConfigUtil;
import com.axm.verify.utils.VerifyUtil;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: AceXiamo
 * @ClassName: ObjectVerifyServiceImpl
 * @Date: 2024/6/24 01:58
 */
@Service("objectVerifyService")
public class ObjectVerifyServiceImpl implements FieldVerifyService {

    @Override
    public boolean verify(String field, String value, FieldConfig config) {
        VerifyUtil.verifyByJs(value, config);
        JSONObject object = null;
        try {
            object = JSONObject.parseObject(value);
            List<String> keys = new ArrayList<>();
            for (FieldConfig child : config.getChildren()) {
                if (child.getRequired() != null && child.getRequired()) {
                    keys.add(child.getKey());
                }
            }
            VerifyUtil.requiredHandle("", object, keys);
        } catch (Exception e) {
            ErrorUtil.addError(config.getKey() + " 字段不是json对象");
        }
        VerifyUtil.verifyObject(object, config);
        return true;
    }
}
