package com.axm.verify.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.axm.verify.entity.FieldConfig;
import com.axm.verify.service.FieldVerifyService;
import com.axm.verify.utils.ErrorUtil;
import com.axm.verify.utils.VerifyUtil;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * The type Array verify service.
 *
 * @Author: AceXiamo
 * @ClassName: ArrayVerifyServiceImpl
 * @Date: 2024 /6/24 22:21
 */
@Service("arrayVerifyService")
public class ArrayVerifyServiceImpl implements FieldVerifyService {

    @Override
    public boolean verify(String field, String value, FieldConfig config) {
        VerifyUtil.verifyByJs(value, config);
        JSONArray array = null;
        try {
            array = JSONArray.parseArray(value);
        } catch (Exception e) {
            ErrorUtil.addError(config.getKey() + " 字段不是数组");
        }
        if (array != null) {
            for (Object object : array) {
                VerifyUtil.verifyObject(JSONObject.from(object), config);
            }
        }
        return true;
    }

}
