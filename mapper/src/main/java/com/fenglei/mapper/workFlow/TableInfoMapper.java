package com.fenglei.mapper.workFlow;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.workFlow.entity.TableInfo;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author yzy
 */

public interface TableInfoMapper extends BaseMapper<TableInfo> {
    /**
     * 通过表单代号获取单据列表
     * @param tableCode 表单代号
     * @return List<HashMap<String, Object>>
     */
    List<HashMap<String, Object>> getOrderList(@Param(value = "tableCode") String tableCode);
}
