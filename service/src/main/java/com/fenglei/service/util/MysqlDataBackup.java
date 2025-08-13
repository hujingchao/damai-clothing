package com.fenglei.service.util;

import com.fenglei.common.util.RequestUtils;
//import com.fenglei.model.system.entity.BackupLog;
//import com.fenglei.service.system.IBackupLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * MYSQL 数据自动备份
 * <P>
 *    请参考文章： https://blog.csdn.net/qq_41463655/article/details/112628365
 * </P>
 * @author wangsong
 * @date 2021/1/14 0014 19:12
 * @return
 * @version 1.0.0
 */
@Component
@Configuration
@EnableScheduling
@Slf4j
public class MysqlDataBackup {

//    @Resource
//    private IBackupLogService iBackupLogService;

    /**
     * 备份 sql 存放目录(相对路径, 注意可能需要在 MvcConfig 配置访问权限)
     */
    String filePath = "File/sql/";

    /**
     * 数据库版本是否为 8.0 + （false=否  true=是）， mysql8+ 需要参数  --column-statistics=0  ， mysql8- 不需要
     */
    Boolean isDbVersion8 = false;

    /**
     * 备份命令
     * USERNAME   账号
     * PASSWORD   密码
     * SERVERPATH 服务器IP/域名
     * DBNAME     数据库名称
     * FILEPATH   备份文件存放地址+名称
     * 说明
     * cmdCompression ： 需压缩 （本地或服务器需安装 mysqldump 命令(安装mysql自带患独立安装) +  gzip 命令(独立安装)）
     * cmd ：            不压缩 (本地或服务器需安装 mysqldump 命令(安装mysql自带患独立安装)
     * --column-statistics=0     mysql8 添加该参数, 非mysql8 不添加, 否则将出错
     */
    String cmdMysql8 = "mysqldump --column-statistics=0  -u{USERNAME} -p{PASSWORD} -h{SERVERPATH} -P{PORT} --databases {DBNAME}"; //  > {FILEPATH}.sql
    String cmd = "mysqldump  -u{USERNAME} -p{PASSWORD} -h{SERVERPATH} -P{PORT} --databases {DBNAME}";      //  > {FILEPATH}.sql


    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUserName;

    @Value("${spring.datasource.password}")
    private String dbPassWord;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    @Transactional(rollbackFor = Exception.class)
    public void configureTasks(Integer bizType) {
        log.info("【备份数据库】--START");
        String dbUrl2 = dbUrl.replace("jdbc:mysql://", "");

        // 获取数据库名称
        String dbName = dbUrl2.substring(dbUrl2.indexOf("/") + 1, dbUrl2.indexOf("?"));
        // 获取数据库IP地址
        String serverPath = dbUrl2.substring(0, dbUrl2.indexOf(":"));
        // 数据库账号
        String username = dbUserName;
        // 数据库密码
        String password = dbPassWord;

        // 备份文件目录+名称  备份文件存放目录+名称(名称 = 数据库名+时间字符串.sql)
        String timeStr = sdf.format(new Date())
                .replaceAll("-", "_")
                .replaceAll(" ", "_")
                .replaceAll(":", "");
        timeStr = timeStr.substring(0, 15);
        String pathFileName = filePath + dbName + "_" + timeStr + ".sql";
        String newCmd = "";
        if (isDbVersion8) {
            newCmd = cmdMysql8;
        } else {
            newCmd = cmd;
        }
        // 执行命令
        newCmd = newCmd.replace("{USERNAME}", username)
                .replace("{PASSWORD}", password)
                .replace("{SERVERPATH}", serverPath)
                .replace("{PORT}", "3307")
                .replace("{DBNAME}", dbName)
                .replace("{FILEPATH}", pathFileName);
        System.out.println(newCmd);
        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        try {
            // 创建存放sql的文件
            existsFile(new File(pathFileName));
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(pathFileName), "utf8"));
            Process process = null;
            String property = System.getProperty("os.name");
            System.out.println(property);
            if (property.indexOf("Linux") != -1) {
                // linux
                process = Runtime.getRuntime().exec(new String[]{"bash", "-c", newCmd});
            } else {
                // 本地win
                process = Runtime.getRuntime().exec(newCmd);
            }
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");
            bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                printWriter.println(line);
            }
            // 此次会执行过长时间,直到备份完成
            printWriter.flush();
            printWriter.close();
            //0 表示线程正常终止。
            if (process.waitFor() == 0) {
                // 线程正常执行
                log.info("【备份数据库】SUCCESS，SQL文件：{}", pathFileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("【备份数据库】FAILURE");
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        log.info("【备份数据库】--END");

//        BackupLog backupLog = new BackupLog();
//        backupLog.setBizType(bizType);
//        backupLog.setFileName(dbName + "_" + timeStr + ".sql");
//        backupLog.setFilePath(filePath);
//        iBackupLogService.add(backupLog);
    }


    /**
     * 判断文件是否存在，不存在创建
     */
    private static void existsFile(File file) {
        // 判断文件路径是否存在,不存在新建
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
