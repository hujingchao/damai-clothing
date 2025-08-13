package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.mapper.basedata.BdSupplierMapper;
import com.fenglei.model.basedata.BdMaterialSupplier;
import com.fenglei.model.basedata.BdSupplier;
import com.fenglei.model.basedata.BdTempMaterialSupplier;
import com.fenglei.service.basedata.BdMaterialSupplierService;
import com.fenglei.service.basedata.BdSupplierService;
import com.fenglei.service.basedata.BdTempMaterialSupplierService;
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
public class BdSupplierServiceImpl extends ServiceImpl<BdSupplierMapper, BdSupplier> implements BdSupplierService {

    @Resource
    private BdMaterialSupplierService bdMaterialSupplierService;
    @Resource
    private BdTempMaterialSupplierService bdTempMaterialSupplierService;

    @Override
    public IPage<BdSupplier> myPage(Page page, BdSupplier bdSupplier) {
        IPage<BdSupplier> iPage = this.page(page,
                new LambdaQueryWrapper<BdSupplier>()
                        .like(StringUtils.isNotEmpty(bdSupplier.getNumber()), BdSupplier::getNumber, bdSupplier.getNumber())
                        .like(StringUtils.isNotEmpty(bdSupplier.getName()), BdSupplier::getName, bdSupplier.getName())
                        .notIn(bdSupplier.getExcludeIds() != null && bdSupplier.getExcludeIds().size() > 0, BdSupplier::getId, bdSupplier.getExcludeIds())
                        .orderByDesc(BdSupplier::getCreateTime)
        );

        return iPage;
    }

    @Override
    public List<BdSupplier> myList(BdSupplier bdSupplier) {
        List<BdSupplier> list = this.list(
                new LambdaQueryWrapper<BdSupplier>()
                        .notIn(bdSupplier.getExcludeIds() != null && bdSupplier.getExcludeIds().size() > 0, BdSupplier::getId, bdSupplier.getExcludeIds())
                        .like(StringUtils.isNotEmpty(bdSupplier.getNumber()), BdSupplier::getNumber, bdSupplier.getNumber())
                        .like(StringUtils.isNotEmpty(bdSupplier.getName()), BdSupplier::getName, bdSupplier.getName())
                        .apply(StringUtils.isNotEmpty(bdSupplier.getCommFilter()), " (number like '%" + bdSupplier.getCommFilter() + "%' or name like '%" + bdSupplier.getCommFilter() + "%')")
        );

        return list;
    }

    @Override
    public BdSupplier add(BdSupplier bdSupplier) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdSupplier.getNumber())) {
            List<BdSupplier> list = this.list(
                    new LambdaQueryWrapper<BdSupplier>()
                            .eq(BdSupplier::getNumber, bdSupplier.getNumber())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同编码的供应商");
            }
        } else {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String no = "GYS" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            List<BdSupplier> list = this.list(
                    new LambdaQueryWrapper<BdSupplier>()
                            .likeRight(BdSupplier::getNumber, no)
                            .orderByDesc(BdSupplier::getNumber)
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
            bdSupplier.setNumber(no);
        }

        bdSupplier.setCreateTime(sdf.format(new Date()));
        bdSupplier.setCreatorId(RequestUtils.getUserId());
        bdSupplier.setCreator(RequestUtils.getNickname());
        if (!this.save(bdSupplier)) {
            throw new BizException("保存失败");
        }

        return bdSupplier;
    }

    @Override
    public BdSupplier myUpdate(BdSupplier bdSupplier) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdSupplier.getNumber())) {
            List<BdSupplier> list = this.list(
                    new LambdaQueryWrapper<BdSupplier>()
                            .eq(BdSupplier::getNumber, bdSupplier.getNumber())
                            .ne(BdSupplier::getId, bdSupplier.getId())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同编码的供应商，请修改，异常码1");
            }
        } else {
            BdSupplier thisSupp = this.getById(bdSupplier.getId());
            if (thisSupp != null) {
                bdSupplier.setNumber(thisSupp.getNumber());

                List<BdSupplier> list = this.list(
                        new LambdaQueryWrapper<BdSupplier>()
                                .eq(BdSupplier::getNumber, bdSupplier.getNumber())
                                .ne(BdSupplier::getId, bdSupplier.getId())
                );
                if (list != null && list.size() > 0) {
                    throw new BizException("已存在相同编码的供应商，请修改，异常码2");
                }
            } else {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String no = "GYS" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
                List<BdSupplier> list = this.list(
                        new LambdaQueryWrapper<BdSupplier>()
                                .likeRight(BdSupplier::getNumber, no)
                                .orderByDesc(BdSupplier::getNumber)
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
                bdSupplier.setNumber(no);
            }
        }

        bdSupplier.setUpdateTime(sdf.format(new Date()));
        bdSupplier.setUpdaterId(RequestUtils.getUserId());
        bdSupplier.setUpdater(RequestUtils.getNickname());
        if (!this.updateById(bdSupplier)) {
            throw new BizException("修改失败");
        }

        return bdSupplier;
    }

    Boolean chkIsUsed(List<String> ids) {
        List<BdMaterialSupplier> materialSuppliers = bdMaterialSupplierService.list(
                new LambdaQueryWrapper<BdMaterialSupplier>()
                        .in(BdMaterialSupplier::getPid, ids)
        );
        if (materialSuppliers != null && materialSuppliers.size() > 0) {
            throw new BizException("当前供应商已被 成品/辅料/原材料 引用，无法删除1");
        }

        List<BdTempMaterialSupplier> tempMaterialSuppliers = bdTempMaterialSupplierService.list(
                new LambdaQueryWrapper<BdTempMaterialSupplier>()
                        .in(BdTempMaterialSupplier::getPid, ids)
        );
        if (tempMaterialSuppliers != null && tempMaterialSuppliers.size() > 0) {
            throw new BizException("当前供应商已被 成品/辅料/原材料 引用，无法删除2");
        }

        return false;
    }

    @Override
    public Boolean deleteById(Long id) {
        BdSupplier supplier = this.getById(id);
        if (supplier == null) {
            return true;
        }

        List<String> ids = new ArrayList<>();
        ids.add(id.toString());
        if (this.chkIsUsed(ids)) {
            throw new BizException("存货已被引用");
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
        List<BdSupplier> list = this.list(
                new LambdaQueryWrapper<BdSupplier>()
                        .in(BdSupplier::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<String> delIds = list.stream().map(BdSupplier::getId).collect(Collectors.toList());
            if (this.chkIsUsed(delIds)) {
                throw new BizException("存货已被引用");
            }

            if (!this.removeByIds(delIds)) {
                throw new BizException("删除失败");
            }
        }
    }

    @Override
    public BdSupplier detail(String id) {
        BdSupplier result = this.getById(id);
        return result;
    }
}
