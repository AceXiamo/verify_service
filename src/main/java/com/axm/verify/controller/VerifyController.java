package com.axm.verify.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
import com.axm.verify.core.ResultVo;
import com.axm.verify.entity.FieldConfig;
import com.axm.verify.entity.KeyWithValue;
import com.axm.verify.service.FieldVerifyService;
import com.axm.verify.utils.*;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @Author: AceXiamo
 * @ClassName: VerifyController
 * @Date: 2024/6/24 00:04
 */
@RestController
@RequestMapping("verify")
public class VerifyController {

    @PostMapping
    public ResultVo root(@RequestHeader(value = "Verify-Fields", required = false) String verifyFields,
                         @RequestHeader(value = "Verify-Group", required = false) String verifyGroup,
                         @RequestBody JSONObject object) {
        List<String> keys = null;
        if (StrUtil.isNotEmpty(verifyGroup)) {
            keys = Arrays.asList(FieldGroupConfigUtil.maps.get(verifyGroup));
            VerifyUtil.requiredHandle("", object, keys);
        } else if (StrUtil.isNotEmpty(verifyFields)) {
            keys = Arrays.asList(verifyFields.split(","));
            VerifyUtil.requiredHandle("", object, keys);
        } else {
            keys = Arrays.asList(object.keySet().toArray(new String[0]));
        }
        // init data
        DataUtil.setInitData(object);

        for (String key : keys) {
            FieldConfig config = FieldConfigUtil.fieldConfigs.get(key);
            if (config == null) continue;
            FieldVerifyService fieldVerifyService = SpringUtil.getBean(config.getType() + "VerifyService");
            fieldVerifyService.verify(key, object.getString(key), config);
        }

        // error message handle
        List<String> messages = ErrorUtil.getErrors();
        if (!messages.isEmpty()) {
            return ResultVo.error(messages);
        }
        return ResultVo.success();
    }

    @GetMapping("/manage/list")
    public ResultVo list() {
        return ResultVo.success(Arrays.asList(FieldConfigUtil.fieldConfigs.values().toArray()));
    }

    @PostMapping("/manage/save")
    public ResultVo save(@RequestBody FieldConfig fieldConfig) {
        FieldConfigUtil.addVerifyItem(fieldConfig);
        return ResultVo.success();
    }

    @DeleteMapping("/manage/delete")
    public ResultVo delete(@RequestParam("key") String key) {
        FieldConfigUtil.removeVerifyItem(key);
        return ResultVo.success();
    }

    @GetMapping("/group/list")
    public ResultVo groupList() {
        Set<String> set = FieldGroupConfigUtil.maps.keySet();
        List<KeyWithValue> list = FieldGroupConfigUtil.maps.entrySet().stream().map(entry -> {
            KeyWithValue keyWithValue = new KeyWithValue();
            keyWithValue.setKey(entry.getKey());
            keyWithValue.setValue(String.join(",", entry.getValue()));
            return keyWithValue;
        }).toList();
        return ResultVo.success(list);
    }

    @PostMapping("/group/save")
    public ResultVo groupSave(@RequestParam("key") String key, @RequestParam("value") String value) {
        FieldGroupConfigUtil.addVerifyItem(key, value.split(","));
        return ResultVo.success();
    }

    @DeleteMapping("/group/delete")
    public ResultVo groupDelete(@RequestParam("key") String key) {
        FieldGroupConfigUtil.removeVerifyItem(key);
        return ResultVo.success();
    }

    @GetMapping("/js/list")
    public ResultVo jsList() {
        Set<String> set = JSTemplateUtil.maps.keySet();
        List<KeyWithValue> list = JSTemplateUtil.maps.entrySet().stream().map(entry -> {
            KeyWithValue keyWithValue = new KeyWithValue();
            keyWithValue.setKey(entry.getKey());
            keyWithValue.setValue(entry.getValue());
            return keyWithValue;
        }).toList();
        return ResultVo.success(list);
    }

    @PostMapping("/js/save")
    public ResultVo jsSave(@RequestBody KeyWithValue keyWithValue) {
        JSTemplateUtil.addItem(keyWithValue.getKey(), keyWithValue.getValue());
        return ResultVo.success();
    }

    @DeleteMapping("/js/delete")
    public ResultVo jsDelete(@RequestParam("key") String key) {
        JSTemplateUtil.removeItem(key);
        return ResultVo.success();
    }

}
