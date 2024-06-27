package com.axm.verify.service;

import com.axm.verify.entity.FieldConfig;
import org.springframework.stereotype.Service;

/**
 * @Author: AceXiamo
 * @ClassName: FieldVerifyService
 * @Date: 2024/6/24 01:31
 */
public interface FieldVerifyService {

    boolean verify(String field, String value, FieldConfig config);

}
