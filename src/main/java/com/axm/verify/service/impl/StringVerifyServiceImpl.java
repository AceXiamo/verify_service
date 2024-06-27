package com.axm.verify.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.axm.verify.entity.FieldConfig;
import com.axm.verify.mapper.ValueMapper;
import com.axm.verify.service.FieldVerifyService;
import com.axm.verify.utils.ErrorUtil;
import com.axm.verify.utils.FieldConfigUtil;
import com.axm.verify.utils.VerifyUtil;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * The type String verify service.
 *
 * @Author: AceXiamo
 * @ClassName: StringVerifyServiceImpl
 * @Date: 2024 /6/24 01:39
 */
@Service("stringVerifyService")
public class StringVerifyServiceImpl implements FieldVerifyService {

    /**
     * Verify boolean.
     *
     * @param field the field
     * @param value the value
     * @return the boolean
     */
    public boolean verify(String field, String value, FieldConfig config) {
        VerifyUtil.verifyByJs(value, config);
        VerifyUtil.regex(value, config);
        VerifyUtil.valueIn(value, config);
        length(value, config);
        minMaxLength(value, config);
        beginWith(value, config);
        endWith(value, config);
        valueInFromSql(value, config);
        valueInFromList(value, config);
        return true;
    }


    /**
     * Length.
     *
     * @param value  the value
     * @param config the config
     */
    private void length(String value, FieldConfig config) {
        if (config.getLength() == null) return;
        if (config.getLength() != value.length()) {
            ErrorUtil.addError(config.getKey() + " 字段长度不符合要求，要求长度：" + config.getLength());
        }
    }

    /**
     * Min max length.
     *
     * @param value  the value
     * @param config the config
     */
    private void minMaxLength(String value, FieldConfig config) {
        boolean minVerify = config.getMinLength() != null;
        boolean maxVerify = config.getMaxLength() != null;
        if (minVerify && maxVerify) {
            if (value.length() < config.getMinLength() || value.length() > config.getMaxLength()) {
                ErrorUtil.addError(config.getKey() + " 字段长度不在范围内，最小长度：" + config.getMinLength() + "，最大长度：" + config.getMaxLength());
            }
        }
        if (minVerify) {
            if (value.length() < config.getMinLength()) {
                ErrorUtil.addError(config.getKey() + " 字段长度小于最小长度：" + config.getMinLength());
            }
        }
        if (maxVerify) {
            if (value.length() > config.getMaxLength()) {
                ErrorUtil.addError(config.getKey() + " 字段长度大于最大长度：" + config.getMaxLength());
            }
        }
    }

    /**
     * Begin with.
     *
     * @param value  the value
     * @param config the config
     */
    private void beginWith(String value, FieldConfig config) {
        if (config.getBeginWith() == null) return;
        if (!value.startsWith(config.getBeginWith())) {
            ErrorUtil.addError(config.getKey() + " 字段不以 " + config.getBeginWith() + " 开头");
        }
    }

    /**
     * End with.
     *
     * @param value  the value
     * @param config the config
     */
    private void endWith(String value, FieldConfig config) {
        if (config.getEndWith() == null) return;
        if (!value.endsWith(config.getEndWith())) {
            ErrorUtil.addError(config.getKey() + " 字段不以 " + config.getEndWith() + " 结尾");
        }
    }

    /**
     * Value in from sql.
     *
     * @param value  the value
     * @param config the config
     */
    private void valueInFromSql(String value, FieldConfig config) {
        if (StrUtil.isEmpty(config.getValueInSql())) return;
        ValueMapper mapper = SpringUtil.getBean(ValueMapper.class);
        List<String> list = mapper.valueInForString(config.getValueInSql());
        if (!list.contains(value)) {
            ErrorUtil.addError(config.getKey() + " 字段值不在指定范围内，[" + String.join(",", list) + "]");
        }
    }

    /**
     * Value in from list.
     *
     * @param value  the value
     * @param config the config
     */
    private void valueInFromList(String value, FieldConfig config) {
        if (config.getValueIn() == null) return;
        if (!Arrays.stream(config.getValueIn()).toList().contains(value)) {
            ErrorUtil.addError(config.getKey() + " 字段值不在指定范围内，[" + String.join(",", config.getValueIn()) + "]");
        }
    }

}
