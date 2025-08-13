package com.fenglei.common.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.fenglei.common.util.RequestUtils;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 字段自动填充
 *
 * @link https://mp.baomidou.com/guide/auto-fill-metainfo.html
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 新增填充创建时间
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            this.strictInsertFill(metaObject, "gmtCreate", LocalDateTime::now, LocalDateTime.class);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.strictInsertFill(metaObject, "createTime", () -> sdf.format(new Date()), String.class);
            this.strictInsertFill(metaObject, "creator", RequestUtils::getNickname, String.class);
            this.strictInsertFill(metaObject, "creatorId", RequestUtils::getUserId, String.class);
        } catch (Exception ex) {

        }
    }

    /**
     * 更新填充更新时间
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            this.strictUpdateFill(metaObject, "gmtModified", LocalDateTime::now, LocalDateTime.class);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.strictUpdateFill(metaObject, "updateTime", () -> sdf.format(new Date()), String.class);
            this.strictUpdateFill(metaObject, "updater", RequestUtils::getUsername, String.class);
            this.strictUpdateFill(metaObject, "updaterId", RequestUtils::getUserId, String.class);
        } catch (Exception ex) {

        }
    }

}
