package com.fenglei.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

public class FastAutoGeneratorTest {

    /**
     * 执行初始化数据库脚本
     */
    public static void before() throws SQLException {
        Connection conn = DATA_SOURCE_CONFIG.build().getConn();
        InputStream inputStream = H2CodeGeneratorTest.class.getResourceAsStream("/sql/init.sql");
        ScriptRunner scriptRunner = new ScriptRunner(conn);
        scriptRunner.setAutoCommit(true);
        scriptRunner.runScript(new InputStreamReader(inputStream));
        conn.close();
    }

    /**
     * 数据源配置
     */
    private static final DataSourceConfig.Builder DATA_SOURCE_CONFIG = new DataSourceConfig
            .Builder("jdbc:mysql://192.168.0.200:3307/damai_clothing?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai&autoReconnect=true",
            "root",
            "&6QtQJN*MCCt@56")
            .typeConvert(new MySqlTypeConvert() {
                @Override
                public DbColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
                    //tinyint转换成Boolean
                    if (fieldType.toLowerCase().contains("tinyint")) {
                        return DbColumnType.BOOLEAN;
                    }
                    //将数据库中datetime转换成string
                    if (fieldType.toLowerCase().contains("datetime")) {
                        return DbColumnType.STRING;
                    }
                    //将数据库中的bigint转换成string
                    if (fieldType.toLowerCase().contains("bigint")) {
                        return DbColumnType.STRING;
                    }
                    return (DbColumnType) super.processTypeConvert(globalConfig, fieldType);
                }
            });

    /**
     * 执行 run
     */
    public static void main(String[] args) throws SQLException {
        File f = new File(H2CodeGeneratorTest.class.getResource("/").getPath());
//        before();
        FastAutoGenerator.create(DATA_SOURCE_CONFIG)
                // 全局配置
                .globalConfig((scanner, builder) -> builder
                        .author(scanner.apply("作者："))
                        .outputDir(f.getPath())
                        .enableSwagger()
                        .fileOverride())
                // 包配置
                .packageConfig((scanner, builder) -> {
                            String module = scanner.apply("模块名：");
                            builder
                                    .parent("com.fenglei")
                                    .entity("model." + module + ".entity")
                                    .service("service." + module)
                                    .serviceImpl("service." + module + ".impl")
                                    .mapper("mapper." + module)
                                    .xml("mapper." + module)
                                    .controller("controller." + module);
                        }
                )
                // 策略配置
                .strategyConfig((scanner, builder) -> {
                            String tableName = scanner.apply("表名：");
                            String[] split = tableName.split(",");
                            String isItem = scanner.apply("是否子表（0否1是）：");
                            builder
                                    .addTablePrefix("sys_")
                                    .addInclude(split)
                                    .controllerBuilder().enableRestStyle()
                                    .entityBuilder()
                                    .disableSerialVersionUID()
                                    .enableLombok()
                                    .idType(IdType.AUTO)
                                    .superClass("1".equals(isItem) ? "com.fenglei.model.base.pojo.UBillItemBaseEntity" : "com.fenglei.model.base.pojo.UBillBaseEntity")
                                    .addIgnoreColumns("id", "no", "bill_status", "create_time", "creator", "creator_id", "update_time", "updater", "updater_id", "audit_time", "auditor", "auditor_id", "pid", "seq");
                        }
                )
                /*
                    模板引擎配置，默认 Velocity 可选模板引擎 Beetl 或 Freemarker
                   .templateEngine(new BeetlTemplateEngine())
                   .templateEngine(new FreemarkerTemplateEngine())
                 */
                .templateConfig(builder -> builder
                        .controller("/templates/controller.java")
                        .entity("/templates/entity.java")
                        .mapper("/templates/mapper.java")
                        .mapperXml("/templates/mapper.xml")
                        .service("/templates/service.java")
                        .serviceImpl("/templates/serviceImpl.java")
                )

                .execute();
    }
}

