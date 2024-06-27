package com.axm.verify.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Error util.
 *
 * @Author: AceXiamo
 * @ClassName: ErrorUtil
 * @Date: 2024 /6/27 17:17
 */
@UtilityClass
@Slf4j
public class ErrorUtil {

    private final ThreadLocal<List<String>> errorThreadLocal = ThreadLocal.withInitial(ArrayList::new);

    /**
     * Add error.
     *
     * @param error the error
     */
    public void addError(String error) {
        errorThreadLocal.get().add(error);
    }

    /**
     * Gets errors.
     *
     * @return the errors
     */
    public List<String> getErrors() {
        return errorThreadLocal.get();
    }

    /**
     * Clear errors.
     */
    public void clearErrors() {
        errorThreadLocal.get().clear();
    }

}
