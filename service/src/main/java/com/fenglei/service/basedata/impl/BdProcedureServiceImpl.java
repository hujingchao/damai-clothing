package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.mapper.basedata.BdProcedureMapper;
import com.fenglei.model.basedata.*;
import com.fenglei.service.basedata.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class BdProcedureServiceImpl extends ServiceImpl<BdProcedureMapper, BdProcedure> implements BdProcedureService {

    @Resource
    private BdWagesItemService bdWagesItemService;
    @Resource
    private BdWagesService bdWagesService;
    @Resource
    private BdTempWagesItemService bdTempWagesItemService;
    @Resource
    private BdTempWagesService bdTempWagesService;
    @Resource
    private BdManualLedgerProcedureService bdManualLedgerProcedureService;
    @Resource
    private BdManualLedgerService bdManualLedgerService;

    @Override
    public IPage<BdProcedure> myPage(Page page, BdProcedure bdProcedure) {
        IPage<BdProcedure> iPage = this.page(page,
                new LambdaQueryWrapper<BdProcedure>()
                        .like(StringUtils.isNotEmpty(bdProcedure.getNumber()), BdProcedure::getNumber, bdProcedure.getNumber())
                        .like(StringUtils.isNotEmpty(bdProcedure.getName()), BdProcedure::getName, bdProcedure.getName())
                        .notIn(bdProcedure.getExcludeIds() != null && bdProcedure.getExcludeIds().size() > 0, BdProcedure::getId, bdProcedure.getExcludeIds())
                        .orderByDesc(BdProcedure::getCreateTime)
        );

        return iPage;
    }

    @Override
    public List<BdProcedure> myList(BdProcedure bdProcedure) {
        List<BdProcedure> list = this.list(
                new LambdaQueryWrapper<BdProcedure>()
                        .notIn(bdProcedure.getExcludeIds() != null && bdProcedure.getExcludeIds().size() > 0, BdProcedure::getId, bdProcedure.getExcludeIds())
                        .like(StringUtils.isNotEmpty(bdProcedure.getNumber()), BdProcedure::getNumber, bdProcedure.getNumber())
                        .like(StringUtils.isNotEmpty(bdProcedure.getName()), BdProcedure::getName, bdProcedure.getName())
                        .apply(StringUtils.isNotEmpty(bdProcedure.getCommFilter()), " (number like '%" + bdProcedure.getCommFilter() + "%' or name like '%" + bdProcedure.getCommFilter() + "%')")
        );

        return list;
    }

    @Override
    public BdProcedure add(BdProcedure bdProcedure) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdProcedure.getNumber())) {
            List<BdProcedure> list = this.list(
                    new LambdaQueryWrapper<BdProcedure>()
                            .eq(BdProcedure::getNumber, bdProcedure.getNumber())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同编码的工序");
            }
        } else {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String no = "GX" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            List<BdProcedure> list = this.list(
                    new LambdaQueryWrapper<BdProcedure>()
                            .likeRight(BdProcedure::getNumber, no)
                            .orderByDesc(BdProcedure::getNumber)
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
            bdProcedure.setNumber(no);
        }

        bdProcedure.setCreateTime(sdf.format(new Date()));
        bdProcedure.setCreatorId(RequestUtils.getUserId());
        bdProcedure.setCreator(RequestUtils.getNickname());
        if (!this.save(bdProcedure)) {
            throw new BizException("保存失败");
        }

        return bdProcedure;
    }

    @Override
    public BdProcedure myUpdate(BdProcedure bdProcedure) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdProcedure.getNumber())) {
            List<BdProcedure> list = this.list(
                    new LambdaQueryWrapper<BdProcedure>()
                            .eq(BdProcedure::getNumber, bdProcedure.getNumber())
                            .ne(BdProcedure::getId, bdProcedure.getId())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同编码的工序，请修改，异常码1");
            }
        } else {
            BdProcedure thisProcedure = this.getById(bdProcedure.getId());
            if (thisProcedure != null) {
                bdProcedure.setNumber(thisProcedure.getNumber());

                List<BdProcedure> list = this.list(
                        new LambdaQueryWrapper<BdProcedure>()
                                .eq(BdProcedure::getNumber, bdProcedure.getNumber())
                                .ne(BdProcedure::getId, bdProcedure.getId())
                );
                if (list != null && list.size() > 0) {
                    throw new BizException("已存在相同编码的工序，请修改，异常码2");
                }
            } else {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String no = "GX" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
                List<BdProcedure> list = this.list(
                        new LambdaQueryWrapper<BdProcedure>()
                                .likeRight(BdProcedure::getNumber, no)
                                .orderByDesc(BdProcedure::getNumber)
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
                bdProcedure.setNumber(no);
            }
        }

        bdProcedure.setUpdateTime(sdf.format(new Date()));
        bdProcedure.setUpdaterId(RequestUtils.getUserId());
        bdProcedure.setUpdater(RequestUtils.getNickname());
        if (!this.updateById(bdProcedure)) {
            throw new BizException("修改失败");
        }

        return bdProcedure;
    }

    Boolean chkIsUsed(List<String> ids) {
        // 工价模板
        List<BdWagesItem> bdWagesItems = bdWagesItemService.list(
                new LambdaQueryWrapper<BdWagesItem>()
                        .in(BdWagesItem::getProcedureId, ids)
        );
        if (bdWagesItems != null && bdWagesItems.size() > 0) {
            List<String> pids = bdWagesItems.stream().map(BdWagesItem::getPid).distinct().collect(Collectors.toList());
            List<BdWages> bdWages = bdWagesService.list(
                    new LambdaQueryWrapper<BdWages>()
                            .in(BdWages::getId, pids)
            );
            if (bdWages != null && bdWages.size() > 0) {
                List<String> strs = new ArrayList<>();
                for (BdWages bdWage : bdWages) {
                    String str = " " + bdWage.getNumber() + "  "  + bdWage.getName() + " ";
                    if (!strs.contains(str)) {
                        strs.add(str);
                    }
                }
                throw new BizException("当前工序已被工价档案：" + StringUtils.join(strs, ",") + "引用，无法删除");
            }
        }

        // 工价模板-临时
        List<BdTempWagesItem> bdTempWagesItems = bdTempWagesItemService.list(
                new LambdaQueryWrapper<BdTempWagesItem>()
                        .in(BdTempWagesItem::getProcedureId, ids)
        );
        if (bdTempWagesItems != null && bdTempWagesItems.size() > 0) {
            List<String> pids = bdTempWagesItems.stream().map(BdTempWagesItem::getPid).distinct().collect(Collectors.toList());
            List<BdTempWages> bdTempWages = bdTempWagesService.list(
                    new LambdaQueryWrapper<BdTempWages>()
                            .in(BdTempWages::getId, pids)
            );
            if (bdTempWages != null && bdTempWages.size() > 0) {
                List<String> strs = new ArrayList<>();
                for (BdTempWages bdWage : bdTempWages) {
                    String str = " " + bdWage.getNumber() + "  "  + bdWage.getName() + " ";
                    if (!strs.contains(str)) {
                        strs.add(str);
                    }
                }
                throw new BizException("当前工序已被工价档案：" + StringUtils.join(strs, ",") + "引用，无法删除");
            }
        }

        // 手工帐
        List<BdManualLedgerProcedure> bdManualLedgerProcedures = bdManualLedgerProcedureService.list(
                new LambdaQueryWrapper<BdManualLedgerProcedure>()
                        .eq(BdManualLedgerProcedure::getProcedureId, ids)
        );
        if (bdManualLedgerProcedures != null && bdManualLedgerProcedures.size() > 0) {
            List<String> pids = bdManualLedgerProcedures.stream().map(BdManualLedgerProcedure::getPid).distinct().collect(Collectors.toList());
            List<BdManualLedger> bdManualLedgers = bdManualLedgerService.list(
                    new LambdaQueryWrapper<BdManualLedger>()
                            .in(BdManualLedger::getId, pids)
            );
            if (bdManualLedgers != null && bdManualLedgers.size() > 0) {
                List<String> strs = new ArrayList<>();
                for (BdManualLedger bdManualLedger : bdManualLedgers) {
                    String str = " " + bdManualLedger.getNumber() + "  "  + bdManualLedger.getName() + " ";
                    if (!strs.contains(str)) {
                        strs.add(str);
                    }
                }
                throw new BizException("当前工序已被手工帐档案：" + StringUtils.join(strs, ",") + "引用，无法删除");
            }
        }

        // 生产订单


        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Long id) {
        BdProcedure bdProcedure = this.getById(id);
        if (bdProcedure == null) {
            return true;
        }

        List<String> ids = new ArrayList<>();
        ids.add(id.toString());
        if (this.chkIsUsed(ids)) {
            throw new BizException("工序已被引用");
        }

        if (!this.removeById(id)) {
            throw new BizException("删除失败");
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<BdProcedure> list = this.list(
                new LambdaQueryWrapper<BdProcedure>()
                        .in(BdProcedure::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<String> delIds = list.stream().map(BdProcedure::getId).collect(Collectors.toList());
            if (this.chkIsUsed(delIds)) {
                throw new BizException("工序已被引用");
            }

            if (!this.removeByIds(delIds)) {
                throw new BizException("删除失败");
            }
        }
    }

    @Override
    public BdProcedure detail(String id) {
        BdProcedure result = this.getById(id);
        return result;
    }
}
