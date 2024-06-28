package com.axm.verify.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.axm.verify.entity.FieldConfig;
import com.axm.verify.mapper.ValueMapper;
import com.axm.verify.service.FieldVerifyService;
import com.axm.verify.utils.DataUtil;
import com.axm.verify.utils.ErrorUtil;
import com.axm.verify.utils.VerifyUtil;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The type Number verify service.
 *
 * @Author: AceXiamo
 * @ClassName: FieldVerifyServiceImpl
 * @Date: 2024 /6/24 01:32
 */
@Service("numberVerifyService")
public class NumberVerifyServiceImpl implements FieldVerifyService {

    @Override
    public boolean verify(String field, String value, FieldConfig config) {
        VerifyUtil.verifyByJs(value, config);
        VerifyUtil.regex(value, config);
        if (StrUtil.isEmpty(value)) return true;
        BigDecimal parse = new BigDecimal(value);
        valueInFromSql(parse, config);
        valueInFromList(parse, config);

        BigDecimal min = config.getMinValue();
        BigDecimal max = config.getMaxValue();
        if (config.getStaticIsMin() != null && !config.getStaticIsMin()) {
            BigDecimal fromSql = valFormSql(config.getMinValueSql());
            if (min == null || fromSql.compareTo(min) < 0) {
                min = fromSql;
            }
        }
        if (config.getStaticIsMax() != null && !config.getStaticIsMax()) {
            BigDecimal fromSql = valFormSql(config.getMaxValueSql());
            if (max == null || fromSql.compareTo(max) > 0) {
                max = fromSql;
            }
        }
        minMax(parse, min, max, config);
        return true;
    }

    /**
     * Min max.
     *
     * @param value the value
     * @param min   the min
     * @param max   the max
     */
    private void minMax(BigDecimal value, BigDecimal min, BigDecimal max, FieldConfig config) {
        if (min != null && value.compareTo(min) < 0) {
            ErrorUtil.addError(config.getKey() + " 值小于最小值：" + min);
        }
        if (max != null && value.compareTo(max) > 0) {
            ErrorUtil.addError(config.getKey() + " 值大于最大值：" + max);
        }
    }

    /**
     * Value in from sql.
     *
     * @param value  the value
     * @param config the config
     */
    private void valueInFromSql(BigDecimal value, FieldConfig config) {
        if (config.getValueInSql() == null) return;
        ValueMapper mapper = SpringUtil.getBean(ValueMapper.class);
        String sql = DataUtil.replaceSalDynamicKey(config.getValueInSql());
        List<BigDecimal> list = mapper.valueInForNumber(sql);
        if (!list.contains(value)) {
            ErrorUtil.addError(config.getKey() + " 字段值不在指定范围内，[" + list + "]");
        }
    }

    /**
     * Value in from list.
     *
     * @param value  the value
     * @param config the config
     */
    private void valueInFromList(BigDecimal value, FieldConfig config) {
        if (config.getValueIn() == null) return;
        String[] list = config.getValueIn();
        if (list.length > 0) {
            List<BigDecimal> parse = new ArrayList<>();
            for (String s : list) {
                parse.add(new BigDecimal(s));
            }
            if (!parse.contains(value)) {
                ErrorUtil.addError(config.getKey() + " 字段值不在指定范围内，[" + Arrays.toString(config.getValueIn()) + "]");
            }

        }
    }

    private BigDecimal valFormSql(String sql) {
        ValueMapper mapper = SpringUtil.getBean(ValueMapper.class);
        sql = DataUtil.replaceSalDynamicKey(sql);
        return mapper.valueForNumber(sql);
    }

    /**
     * Min max from sql.
     *
     * @param value  the value
     * @param config the config
     */
    public void minMaxFromSql(BigDecimal value, FieldConfig config) {
        ValueMapper mapper = SpringUtil.getBean(ValueMapper.class);

        boolean minVerify = config.getMinValueSql() != null;
        boolean maxVerify = config.getMaxValueSql() != null;

        if (minVerify && maxVerify) {
            BigDecimal minValue = mapper.valueForNumber(config.getMinValueSql());
            BigDecimal maxValue = mapper.valueForNumber(config.getMaxValueSql());
            if (value.compareTo(minValue) < 0 || value.compareTo(maxValue) > 0) {
                ErrorUtil.addError(config.getKey() + " 字段值不在范围内，最小值：" + minValue + "，最大值：" + maxValue);
            }
        }
        if (minVerify) {
            BigDecimal minValue = mapper.valueForNumber(config.getMinValueSql());
            if (value.compareTo(minValue) < 0) {
                ErrorUtil.addError(config.getKey() + " 字段值小于最小值：" + minValue);
            }
        }
        if (maxVerify) {
            BigDecimal maxValue = mapper.valueForNumber(config.getMaxValueSql());
            if (value.compareTo(maxValue) > 0) {
                ErrorUtil.addError(config.getKey() + " 字段值大于最大值：" + maxValue);
            }
        }
    }

}
