package com.axm.verify.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @Author: AceXiamo
 * @ClassName: ResultVo
 * @Date: 2024/6/24 02:23
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultVo {

    private Integer code;
    private String message;
    private Object data;


    public static ResultVo success() {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(200);
        resultVo.setMessage("success");
        return resultVo;
    }

    public static ResultVo success(Object data) {
        ResultVo resultVo = success();
        resultVo.setData(data);
        return resultVo;
    }

    public static ResultVo error(String message) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(500);
        resultVo.setMessage(message);
        return resultVo;
    }

    public static ResultVo error(Object data) {
        ResultVo resultVo = new ResultVo();
        resultVo.setCode(500);
        resultVo.setData(data);
        return resultVo;
    }

}
