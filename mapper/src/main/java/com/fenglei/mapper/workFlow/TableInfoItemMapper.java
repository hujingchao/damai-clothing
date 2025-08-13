package com.fenglei.mapper.workFlow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.workFlow.entity.TableInfoItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author yzy
 */

public interface TableInfoItemMapper extends BaseMapper<TableInfoItem> {

    /**
     * 批量插入
     * @param tableInfoItems 外部表单子项
     * @return Boolean
     */
    Boolean insertBatch(@Param(value = "tableInfoItems") List<TableInfoItem> tableInfoItems);

    /**
     * 通过子表id获取字段信息
     * @param tableInfoId 外部表单Id
     * @return List<TableInfoItem>
     */
    List<TableInfoItem> getTableInfoItemByTableInfoId(String tableInfoId);

    /**
     * 批量更新字段信息
     * @param tableInfoItems 外部表单子项
     * @return Boolean
     */
    Boolean updateBatch(@Param(value = "tableInfoItems") List<TableInfoItem> tableInfoItems);

}
