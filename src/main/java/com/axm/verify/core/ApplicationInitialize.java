package com.axm.verify.core;

import com.axm.verify.utils.FieldConfigUtil;
import com.axm.verify.utils.FieldGroupConfigUtil;
import com.axm.verify.utils.JSTemplateUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Author: AceXiamo
 * @ClassName: ApplicationInitialize
 * @Date: 2024/6/24 00:48
 */
@Component
public class ApplicationInitialize implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        FieldConfigUtil.initFile();
        FieldGroupConfigUtil.initFile();
        JSTemplateUtil.initFile();
    }

}