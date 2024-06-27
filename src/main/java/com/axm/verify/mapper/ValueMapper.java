package com.axm.verify.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: AceXiamo
 * @ClassName: ValueMapper
 * @Date: 2024/6/25 05:09
 */

@Mapper
public interface ValueMapper extends BaseMapper {

    @SelectProvider(type = SqlProvider.class, method = "dynamicSQL")
    BigDecimal valueForNumber(String sql);

    @SelectProvider(type = SqlProvider.class, method = "dynamicSQL")
    List<BigDecimal> valueInForNumber(String sql);

    @SelectProvider(type = SqlProvider.class, method = "dynamicSQL")
    String valueForString(String sql);

    @SelectProvider(type = SqlProvider.class, method = "dynamicSQL")
    List<String> valueInForString(String sql);

    class SqlProvider {
        public String dynamicSQL(String sql) {
            return sql;
        }
    }
}
