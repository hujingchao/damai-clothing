package com.fenglei.service.util;

import com.fenglei.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 定时任务调度测试
 *
 * @author ruoyi
 */
@Component("myBackup")
public class MyBackup {

    @Autowired
    private MysqlDataBackup mysqlDataBackup;

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

    public void backupFunc(String beginTime, String endTime) throws Exception {
        Boolean tf = false;
        if (StringUtils.isEmpty(beginTime) && StringUtils.isEmpty(endTime)) {
            tf = true;
        }
        if (StringUtils.isNotEmpty(beginTime) && StringUtils.isEmpty(endTime)) {
            Date now = new Date();
            String beginTimeStr = sdf1.format(now) + " " + beginTime;
            Integer count = beginTimeStr.length() - beginTimeStr.replace(":", "").length();
            if (count < 1) {
                beginTimeStr = beginTimeStr + ":00:00";
            } else if (count == 1) {
                beginTimeStr = beginTimeStr + ":00";
            }
            Date beginDate = sdf.parse(beginTimeStr);
            if (now.compareTo(beginDate) >= 0) {
                tf = true;
            }
        }
        if (StringUtils.isEmpty(beginTime) && StringUtils.isNotEmpty(endTime)) {
            Date now = new Date();
            String endTimeStr = sdf1.format(now) + " " + endTime;
            Integer count = endTimeStr.length() - endTimeStr.replace(":", "").length();
            if (count < 1) {
                endTimeStr = endTimeStr + ":00:00";
            } else if (count == 1) {
                endTimeStr = endTimeStr + ":00";
            }
            Date endDate = sdf.parse(endTimeStr);
            if (endDate.compareTo(now) >= 0) {
                tf = true;
            }
        }
        if (StringUtils.isNotEmpty(beginTime) && StringUtils.isNotEmpty(endTime)) {
            Date now = new Date();
            String beginTimeStr = sdf1.format(now) + " " + beginTime;
            Integer count1 = beginTimeStr.length() - beginTimeStr.replace(":", "").length();
            if (count1 < 1) {
                beginTimeStr = beginTimeStr + ":00:00";
            } else if (count1 == 1) {
                beginTimeStr = beginTimeStr + ":00";
            }
            Date beginDate = sdf.parse(beginTimeStr);
            String endTimeStr = sdf1.format(now) + " " + endTime;
            Integer count2 = endTimeStr.length() - endTimeStr.replace(":", "").length();
            if (count2 < 1) {
                endTimeStr = endTimeStr + ":00:00";
            } else if (count2 == 1) {
                endTimeStr = endTimeStr + ":00";
            }
            Date endDate = sdf.parse(endTimeStr);
            if (now.compareTo(beginDate) >= 0 && endDate.compareTo(now) >= 0) {
                tf = true;
            }
        }

        if (tf) {
            mysqlDataBackup.configureTasks(1);
        }
    }
}