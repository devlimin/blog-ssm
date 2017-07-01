package com.limin.blog;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by devlimin on 2017/6/28.
 */
public class Main {
    public static void main(String[] args) throws  Exception {
        List<String> warnings = new ArrayList<String>();
        boolean overwrite = true;
        System.out.println(new File(".").getAbsolutePath());

        File configFile = new File("generator"+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"generatorConfig.xml");
        System.out.println(configFile.getAbsolutePath());

        ConfigurationParser cp = new ConfigurationParser(warnings);
        org.mybatis.generator.config.Configuration config = cp.parseConfiguration(configFile);
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config,
                callback, warnings);
        myBatisGenerator.generate(null);
    }
}
