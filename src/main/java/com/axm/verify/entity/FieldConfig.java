package com.axm.verify.entity;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: AceXiamo
 * @ClassName: FieldConfig
 * @Date: 2024/6/24 00:54
 */
@Data
@Builder
public class FieldConfig {

    // common
    private String key;
    private String type;
    private String regex;
    private String desc;
    private Boolean required;
    private String[] valueIn;
    private String valueInSql;
    private Boolean staticIsValueIn;
    private String javascript;

    // for number
    private BigDecimal minValue;
    private String minValueSql;
    private Boolean staticIsMin;
    private BigDecimal maxValue;
    private String maxValueSql;
    private Boolean staticIsMax;

    // for string
    private Integer length;
    private Integer maxLength;
    private Integer minLength;
    private String beginWith;
    private String endWith;

    // for date
    private String format;
    private String minDate;
    private String maxDate;

    // for array
    private String arrayType;

    // for object
    private FieldConfig[] children;
}
