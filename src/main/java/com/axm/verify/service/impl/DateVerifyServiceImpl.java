package com.axm.verify.service.impl;

import com.axm.verify.entity.FieldConfig;
import com.axm.verify.service.FieldVerifyService;
import com.axm.verify.utils.ErrorUtil;
import com.axm.verify.utils.FieldConfigUtil;
import com.axm.verify.utils.VerifyUtil;
import org.springframework.stereotype.Service;

/**
 * The type Date verify service.
 *
 * @Author: AceXiamo
 * @ClassName: DateVerifyServiceImpl
 * @Date: 2024 /6/24 01:54
 */
@Service("dateVerifyService")
public class DateVerifyServiceImpl implements FieldVerifyService {

    @Override
    public boolean verify(String field, String value, FieldConfig config) {
        VerifyUtil.verifyByJs(value, config);
        VerifyUtil.valueIn(value, config);
        VerifyUtil.regex(value, config);
        format(value, config);
        minMax(value, config);
        return true;
    }

    /**
     * Format.
     *
     * @param value  the value
     * @param config the config
     */
    private void format(String value, FieldConfig config) {
        if (config.getFormat() == null) return;
        if (!value.matches(config.getFormat())) {
            ErrorUtil.addError(config.getKey() + " 字段格式不符合要求，要求格式：" + config.getFormat());
        }
    }

    /**
     * Min max.
     *
     * @param value  the value
     * @param config the config
     */
    private void minMax(String value, FieldConfig config) {
        boolean minVerify = config.getMinDate() != null;
        boolean maxVerify = config.getMaxDate() != null;

        if (minVerify && maxVerify) {
            if (value.compareTo(config.getMinDate()) < 0 || value.compareTo(config.getMaxDate()) > 0) {
                ErrorUtil.addError(config.getKey() + " 字段值不在范围内，最小时间：" + config.getMinDate() + "，最大时间：" + config.getMaxDate());
            }
        }
        if (minVerify) {
            if (value.compareTo(config.getMinDate()) < 0) {
                ErrorUtil.addError(config.getKey() + " 字段值小于最小时间：" + config.getMinDate());
            }
        }
        if (maxVerify) {
            if (value.compareTo(config.getMaxDate()) > 0) {
                ErrorUtil.addError(config.getKey() + " 字段值大于最大时间：" + config.getMaxDate());
            }
        }

    }

}
