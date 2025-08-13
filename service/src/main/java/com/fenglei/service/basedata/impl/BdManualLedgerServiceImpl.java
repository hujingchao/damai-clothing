package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.mapper.basedata.BdManualLedgerMapper;
import com.fenglei.mapper.basedata.BdManualLedgerProcedureMapper;
import com.fenglei.mapper.basedata.BdManualLedgerStaffMapper;
import com.fenglei.model.basedata.*;
import com.fenglei.model.system.entity.SysUser;
import com.fenglei.service.basedata.BdManualLedgerProcedureService;
import com.fenglei.service.basedata.BdManualLedgerService;
import com.fenglei.service.basedata.BdManualLedgerStaffService;
import com.fenglei.service.basedata.BdProcedureService;
import com.fenglei.service.system.ISysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdManualLedgerServiceImpl extends ServiceImpl<BdManualLedgerMapper, BdManualLedger> implements BdManualLedgerService {

    @Resource
    private BdManualLedgerProcedureService bdManualLedgerProcedureService;
    @Resource
    private BdManualLedgerStaffService bdManualLedgerStaffService;
    @Resource
    private BdProcedureService bdProcedureService;
    @Resource
    private ISysUserService sysUserService;

    @Resource
    private BdManualLedgerProcedureMapper bdManualLedgerProcedureMapper;
    @Resource
    private BdManualLedgerStaffMapper bdManualLedgerStaffMapper;

    @Override
    public IPage<BdManualLedger> myPage(Page page, BdManualLedger bdManualLedger) {

        List<String> pids = new ArrayList<>();
        if (StringUtils.isNotEmpty(bdManualLedger.getProcedureId())) {
            List<BdManualLedgerProcedure> procedures = bdManualLedgerProcedureService.list(
                    new LambdaQueryWrapper<BdManualLedgerProcedure>()
                            .eq(BdManualLedgerProcedure::getProcedureId, bdManualLedger.getProcedureId())
            );
            if (procedures != null && procedures.size() > 0) {
                for (BdManualLedgerProcedure procedure : procedures) {
                    if (!pids.contains(procedure.getPid())) {
                        pids.add(procedure.getPid());
                    }
                }
            } else {
                if (pids.size() == 0) {
                    pids.add("-1");
                }
            }
        }
        if (StringUtils.isNotEmpty(bdManualLedger.getStaffId())) {
            List<BdManualLedgerStaff> staffs = bdManualLedgerStaffService.list(
                    new LambdaQueryWrapper<BdManualLedgerStaff>()
                            .eq(BdManualLedgerStaff::getStaffId, bdManualLedger.getStaffId())
            );
            if (staffs != null && staffs.size() > 0) {
                for (BdManualLedgerStaff staff : staffs) {
                    if (!pids.contains(staff.getPid())) {
                        pids.add(staff.getPid());
                    }
                }
            } else {
                if (pids.size() == 0) {
                    pids.add("-1");
                }
            }

            List<BdManualLedger> manualLedgers = this.list(
                    new LambdaQueryWrapper<BdManualLedger>()
                            .eq(BdManualLedger::getLeaderId, bdManualLedger.getStaffId())
            );
            if (manualLedgers != null && manualLedgers.size() > 0) {
                for (BdManualLedger manualLedger : manualLedgers) {
                    if (!pids.contains(manualLedger.getId())) {
                        pids.add(manualLedger.getId());
                    }
                }
            } else {
                if (pids.size() == 0) {
                    pids.add("-1");
                }
            }
        }
        bdManualLedger.setIds(pids);

        IPage<BdManualLedger> iPage = baseMapper.getPage(page, bdManualLedger);
        if (iPage != null) {
            List<BdManualLedger> records = iPage.getRecords();
            if (records != null && records.size() > 0) {
                List<String> ids = records.stream().map(BdManualLedger::getId).collect(Collectors.toList());

                BdManualLedgerProcedure bdManualLedgerProcedure = new BdManualLedgerProcedure();
                bdManualLedgerProcedure.setPids(ids);
                List<BdManualLedgerProcedure> procedures = bdManualLedgerProcedureMapper.getList(bdManualLedgerProcedure);

                BdManualLedgerStaff bdManualLedgerStaff = new BdManualLedgerStaff();
                bdManualLedgerStaff.setPids(ids);
                List<BdManualLedgerStaff> staffs = bdManualLedgerStaffMapper.getList(bdManualLedgerStaff);

                for (BdManualLedger record : records) {
                    if (procedures != null && procedures.size() > 0) {
                        List<BdManualLedgerProcedure> myProcedures = procedures.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
                        record.setProcedures(myProcedures);
                    }

                    if (staffs != null && staffs.size() > 0) {
                        List<BdManualLedgerStaff> myStaffs = staffs.stream().filter(r -> r.getPid().equals(record.getId())).collect(Collectors.toList());
                        record.setStaffs(myStaffs);
                    }
                }
            }
        }

        return iPage;
    }

    @Override
    public List<BdManualLedger> myList(BdManualLedger bdManualLedger) {
        List<String> pids = new ArrayList<>();

        if (StringUtils.isNotEmpty(bdManualLedger.getCommFilter()) || StringUtils.isNotEmpty(bdManualLedger.getProcedureId())) {
            List<String> procedureIds = new ArrayList<>();
            if (StringUtils.isNotEmpty(bdManualLedger.getCommFilter())) {
                List<BdProcedure> procedures = bdProcedureService.list(
                        new LambdaQueryWrapper<BdProcedure>()
                                .like(BdProcedure::getNumber, bdManualLedger.getCommFilter())
                                .or()
                                .like(BdProcedure::getName, bdManualLedger.getCommFilter())
                );
                if (procedures != null && procedures.size() > 0) {
                    for (BdProcedure procedure : procedures) {
                        procedureIds.add(procedure.getId());
                    }
                } else {
                    procedureIds.add("-1");
                }
            }
            List<BdManualLedgerProcedure> procedures = bdManualLedgerProcedureService.list(
                    new LambdaQueryWrapper<BdManualLedgerProcedure>()
                            .in(procedureIds != null && procedureIds.size() > 0, BdManualLedgerProcedure::getProcedureId, procedureIds)
                            .eq(StringUtils.isNotEmpty(bdManualLedger.getProcedureId()), BdManualLedgerProcedure::getProcedureId, bdManualLedger.getProcedureId())
            );
            if (procedures != null && procedures.size() > 0) {
                for (BdManualLedgerProcedure procedure : procedures) {
                    if (!pids.contains(procedure.getPid())) {
                        pids.add(procedure.getPid());
                    }
                }
            }
            if (pids.size() == 0) {
                pids.add("-1");
            }
        }

        if (StringUtils.isNotEmpty(bdManualLedger.getCommFilter()) || StringUtils.isNotEmpty(bdManualLedger.getStaffId())) {
            List<String> staffIds = new ArrayList<>();
            if (StringUtils.isNotEmpty(bdManualLedger.getCommFilter())) {
                List<SysUser> users = sysUserService.list(
                        new LambdaQueryWrapper<SysUser>()
                                .like(SysUser::getUsername, bdManualLedger.getCommFilter())
                                .or()
                                .like(SysUser::getPhone, bdManualLedger.getCommFilter())
                );
                if (users != null && users.size() > 0) {
                    for (SysUser user : users) {
                        staffIds.add(user.getId());
                    }
                }
                if (staffIds.size() == 0) {
                    staffIds.add("-1");
                }
            }
            if (StringUtils.isNotEmpty(bdManualLedger.getStaffId())) {
                List<BdManualLedgerStaff> staffs = bdManualLedgerStaffService.list(
                        new LambdaQueryWrapper<BdManualLedgerStaff>()
                                .in(staffIds != null && staffIds.size() > 0, BdManualLedgerStaff::getStaffId, staffIds)
                                .eq(StringUtils.isNotEmpty(bdManualLedger.getStaffId()), BdManualLedgerStaff::getStaffId, bdManualLedger.getStaffId())
                );
                if (staffs != null && staffs.size() > 0) {
                    for (BdManualLedgerStaff staff : staffs) {
                        if (!pids.contains(staff.getPid())) {
                            pids.add(staff.getPid());
                        }
                    }
                }
            }
            if (staffIds != null && staffIds.size() > 0) {
                List<BdManualLedger> manualLedgers = this.list(
                        new LambdaQueryWrapper<BdManualLedger>()
                                .in(BdManualLedger::getLeader, staffIds)
                );
                if (manualLedgers != null && manualLedgers.size() > 0) {
                    for (BdManualLedger manualLedger : manualLedgers) {
                        if (!pids.contains(manualLedger.getId())) {
                            pids.add(manualLedger.getId());
                        }
                    }
                }
            }
            if (pids.size() == 0) {
                pids.add("-1");
            }
        }

        List<BdManualLedger> list = this.list(
                new LambdaQueryWrapper<BdManualLedger>()
                        .apply(StringUtils.isNotEmpty(bdManualLedger.getCommFilter()), " (number like '%" + bdManualLedger.getCommFilter() + "%' or name like '%" + bdManualLedger.getCommFilter() + "%')")
                        .like(StringUtils.isNotEmpty(bdManualLedger.getNumber()), BdManualLedger::getNumber, bdManualLedger.getNumber())
                        .like(StringUtils.isNotEmpty(bdManualLedger.getName()), BdManualLedger::getName, bdManualLedger.getName())
                        .in(pids.size() > 0, BdManualLedger::getId, pids)
                        .orderByDesc(BdManualLedger::getCreateTime)
        );

        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BdManualLedger add(BdManualLedger bdManualLedger) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdManualLedger.getNumber())) {
            List<BdManualLedger> list = this.list(
                    new LambdaQueryWrapper<BdManualLedger>()
                            .eq(BdManualLedger::getNumber, bdManualLedger.getNumber())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同编码的手工帐");
            }
        } else {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String no = "SGZ" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            List<BdManualLedger> list = this.list(
                    new LambdaQueryWrapper<BdManualLedger>()
                            .likeRight(BdManualLedger::getNumber, no)
                            .orderByDesc(BdManualLedger::getNumber)
            );
            if (list != null && list.size() > 0) {
                String maxNo = list.get(0).getNumber();
                Integer pos = maxNo.lastIndexOf("-");
                String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
                Integer maxNoInt = Integer.valueOf(maxIdxStr);
                String noIdxStr = String.format("%04d", maxNoInt + 1);
                no = no + noIdxStr;
            } else {
                no = no + "0001";
            }
            bdManualLedger.setNumber(no);
        }

        if (bdManualLedger.getProcedures() == null || bdManualLedger.getProcedures().size() == 0) {
            throw new BizException("未检测到工序明细");
        }
        if (bdManualLedger.getStaffs() == null || bdManualLedger.getStaffs().size() == 0) {
            throw new BizException("未检测到员工明细");
        }

        bdManualLedger.setCreateTime(sdf.format(new Date()));
        bdManualLedger.setCreatorId(RequestUtils.getUserId());
        bdManualLedger.setCreator(RequestUtils.getNickname());
        if (!this.save(bdManualLedger)) {
            throw new BizException("保存失败");
        }

        List<BdManualLedgerProcedure> procedures = bdManualLedger.getProcedures();
        for (BdManualLedgerProcedure procedure : procedures) {
            procedure.setPid(bdManualLedger.getId());
            procedure.setCreateTime(sdf.format(new Date()));
            procedure.setCreatorId(RequestUtils.getUserId());
            procedure.setCreator(RequestUtils.getNickname());
        }
        if (!bdManualLedgerProcedureService.saveBatch(procedures)) {
            throw new BizException("工序明细保存失败");
        }

        List<BdManualLedgerStaff> staffs = bdManualLedger.getStaffs();
        for (BdManualLedgerStaff staff : staffs) {
            staff.setPid(bdManualLedger.getId());
            staff.setCreateTime(sdf.format(new Date()));
            staff.setCreatorId(RequestUtils.getUserId());
            staff.setCreator(RequestUtils.getNickname());
        }
        if (!bdManualLedgerStaffService.saveBatch(staffs)) {
            throw new BizException("员工明细保存失败");
        }

        return bdManualLedger;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BdManualLedger myUpdate(BdManualLedger bdManualLedger) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdManualLedger.getNumber())) {
            List<BdManualLedger> list = this.list(
                    new LambdaQueryWrapper<BdManualLedger>()
                            .eq(BdManualLedger::getNumber, bdManualLedger.getNumber())
                            .ne(BdManualLedger::getId, bdManualLedger.getId())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同编码的手工帐，请修改，异常码1");
            }
        } else {
            BdManualLedger thisManualLedger = this.getById(bdManualLedger.getId());
            if (thisManualLedger != null) {
                bdManualLedger.setNumber(thisManualLedger.getNumber());

                List<BdManualLedger> list = this.list(
                        new LambdaQueryWrapper<BdManualLedger>()
                                .eq(BdManualLedger::getNumber, bdManualLedger.getNumber())
                                .ne(BdManualLedger::getId, bdManualLedger.getId())
                );
                if (list != null && list.size() > 0) {
                    throw new BizException("已存在相同编码的手工帐，请修改，异常码2");
                }
            } else {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String no = "SGZ" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
                List<BdManualLedger> list = this.list(
                        new LambdaQueryWrapper<BdManualLedger>()
                                .likeRight(BdManualLedger::getNumber, no)
                                .orderByDesc(BdManualLedger::getNumber)
                );
                if (list != null && list.size() > 0) {
                    String maxNo = list.get(0).getNumber();
                    Integer pos = maxNo.lastIndexOf("-");
                    String maxIdxStr = maxNo.substring(pos + 1, pos + 5);
                    Integer maxNoInt = Integer.valueOf(maxIdxStr);
                    String noIdxStr = String.format("%04d", maxNoInt + 1);
                    no = no + noIdxStr;
                } else {
                    no = no + "0001";
                }
                bdManualLedger.setNumber(no);
            }
        }

        if (bdManualLedger.getProcedures() == null || bdManualLedger.getProcedures().size() == 0) {
            throw new BizException("未检测到工序明细");
        }
        if (bdManualLedger.getStaffs() == null || bdManualLedger.getStaffs().size() == 0) {
            throw new BizException("未检测到员工明细");
        }

        bdManualLedger.setUpdateTime(sdf.format(new Date()));
        bdManualLedger.setUpdaterId(RequestUtils.getUserId());
        bdManualLedger.setUpdater(RequestUtils.getNickname());
        if (!this.updateById(bdManualLedger)) {
            throw new BizException("更新失败");
        }

        // 删除旧数据
        List<BdManualLedgerProcedure> oldProcedures = bdManualLedgerProcedureService.list(
                new LambdaQueryWrapper<BdManualLedgerProcedure>()
                        .eq(BdManualLedgerProcedure::getPid, bdManualLedger.getId())
        );
        if (oldProcedures != null && oldProcedures.size() > 0) {
            List<String> oldProcedureIds = oldProcedures.stream().map(BdManualLedgerProcedure::getId).collect(Collectors.toList());
            if (oldProcedureIds != null && oldProcedureIds.size() > 0) {
                if (!bdManualLedgerProcedureService.removeByIds(oldProcedureIds)) {
                    throw new BizException("工序明细更新失败，异常码1");
                }
            }
        }
        List<BdManualLedgerStaff> oldStaffs = bdManualLedgerStaffService.list(
                new LambdaQueryWrapper<BdManualLedgerStaff>()
                        .eq(BdManualLedgerStaff::getPid, bdManualLedger.getId())
        );
        if (oldStaffs != null && oldStaffs.size() > 0) {
            List<String> oldStaffIds = oldStaffs.stream().map(BdManualLedgerStaff::getId).collect(Collectors.toList());
            if (oldStaffIds != null && oldStaffIds.size() > 0) {
                if (!bdManualLedgerStaffService.removeByIds(oldStaffIds)) {
                    throw new BizException("员工明细更新失败，异常码1");
                }
            }
        }

        // 保存新数据
        List<BdManualLedgerProcedure> newProcedures = bdManualLedger.getProcedures();
        for (BdManualLedgerProcedure procedure : newProcedures) {
            procedure.setPid(bdManualLedger.getId());
            procedure.setUpdateTime(sdf.format(new Date()));
            procedure.setUpdaterId(RequestUtils.getUserId());
            procedure.setUpdater(RequestUtils.getNickname());

            if (ObjectUtils.isEmpty(procedure.getCreatorId())) {
                procedure.setCreateTime(sdf.format(new Date()));
                procedure.setCreatorId(RequestUtils.getUserId());
                procedure.setCreator(RequestUtils.getNickname());
            }
        }
        if (!bdManualLedgerProcedureService.saveOrUpdateBatch(newProcedures)) {
            throw new BizException("工序明细保存失败，异常码2");
        }

        List<BdManualLedgerStaff> newStaffs = bdManualLedger.getStaffs();
        for (BdManualLedgerStaff staff : newStaffs) {
            staff.setPid(bdManualLedger.getId());
            staff.setUpdateTime(sdf.format(new Date()));
            staff.setUpdaterId(RequestUtils.getUserId());
            staff.setUpdater(RequestUtils.getNickname());

            if (ObjectUtils.isEmpty(staff.getCreatorId())) {
                staff.setCreateTime(sdf.format(new Date()));
                staff.setCreatorId(RequestUtils.getUserId());
                staff.setCreator(RequestUtils.getNickname());
            }
        }
        if (!bdManualLedgerStaffService.saveOrUpdateBatch(newStaffs)) {
            throw new BizException("员工明细保存失败，异常码2");
        }

        return bdManualLedger;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Long id) {
        BdManualLedger bdManualLedger = this.getById(id);
        if (bdManualLedger == null) {
            return true;
        }

        if (!this.removeById(id)) {
            throw new BizException("删除失败，异常码1");
        }

        List<BdManualLedgerProcedure> procedures = bdManualLedgerProcedureService.list(
                new LambdaQueryWrapper<BdManualLedgerProcedure>()
                        .eq(BdManualLedgerProcedure::getPid, id)
        );
        if (procedures != null && procedures.size() > 0) {
            List<String> procedureIds = procedures.stream().map(BdManualLedgerProcedure::getId).collect(Collectors.toList());
            if (!bdManualLedgerProcedureService.removeByIds(procedureIds)) {
                throw new BizException("删除失败，异常码2");
            }
        }

        List<BdManualLedgerStaff> staffs = bdManualLedgerStaffService.list(
                new LambdaQueryWrapper<BdManualLedgerStaff>()
                        .eq(BdManualLedgerStaff::getPid, id)
        );
        if (staffs != null && staffs.size() > 0) {
            List<String> staffIds = staffs.stream().map(BdManualLedgerStaff::getId).collect(Collectors.toList());
            if (!bdManualLedgerStaffService.removeByIds(staffIds)) {
                throw new BizException("删除失败，异常码3");
            }
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<BdManualLedger> list = this.list(
                new LambdaQueryWrapper<BdManualLedger>()
                        .in(BdManualLedger::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<String> delIds = list.stream().map(BdManualLedger::getId).collect(Collectors.toList());
            if (!this.removeByIds(delIds)) {
                throw new BizException("删除失败，异常码1");
            }

            List<BdManualLedgerProcedure> procedures = bdManualLedgerProcedureService.list(
                    new LambdaQueryWrapper<BdManualLedgerProcedure>()
                            .in(BdManualLedgerProcedure::getPid, delIds)
            );
            if (procedures != null && procedures.size() > 0) {
                List<String> procedureIds = procedures.stream().map(BdManualLedgerProcedure::getId).collect(Collectors.toList());
                if (!bdManualLedgerProcedureService.removeByIds(procedureIds)) {
                    throw new BizException("删除失败，异常码2");
                }
            }

            List<BdManualLedgerStaff> staffs = bdManualLedgerStaffService.list(
                    new LambdaQueryWrapper<BdManualLedgerStaff>()
                            .in(BdManualLedgerStaff::getPid, delIds)
            );
            if (staffs != null && staffs.size() > 0) {
                List<String> staffIds = staffs.stream().map(BdManualLedgerStaff::getId).collect(Collectors.toList());
                if (!bdManualLedgerStaffService.removeByIds(staffIds)) {
                    throw new BizException("删除失败，异常码3");
                }
            }
        }
    }

    @Override
    public BdManualLedger detail(String id) {
        BdManualLedger result = baseMapper.infoById(id);
        if (result != null) {
            BdManualLedgerProcedure bdManualLedgerProcedure = new BdManualLedgerProcedure();
            bdManualLedgerProcedure.setPid(id);
            List<BdManualLedgerProcedure> procedures = bdManualLedgerProcedureMapper.getList(bdManualLedgerProcedure);
            result.setProcedures(procedures);

            BdManualLedgerStaff bdManualLedgerStaff = new BdManualLedgerStaff();
            bdManualLedgerStaff.setPid(id);
            List<BdManualLedgerStaff> staffs = bdManualLedgerStaffMapper.getList(bdManualLedgerStaff);
            result.setStaffs(staffs);
        }

        return result;
    }
}
