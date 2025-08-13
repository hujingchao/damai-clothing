package com.fenglei.service.workFlow.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.util.StringUtils;
import com.fenglei.mapper.workFlow.TableInfoMapper;
import com.fenglei.model.workFlow.entity.TableInfo;
import com.fenglei.model.workFlow.entity.TableInfoItem;
import com.fenglei.service.workFlow.ITableInfoItemService;
import com.fenglei.service.workFlow.ITableInfoService;
import com.fenglei.service.workFlow.util.ChildNodeUtil;
import com.fenglei.service.workFlow.util.ClassConversionTools;
import com.fenglei.service.workFlow.util.UpdateItemToGetNewItems;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author pc
 */
@Service
public class TableInfoServiceImpl extends ServiceImpl<TableInfoMapper, TableInfo> implements ITableInfoService {

    @Resource
    private ITableInfoItemService iTableInfoItemService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int add(TableInfo tableInfo) {
        List<TableInfoItem> tableInfoItems = tableInfo.getTableInfoItems();
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        tableInfo.setId(id);
        tableInfo.setCreateTime(new Date());
        tableInfo.setUpdateTime(new Date());
        for (TableInfoItem tableInfoItem : tableInfoItems) {
            String tid = UUID.randomUUID().toString().replaceAll("-", "");
            tableInfoItem.setId(tid);
            tableInfoItem.setTableInfoId(id);
            tableInfoItem.setCreateTime(new Date());
            tableInfoItem.setUpdateTime(new Date());
        }
        iTableInfoItemService.saveOrUpdateBatch(tableInfoItems);
        return baseMapper.insert(tableInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(TableInfo tableInfo1) {
        TableInfo tableInfo = baseMapper.selectById(tableInfo1.getId());
        tableInfo.setUpdateTime(new Date());
        tableInfo.setTableCode(tableInfo1.getTableCode());
        tableInfo.setTableName(tableInfo1.getTableName());
        tableInfo.setFullName(tableInfo1.getFullName());
        tableInfo.setFormName(tableInfo1.getFormName());
        List<TableInfoItem> tableInfoItemList = iTableInfoItemService.list(new QueryWrapper<TableInfoItem>().lambda().eq(TableInfoItem::getTableInfoId, tableInfo.getId()));
        List<TableInfoItem> tableInfoItems1 = tableInfo1.getTableInfoItems();
        List<String> needDelIds = UpdateItemToGetNewItems.needDelIds(tableInfoItemList, tableInfoItems1);
        if (needDelIds.size() > ChildNodeUtil.NUMBER_INT_0) {
            iTableInfoItemService.removeByIds(new HashSet<>(needDelIds));
        }
        List<TableInfoItem> newItems = ClassConversionTools.castList(UpdateItemToGetNewItems.get(UpdateItemToGetNewItems.getIds(tableInfoItemList), UpdateItemToGetNewItems.getIds(tableInfoItems1), tableInfoItemList, TableInfoItem.class), TableInfoItem.class);
        List<TableInfoItem> tableInfoItems = new ArrayList<>();
        for (TableInfoItem tableInfoItem1 : tableInfoItems1) {
            if (StringUtils.isNotEmpty(tableInfoItem1.getId())) {
                for (TableInfoItem tableInfoItem : newItems) {
                    if (tableInfoItem.getId().equals(tableInfoItem1.getId())) {
                        tableInfoItem.setUpdateTime(new Date());
                        tableInfoItem.setFieldCode(tableInfoItem1.getFieldCode());
                        tableInfoItem.setFieldType(tableInfoItem1.getFieldType());
                        tableInfoItem.setFieldName(tableInfoItem1.getFieldName());
                        tableInfoItems.add(tableInfoItem);
                    }
                }
            } else {
                tableInfoItem1.setUpdateTime(new Date());
                tableInfoItem1.setTableInfoId(tableInfo.getId());
                tableInfoItem1.setCreateTime(new Date());
                tableInfoItems.add(tableInfoItem1);
            }
        }
        iTableInfoItemService.saveOrUpdateBatch(tableInfoItems);
        return baseMapper.updateById(tableInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int delete(String id) {
        iTableInfoItemService.remove(new UpdateWrapper<TableInfoItem>().lambda().eq(TableInfoItem::getTableInfoId, id));
        return baseMapper.deleteById(id);
    }


    @Override
    public TableInfo findById(String id) {
        TableInfo tableInfoDTO = baseMapper.selectById(id);
        if (Objects.nonNull(tableInfoDTO)) {
            tableInfoDTO.setTableInfoItems(iTableInfoItemService.list(new QueryWrapper<TableInfoItem>().lambda().eq(TableInfoItem::getTableInfoId, id)));
        }
        return tableInfoDTO;
    }

    @Override
    public Map<String, Object> getOrderList(String id) {
        TableInfo tableInfo = baseMapper.selectById(id);
        List<TableInfoItem> tableInfoItems = iTableInfoItemService.list(new QueryWrapper<TableInfoItem>().lambda().eq(TableInfoItem::getTableInfoId, id));
        tableInfo.setTableInfoItems(tableInfoItems);
        String tableCode = tableInfo.getTableCode();
        List<HashMap<String, Object>> maps = baseMapper.getOrderList(tableCode);
        Map<String, Object> map = new HashMap<>(2);
        List<String> dateStrs = new ArrayList<>();
        for (TableInfoItem tableInfoItem : tableInfoItems) {
            if ("datetime".equals(tableInfoItem.getFieldType())) {
                dateStrs.add(tableInfoItem.getFieldCode());
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (String dateStr : dateStrs) {
            for (HashMap<String, Object> m : maps) {
                if (m.get(dateStr) != null) {
                    String s = sdf.format((Date) m.get(dateStr));
                    m.put(dateStr, s);
                }
            }
        }
        map.put("tableInfo", tableInfo);
        map.put("maps", maps);
        return map;
    }

    @Override
    public List<TableInfo> getTableInfos() {
        List<TableInfo> tableInfos = this.list(new QueryWrapper<TableInfo>().lambda());
        Set<String> tableInfoIds = tableInfos.stream().map(TableInfo::getId).collect(Collectors.toSet());
        if (tableInfoIds.size() > ChildNodeUtil.NUMBER_INT_0) {
            List<TableInfoItem> tableInfoItems = iTableInfoItemService.list(new QueryWrapper<TableInfoItem>().lambda().in(TableInfoItem::getTableInfoId, tableInfoIds));
            for (TableInfo tableInfoDTO : tableInfos) {
                List<TableInfoItem> tableInfoItemList = new ArrayList<>();
                for (TableInfoItem tableInfoItem : tableInfoItems) {
                    if (tableInfoDTO.getId().equals(tableInfoItem.getTableInfoId())) {
                        tableInfoItemList.add(tableInfoItem);
                    }
                }
                tableInfoDTO.setTableInfoItems(tableInfoItemList);
            }
        }
        return tableInfos;
    }

    @Override
    public void deleteBatch(String ids) {
        removeByIds(Arrays.asList(ids));
        List<String> idList = Arrays.stream(ids.split(",")).map(String::valueOf).collect(Collectors.toList());
        List<TableInfoItem> tableInfoItemList = iTableInfoItemService.list(new LambdaQueryWrapper<TableInfoItem>()
                .in(TableInfoItem::getTableInfoId,idList )
        );
        if (tableInfoItemList!=null&&tableInfoItemList.size()>0){
            List<String> itemIdList =tableInfoItemList.stream().map(TableInfoItem::getId).collect(Collectors.toList());
            iTableInfoItemService.removeByIds(itemIdList);
        }
    }

}
