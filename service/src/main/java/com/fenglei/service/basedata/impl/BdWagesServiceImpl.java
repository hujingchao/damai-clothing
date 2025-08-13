package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.constant.Constants;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.result.Result;
import com.fenglei.common.result.ResultCode;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.mapper.basedata.*;
import com.fenglei.model.basedata.*;
import com.fenglei.model.workFlow.dto.FlowDepartment;
import com.fenglei.model.workFlow.dto.FlowOperationInfo;
import com.fenglei.model.workFlow.dto.FlowRole;
import com.fenglei.model.workFlow.dto.FlowUser;
import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.service.basedata.BdProcedureService;
import com.fenglei.service.basedata.BdTempWagesItemService;
import com.fenglei.service.basedata.BdWagesItemService;
import com.fenglei.service.basedata.BdWagesService;
import com.fenglei.service.workFlow.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdWagesServiceImpl extends ServiceImpl<BdWagesMapper, BdWages> implements BdWagesService {

    @Resource
    private BdWagesItemService bdWagesItemService;
    @Resource
    private BdProcedureService bdProcedureService;
    @Resource
    private SystemInformationAcquisitionService systemInformationAcquisitionService;
    @Resource
    private CirculationOperationService circulationOperationService;

    @Resource
    private BdWagesItemMapper bdWagesItemMapper;

    @Resource
    private BdTempWagesItemService bdTempWagesItemService;
    @Resource
    private BdTempWagesMapper bdTempWagesMapper;
    @Resource
    private BdTempWagesItemMapper bdTempWagesItemMapper;

    @Resource
    private BdMaterialMapper bdMaterialMapper;
    @Resource
    private BdTempMaterialMapper bdTempMaterialMapper;

    @Override
    public IPage<BdWages> myPage(Page page, BdWages bdWages) {

        List<String> pids = new ArrayList<>();
        if (StringUtils.isNotEmpty(bdWages.getProcedureId())) {
            List<BdWagesItem> items = bdWagesItemService.list(
                    new LambdaQueryWrapper<BdWagesItem>()
                            .eq(BdWagesItem::getProcedureId, bdWages.getProcedureId())
            );
            if (items != null && items.size() > 0) {
                for (BdWagesItem item : items) {
                    if (!pids.contains(item.getPid())) {
                        pids.add(item.getPid());
                    }
                }
            } else {
                if (pids.size() == 0) {
                    pids.add("-1");
                }
            }
        }
        bdWages.setIds(pids);

        IPage<BdWages> iPage = baseMapper.getPage(page, bdWages);
        if (iPage != null) {
            List<BdWages> records = iPage.getRecords();
            if (records != null && records.size() > 0) {
                List<String> ids = records.stream().map(BdWages::getId).collect(Collectors.toList());
                BdWagesItem bdWagesItem = new BdWagesItem();
                bdWagesItem.setPids(ids);
                List<BdWagesItem> items = bdWagesItemMapper.getList(bdWagesItem);
                if (items != null && items.size() > 0) {
                    for (BdWages record : records) {
                        List<BdWagesItem> myItems = items.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
                        record.setBdWagesItems(myItems);
                    }
                }
            }
        }

        return iPage;
    }

    @Override
    public IPage<BdWages> myPageWithTemp(Page page, BdWages bdWages) throws Exception {

        List<String> pids = new ArrayList<>();
        if (StringUtils.isNotEmpty(bdWages.getProcedureId())) {
            List<BdWagesItem> items = bdWagesItemService.list(
                    new LambdaQueryWrapper<BdWagesItem>()
                            .eq(BdWagesItem::getProcedureId, bdWages.getProcedureId())
            );
            if (items != null && items.size() > 0) {
                for (BdWagesItem item : items) {
                    if (!pids.contains(item.getPid())) {
                        pids.add(item.getPid());
                    }
                }
            } else {
                if (pids.size() == 0) {
                    pids.add("-1");
                }
            }
        }
        bdWages.setIds(pids);

        // 临时
        List<String> tempPids = new ArrayList<>();
        if (StringUtils.isNotEmpty(bdWages.getProcedureId())) {
            List<BdTempWagesItem> items = bdTempWagesItemService.list(
                    new LambdaQueryWrapper<BdTempWagesItem>()
                            .eq(BdTempWagesItem::getProcedureId, bdWages.getProcedureId())
            );
            if (items != null && items.size() > 0) {
                for (BdTempWagesItem item : items) {
                    if (!tempPids.contains(item.getPid())) {
                        tempPids.add(item.getPid());
                    }
                }
            } else {
                if (tempPids.size() == 0) {
                    tempPids.add("-1");
                }
            }
        }
        bdWages.setTempIds(tempPids);
        bdWages.setCreatorId(RequestUtils.getUserId());

        BdTempWages srhTemp = new BdTempWages();
        srhTemp.setCreatorId(RequestUtils.getUserId());
        List<BdTempWages> tempList = bdTempWagesMapper.getList(srhTemp);
        if (tempList != null && tempList.size() > 0) {
            List<String> excludeIds = bdWages.getExcludeIds();
            if (excludeIds == null) {
                excludeIds = new ArrayList<>();
            }

            for (BdTempWages tempWages : tempList) {
                if (StringUtils.isNotEmpty(tempWages.getOriginalId())) {
                    excludeIds.add(tempWages.getOriginalId());
                }
            }
            bdWages.setExcludeIds(excludeIds);
        }

        IPage<BdWages> iPage = baseMapper.getPageWithTemp(page, bdWages);
        if (iPage != null) {
            List<BdWages> records = iPage.getRecords();
            if (records != null && records.size() > 0) {
                List<String> ids = records.stream().filter(r -> (StringUtils.isEmpty(r.getOriginalId()) || StringUtils.equals(r.getOriginalId(), "0")) && r.getStatus() == 2).map(BdWages::getId).collect(Collectors.toList());
                if (ids == null || ids.size() == 0) {
                    ids.add("-1");
                }

                BdWagesItem bdWagesItem = new BdWagesItem();
                bdWagesItem.setPids(ids);
                List<BdWagesItem> items = bdWagesItemMapper.getList(bdWagesItem);

                // 临时
                List<String> tempIds = new ArrayList<>();
                for (BdWages record : records) {
                    if (!ids.contains(record.getId())) {
                        tempIds.add(record.getId());
                    }
                }
                if (tempIds.size() == 0) {
                    tempIds.add("-1");
                }

                BdTempWagesItem bdTempWagesItem = new BdTempWagesItem();
                bdTempWagesItem.setPids(ids);
                List<BdTempWagesItem> tempItems = bdTempWagesItemMapper.getList(bdTempWagesItem);

                for (BdWages record : records) {
                    if ((StringUtils.isEmpty(record.getOriginalId()) || StringUtils.equals(record.getOriginalId(), "0")) && record.getStatus() == 2) {
                        if (items != null && items.size() > 0) {
                            List<BdWagesItem> myItems = items.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
                            record.setBdWagesItems(myItems);
                        }
                    } else {
                        if (tempItems != null && tempItems.size() > 0) {
                            List<BdTempWagesItem> myTempItems = tempItems.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
                            if (myTempItems != null && myTempItems.size() > 0) {
                                List<BdWagesItem> myItems = new ArrayList<>();

                                for (BdTempWagesItem myTempItem : myTempItems) {
                                    BdWagesItem wagesItem = new BdWagesItem();
                                    BeanUtils.copyProperties(wagesItem, myTempItem);

                                    myItems.add(wagesItem);
                                }
                                record.setBdWagesItems(myItems);
                            }
                        }
                    }
                }
            }
        }

        return iPage;
    }

    @Override
    public List<BdWages> myList(BdWages bdWages) {
        List<String> pids = new ArrayList<>();

        List<String> procedureIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(bdWages.getCommFilter())) {
            List<BdProcedure> procedures = bdProcedureService.list(
                    new LambdaQueryWrapper<BdProcedure>()
                            .like(BdProcedure::getNumber, bdWages.getCommFilter())
                            .or()
                            .like(BdProcedure::getName, bdWages.getCommFilter())
            );
            if (procedures != null && procedures.size() > 0) {
                for (BdProcedure procedure : procedures) {
                    procedureIds.add(procedure.getId());
                }
            } else {
                procedureIds.add("-1");
            }
        }

        if (StringUtils.isNotEmpty(bdWages.getProcedureId())) {
            List<BdWagesItem> items = bdWagesItemService.list(
                    new LambdaQueryWrapper<BdWagesItem>()
                            .in(procedureIds != null && procedureIds.size() > 0, BdWagesItem::getProcedureId, procedureIds)
                            .eq(StringUtils.isNotEmpty(bdWages.getProcedureId()), BdWagesItem::getProcedureId, bdWages.getProcedureId())
            );
            if (items != null && items.size() > 0) {
                for (BdWagesItem item : items) {
                    if (!pids.contains(item.getPid())) {
                        pids.add(item.getPid());
                    }
                }
            } else {
                pids.add("-1");
            }
        }

        List<BdWages> list = this.list(
                new LambdaQueryWrapper<BdWages>()
                        .apply(StringUtils.isNotEmpty(bdWages.getCommFilter()), " (number like '%" + bdWages.getCommFilter() + "%' or name like '%" + bdWages.getCommFilter() + "%')")
                        .like(StringUtils.isNotEmpty(bdWages.getNumber()), BdWages::getNumber, bdWages.getNumber())
                        .like(StringUtils.isNotEmpty(bdWages.getName()), BdWages::getName, bdWages.getName())
                        .in(pids.size() > 0, BdWages::getId, pids)
                        .eq(ObjectUtils.isNotEmpty(bdWages.getStatus()), BdWages::getStatus, bdWages.getStatus())
                        .orderByDesc(BdWages::getCreateTime)
        );

        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BdWages add(BdWages bdWages) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdWages.getNumber())) {
            List<BdWages> list = this.list(
                    new LambdaQueryWrapper<BdWages>()
                            .eq(BdWages::getNumber, bdWages.getNumber())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同编码的工价档案");
            }
        } else {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String no = "GJ" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            List<String> nos = baseMapper.getNos(no);
            if (nos != null && nos.size() > 0) {
                String maxNo = nos.get(0);
                Integer pos = maxNo.lastIndexOf("-");
                String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
                Integer maxNoInt = Integer.valueOf(maxIdxStr);
                String noIdxStr = String.format("%04d", maxNoInt + 1);
                no = no + noIdxStr;
            } else {
                no = no + "0001";
            }
            bdWages.setNumber(no);
        }

        if (bdWages.getBdWagesItems() == null || bdWages.getBdWagesItems().size() == 0) {
            throw new BizException("未检测到明细");
        }

        bdWages.setStatus(Constants.INT_STATUS_CREATE);
        bdWages.setCreateTime(sdf.format(new Date()));
        bdWages.setCreatorId(RequestUtils.getUserId());
        bdWages.setCreator(RequestUtils.getNickname());
        bdWages.setEnabled(Constants.BOOL_STATUS_ENABLED);
        bdWages.setEnableTime(sdf.format(new Date()));
        bdWages.setEnablerId(RequestUtils.getUserId());
        bdWages.setEnabler(RequestUtils.getNickname());
        if (!this.save(bdWages)) {
            throw new BizException("保存失败");
        }

        List<BdWagesItem> items = bdWages.getBdWagesItems();
        for (BdWagesItem item : items) {
            item.setPid(bdWages.getId());
            item.setCreateTime(sdf.format(new Date()));
            item.setCreatorId(RequestUtils.getUserId());
            item.setCreator(RequestUtils.getNickname());
        }
        if (!bdWagesItemService.saveBatch(items)) {
            throw new BizException("明细保存失败");
        }

        return bdWages;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BdWages myUpdate(BdWages bdWages) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdWages.getNumber())) {
            List<BdWages> list = this.list(
                    new LambdaQueryWrapper<BdWages>()
                            .eq(BdWages::getNumber, bdWages.getNumber())
                            .ne(BdWages::getId, bdWages.getId())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同编码的工价档案，请修改，异常码1");
            }
        } else {
            BdWages thisWages = this.getById(bdWages.getId());
            if (thisWages != null) {
                bdWages.setNumber(thisWages.getNumber());

                List<BdWages> list = this.list(
                        new LambdaQueryWrapper<BdWages>()
                                .eq(BdWages::getNumber, bdWages.getNumber())
                                .ne(BdWages::getId, bdWages.getId())
                );
                if (list != null && list.size() > 0) {
                    throw new BizException("已存在相同编码的工价档案，请修改，异常码2");
                }
            } else {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String no = "GJ" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
                List<String> nos = baseMapper.getNos(no);
                if (nos != null && nos.size() > 0) {
                    String maxNo = nos.get(0);
                    Integer pos = maxNo.lastIndexOf("-");
                    String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
                    Integer maxNoInt = Integer.valueOf(maxIdxStr);
                    String noIdxStr = String.format("%04d", maxNoInt + 1);
                    no = no + noIdxStr;
                } else {
                    no = no + "0001";
                }
                bdWages.setNumber(no);
            }
        }

        if (bdWages.getBdWagesItems() == null || bdWages.getBdWagesItems().size() == 0) {
            throw new BizException("未检测到明细");
        }

        bdWages.setUpdateTime(sdf.format(new Date()));
        bdWages.setUpdaterId(RequestUtils.getUserId());
        bdWages.setUpdater(RequestUtils.getNickname());
        if (!this.updateById(bdWages)) {
            throw new BizException("更新失败");
        }

        // 删除旧数据
        List<BdWagesItem> oldItems = bdWagesItemService.list(
                new LambdaQueryWrapper<BdWagesItem>()
                        .eq(BdWagesItem::getPid, bdWages.getId())
        );
        if (oldItems != null && oldItems.size() > 0) {
            List<String> oldItemIds = oldItems.stream().map(BdWagesItem::getId).collect(Collectors.toList());
            if (oldItemIds != null && oldItemIds.size() > 0) {
                if (!bdWagesItemService.removeByIds(oldItemIds)) {
                    throw new BizException("明细更新失败，异常码1");
                }
            }
        }

        // 保存新数据
        List<BdWagesItem> newItems = bdWages.getBdWagesItems();
        for (BdWagesItem item : newItems) {
            item.setPid(bdWages.getId());
            item.setUpdateTime(sdf.format(new Date()));
            item.setUpdaterId(RequestUtils.getUserId());
            item.setUpdater(RequestUtils.getNickname());

            if (ObjectUtils.isEmpty(item.getCreatorId())) {
                item.setCreateTime(sdf.format(new Date()));
                item.setCreatorId(RequestUtils.getUserId());
                item.setCreator(RequestUtils.getNickname());
            }
        }
        if (!bdWagesItemService.saveOrUpdateBatch(newItems)) {
            throw new BizException("明细更新失败，异常码2");
        }

        return bdWages;
    }

    Boolean chkIsUsed(List<String> ids) {
        List<BdMaterial> materials = bdMaterialMapper.selectList(
                new LambdaQueryWrapper<BdMaterial>()
                        .in(BdMaterial::getWageId, ids)
        );
        if (materials != null && materials.size() > 0) {
            throw new BizException("当前工价模板已被成品引用，无法删除1");
        }

        List<BdTempMaterial> tempMaterials = bdTempMaterialMapper.selectList(
                new LambdaQueryWrapper<BdTempMaterial>()
                        .in(BdTempMaterial::getWageId, ids)
        );
        if (tempMaterials != null && tempMaterials.size() > 0) {
            throw new BizException("当前工价模板已被成品引用，无法删除2");
        }

        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Long id) {
        BdWages bdWages = this.getById(id);
        if (bdWages == null) {
            return true;
        }
        if (bdWages.getStatus() == Constants.INT_STATUS_APPROVING || bdWages.getStatus() == Constants.INT_STATUS_AUDITED) {
            throw new BizException("流转中 和 已审核 数据无法删除");
        }

        List<String> ids = new ArrayList<>();
        ids.add(id.toString());
        if (this.chkIsUsed(ids)) {
            throw new BizException("存货已被引用");
        }

        if (!this.removeById(id)) {
            throw new BizException("删除失败，异常码1");
        }

        List<BdWagesItem> items = bdWagesItemService.list(
                new LambdaQueryWrapper<BdWagesItem>()
                        .eq(BdWagesItem::getPid, id)
        );
        if (items != null && items.size() > 0) {
            List<String> itemIds = items.stream().map(BdWagesItem::getId).collect(Collectors.toList());
            if (!bdWagesItemService.removeByIds(itemIds)) {
                throw new BizException("删除失败，异常码2");
            }
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<BdWages> list = this.list(
                new LambdaQueryWrapper<BdWages>()
                        .in(BdWages::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<String> delIds = list.stream().filter(r -> r.getStatus() == Constants.INT_STATUS_CREATE || r.getStatus() == Constants.INT_STATUS_RESUBMIT).map(BdWages::getId).collect(Collectors.toList());
            if (delIds != null && delIds.size() > 0) {
                if (this.chkIsUsed(delIds)) {
                    throw new BizException("存货已被引用");
                }

                if (!this.removeByIds(delIds)) {
                    throw new BizException("删除失败，异常码1");
                }
            } else {
                throw new BizException("流转中 及 已审核 数据无法删除");
            }

            List<BdWagesItem> items = bdWagesItemService.list(
                    new LambdaQueryWrapper<BdWagesItem>()
                            .in(BdWagesItem::getPid, delIds)
            );
            if (items != null && items.size() > 0) {
                List<String> itemIds = items.stream().map(BdWagesItem::getId).collect(Collectors.toList());
                if (!bdWagesItemService.removeByIds(itemIds)) {
                    throw new BizException("删除失败，异常码2");
                }
            }
        }
    }

    @Override
    public BdWages detail(String id) {
        BdWages result = baseMapper.infoById(id);
        if (result != null) {
            BdWagesItem bdWagesItem = new BdWagesItem();
            bdWagesItem.setPid(id);
            List<BdWagesItem> items = bdWagesItemMapper.getList(bdWagesItem);
            result.setBdWagesItems(items);
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BdWages submit(String id) throws Exception {
        BdWages result = this.detail(id);
        if (result == null) {
            throw new BizException("未检索到数据");
        }
        if (result.getStatus() != Constants.INT_STATUS_CREATE && result.getStatus() != Constants.INT_STATUS_RESUBMIT) {
            throw new BizException("提交失败，仅 '创建' 和 '重新审核' 状态允许提交");
        }

        result = this.auditUptData(result);

        if (!this.updateById(result)) {
            throw new BizException("操作失败");
        }

        return result;
    }

    private BdWages auditUptData(BdWages bdWages) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        bdWages.setStatus(Constants.INT_STATUS_AUDITED);
        bdWages.setAuditTime(sdf.format(new Date()));
        bdWages.setAuditorId(RequestUtils.getUserId());
        bdWages.setAuditor(RequestUtils.getNickname());

        return bdWages;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchSubmitByIds(String[] ids) throws Exception {
        List<String> idList = Arrays.asList(ids);
        List<BdWages> list = this.list(
                new LambdaQueryWrapper<BdWages>()
                        .in(BdWages::getId, idList)
        );
        if (list != null && list.size() > 0) {
            // 过滤 创建/重新审核 且 启用 的数据
            List<BdWages> submitList = list.stream().filter(r -> (r.getStatus() == Constants.INT_STATUS_CREATE || r.getStatus() == Constants.INT_STATUS_RESUBMIT) && r.getEnabled()).collect(Collectors.toList());
            if (submitList != null && submitList.size() > 0) {
                for (BdWages bdWages : submitList) {
                    this.submit(bdWages.getId());
                }
            }

            List<BdWages> errList = list.stream().filter(r -> (r.getStatus() == Constants.INT_STATUS_CREATE || r.getStatus() == Constants.INT_STATUS_RESUBMIT) && r.getEnabled() == Constants.BOOL_STATUS_UNENABLED).collect(Collectors.toList());
            if (errList != null && errList.size() > 0) {
                return "存在禁用数据无法提交";
            }

            return "提交成功";
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result doAction(BdWages bdWages) throws Exception {
        if (bdWages.getStatus() == Constants.INT_STATUS_APPROVING && ObjectUtils.isNotEmpty(bdWages.getWorkFlow())) {
            FlowOperationInfo flowOperationInfo = new FlowOperationInfo();
            flowOperationInfo.setWorkFlowId(bdWages.getWorkFlowId());
            flowOperationInfo.setFormData(bdWages);
            flowOperationInfo.setUserId(bdWages.getUserId());
            flowOperationInfo.setChildNodes(bdWages.getChildNodes());
            flowOperationInfo.setCurrentNodeId(bdWages.getCurrentNodeId());
            flowOperationInfo.setChildNodeApprovalResult(bdWages.getChildNodeApprovalResult());
            if (StringUtils.isNotEmpty(flowOperationInfo.getWorkFlowId())) {
                // 提交
                List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
                Boolean start = circulationOperationService.startFlow(flowOperationInfo.getWorkFlowId(), flowOperationInfo.getFormData(), flowOperationInfo.getUserId(), flowOperationInfo.getChildNodes(), flowUsers);
                if (!start) {
                    throw new BizException("流程提交错误");
                }
                bdWages.setWorkFlowId("");
            } else {
                // 审批流程
                List<FlowUser> flowUsers = systemInformationAcquisitionService.getFlowUsers(null);
                List<FlowRole> flowRoles = systemInformationAcquisitionService.getFlowRoles(null);
                List<FlowDepartment> flowDepartments = systemInformationAcquisitionService.getFlowDepartments(null);
                Boolean next = circulationOperationService.doNextFlow(flowOperationInfo.getFormData(), flowUsers, flowRoles, flowDepartments, flowOperationInfo.getChildNodeApprovalResult(), flowOperationInfo.getUserId());
                if (!next) {
                    throw new BizException("流程提交失败");
                }
            }
            List<String> ids = new ArrayList<>();
            ids.add(bdWages.getId());
            List<ChildNode> currentNodes = getCurrentNodes(ids, bdWages.getWorkFlow().getId());
            bdWages.setWorkFlowInstantiateStatus(currentNodes.get(0).getWorkFlowInstantiateStatus());
            bdWages.setNodeStatus(currentNodes.get(0).getStatus());
            bdWages.setCurrentNodeId(currentNodes.get(0).getId());
            // 审批流正常结束
            if (circulationOperationService.whetherLast(bdWages.getId()) == 1) {
                bdWages = this.auditUptData(bdWages);
            }
            // 驳回
            if (circulationOperationService.whetherLast(bdWages.getId()) == 2) {
                bdWages.setStatus(Constants.INT_STATUS_RESUBMIT);
            }
        }

        if (!this.updateById(bdWages)) {
            throw new BizException("操作失败");
        }

        return Result.success(bdWages);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result batchDoAction(BdWages bdWages) throws Exception {
        List<String> ids = bdWages.getIds();
        List<BdWages> wages = this.list(
                new LambdaQueryWrapper<BdWages>()
                        .in(BdWages::getId, ids)
        );
        if (wages != null && wages.size() > 0) {
            List<ChildNode> childNodes = getCurrentNodes(ids, bdWages.getWorkFlow().getId());
            for (int i = 0; i < wages.size(); i++) {
                BdWages item = wages.get(i);
                item.setStatus(bdWages.getStatus());
                item.setWorkFlowId(bdWages.getWorkFlowId());
                item.setUserId(bdWages.getUserId());
                item.setChildNodes(bdWages.getChildNodes());
                item.setChildNodeApprovalResult(bdWages.getChildNodeApprovalResult());
                item.setWorkFlow(bdWages.getWorkFlow());
                for (int j = 0; j < childNodes.size(); j++) {
                    if (childNodes.get(j).getFromId().equals(item.getId())) {
                        item.setWorkFlowInstantiateStatus(childNodes.get(j).getWorkFlowInstantiateStatus());
                        item.setNodeStatus(childNodes.get(j).getStatus());
                        item.setCurrentNodeId(childNodes.get(j).getId());
                        break;
                    }
                }
                Result result = doAction(item);
                if (!ResultCode.SUCCESS.getCode().equalsIgnoreCase(result.getCode())) {
                    throw new BizException("操作失败");
                }
            }
        }

        return Result.success("操作成功");
    }

    private List<ChildNode> getCurrentNodes(List<String> ids, String workFlowId){
        FlowOperationInfo flowOperationInfo =new FlowOperationInfo();
        FlowUser flowUser = new FlowUser();
        flowUser.setEmployeeName(RequestUtils.getNickname());
        flowUser.setId(RequestUtils.getUserId());
        flowOperationInfo.setFlowUser(flowUser);
        flowOperationInfo.setFormIds(ids);
        flowOperationInfo.setWorkFlowId(workFlowId);

        List<ChildNode> childNodeList = circulationOperationService.getCurrentNodes(flowOperationInfo);
        return childNodeList;
    }

    @Override
    public BdWages unAudit(String id) throws Exception {
        BdWages result = this.detail(id);
        if (result == null) {
            throw new BizException("未检索到数据");
        }
        if (result.getStatus() != Constants.INT_STATUS_AUDITED) {
            throw new BizException("反审核失败，仅 '已完成' 状态允许反审核");
        }

        result = this.unAuditUptData(result);
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public BdWages unAuditUptData(BdWages bdWages) throws Exception {
        bdWages.setStatus(Constants.INT_STATUS_RESUBMIT);
        bdWages.setAuditTime(null);
        bdWages.setAuditorId(null);
        bdWages.setAuditor(null);
        if (!this.updateById(bdWages)) {
            throw new BizException("反审核失败");
        }

        // 生成临时数据
        BdWages thisData = this.detail(bdWages.getId());
        if (thisData != null) {
            BdTempWages tempWages = new BdTempWages();
            BeanUtils.copyProperties(tempWages, thisData);

            tempWages.setOriginalId(tempWages.getId());
            tempWages.setStatus(3);

            IdentifierGenerator identifierGenerator = new DefaultIdentifierGenerator();
            String id = identifierGenerator.nextId(new Object()).toString();
            tempWages.setId(id);

            if (bdTempWagesMapper.insert(tempWages) < 1) {
                throw new BizException("反审核失败，请重试");
            }

            // 明细
            List<BdWagesItem> wagesItems = thisData.getBdWagesItems();
            if (wagesItems != null && wagesItems.size() > 0) {
                List<BdTempWagesItem> tempWagesItems = new ArrayList<>();

                for (BdWagesItem wagesItem : wagesItems) {
                    BdTempWagesItem tempWagesItem = new BdTempWagesItem();
                    BeanUtils.copyProperties(tempWagesItem, wagesItem);

                    tempWagesItem.setId(null);
                    tempWagesItem.setPid(id);

                    tempWagesItems.add(tempWagesItem);
                }
                if (!bdTempWagesItemService.saveBatch(tempWagesItems)) {
                    throw new BizException("反审核失败，明细异常，请重试");
                }
            }

            // 替换为临时id
            bdWages.setOriginalId(bdWages.getId());
            bdWages.setId(id);
        } else {
            throw new BizException("数据不存在");
        }

        return bdWages;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchUnAuditByIds(String[] ids) throws Exception {
        List<String> idList = Arrays.asList(ids);
        List<BdWages> list = this.list(
                new LambdaQueryWrapper<BdWages>()
                        .in(BdWages::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<BdWages> unAuditList = list.stream().filter(r -> r.getStatus() == Constants.INT_STATUS_AUDITED && r.getEnabled()).collect(Collectors.toList());
            if (unAuditList != null && unAuditList.size() > 0) {
                for (BdWages bdWages : unAuditList) {
                    this.unAudit(bdWages.getId());
                }
            }

            List<BdWages> errList = list.stream().filter(r -> r.getStatus() == Constants.INT_STATUS_AUDITED && r.getEnabled() == Constants.BOOL_STATUS_UNENABLED).collect(Collectors.toList());
            if (errList != null && errList.size() > 0) {
                return "存在禁用数据无法反审核";
            }

            return "反审核成功";
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BdWages cancel(String id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        BdWages result = this.detail(id);
        if (result == null) {
            throw new BizException("未检索到数据");
        }
        if (result.getStatus() == Constants.INT_STATUS_APPROVING) {
            throw new BizException("禁用失败，流转中数据无法禁用");
        }

        if (result.getEnabled() == Constants.BOOL_STATUS_UNENABLED) {
//            throw new BizException("禁用失败，仅启用状态允许禁用");
        } else {
            result.setEnabled(Constants.BOOL_STATUS_UNENABLED);
            result.setEnableTime(sdf.format(new Date()));
            result.setEnablerId(RequestUtils.getUserId());
            result.setEnabler(RequestUtils.getNickname());
            if (!this.updateById(result)) {
                throw new BizException("禁用失败");
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchCancelByIds(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<BdWages> list = this.list(
                new LambdaQueryWrapper<BdWages>()
                        .in(BdWages::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<BdWages> cancelList = list.stream().filter(r -> r.getEnabled() == Constants.BOOL_STATUS_ENABLED && r.getStatus() != Constants.INT_STATUS_APPROVING).collect(Collectors.toList());
            if (cancelList != null && cancelList.size() > 0) {
                for (BdWages bdWages : cancelList) {
                    this.cancel(bdWages.getId());
                }
            }

            if (cancelList.size() == list.size()) {
                return "禁用成功";
            } else {
                return "禁用成功，部分数据处于流转中，无法禁用";
            }
        } else {
            throw new BizException("未选择数据");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BdWages unCancel(String id) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        BdWages result = this.detail(id);
        if (result == null) {
            throw new BizException("未检索到数据");
        }
        if (result.getStatus() == Constants.INT_STATUS_APPROVING) {
            throw new BizException("反禁用失败，流转中数据无法禁用");
        }

        if (result.getEnabled()) {
//            throw new BizException("反禁用失败，仅禁用状态允许反禁用");
        } else {
            result.setEnabled(Constants.BOOL_STATUS_ENABLED);
            result.setEnableTime(sdf.format(new Date()));
            result.setEnablerId(RequestUtils.getUserId());
            result.setEnabler(RequestUtils.getNickname());
            if (!this.updateById(result)) {
                throw new BizException("反禁用失败");
            }
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchUnCancelByIds(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<BdWages> list = this.list(
                new LambdaQueryWrapper<BdWages>()
                        .in(BdWages::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<BdWages> unCancelList = list.stream().filter(r -> r.getEnabled() == Constants.BOOL_STATUS_UNENABLED && r.getStatus() != Constants.INT_STATUS_APPROVING).collect(Collectors.toList());
            if (unCancelList != null && unCancelList.size() > 0) {
                for (BdWages bdWages : unCancelList) {
                    this.unCancel(bdWages.getId());
                }
            }

            if (unCancelList.size() == list.size()) {
                return "反禁用成功";
            } else {
                return "反禁用成功，部分数据处于流转中，无法反禁用";
            }
        } else {
            throw new BizException("未选择数据");
        }
    }
}
