package com.fenglei.service.workFlow;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.workFlow.entity.TableInfo;

import java.util.List;
import java.util.Map;


/**
 * @author yzy
 */
public interface ITableInfoService extends IService<TableInfo> {

    /**
     * 添加外部表单
     * @param tableInfo 查询数据
     * @return int
     */
    int add(TableInfo tableInfo);

    /**
     * 修改外部表单
     * @param tableInfo 查询数据
     * @return int
     */
    int update(TableInfo tableInfo);

    /**
     * 通过id删除外部表单
     * @param id 表单id
     * @return int
     */
    int delete(String id);

    /**
     * 通过id获取外部表单
     * @param id 表单ID
     * @return TableInfoDTO
     */
    TableInfo findById(String id);

    /**
     * 通过id获取表单列表
     * @param id 表单ID
     * @return Map
     */
    Map<String, Object> getOrderList(String id);

    /**
     * 获取所有外部表单
     * @return List
     */
    List<TableInfo> getTableInfos();

    void deleteBatch(String ids);


}
