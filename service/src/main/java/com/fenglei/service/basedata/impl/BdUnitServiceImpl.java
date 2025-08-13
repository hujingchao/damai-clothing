package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.mapper.basedata.BdUnitMapper;
import com.fenglei.model.basedata.BdMaterial;
import com.fenglei.model.basedata.BdTempMaterial;
import com.fenglei.model.basedata.BdUnit;
import com.fenglei.service.basedata.BdMaterialService;
import com.fenglei.service.basedata.BdTempMaterialService;
import com.fenglei.service.basedata.BdUnitService;
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
public class BdUnitServiceImpl extends ServiceImpl<BdUnitMapper, BdUnit> implements BdUnitService {

    @Resource
    private BdMaterialService bdMaterialService;
    @Resource
    private BdTempMaterialService bdTempMaterialService;

    @Override
    public IPage<BdUnit> myPage(Page page, BdUnit bdUnit) {
        IPage<BdUnit> iPage = this.page(page,
                new LambdaQueryWrapper<BdUnit>()
                        .like(StringUtils.isNotEmpty(bdUnit.getNumber()), BdUnit::getNumber, bdUnit.getNumber())
                        .like(StringUtils.isNotEmpty(bdUnit.getName()), BdUnit::getName, bdUnit.getName())
                        .orderByDesc(BdUnit::getCreateTime)
        );

        return iPage;
    }

    @Override
    public List<BdUnit> myList(BdUnit bdUnit) {
        List<BdUnit> list = this.list(
                new LambdaQueryWrapper<BdUnit>()
                        .like(StringUtils.isNotEmpty(bdUnit.getNumber()), BdUnit::getNumber, bdUnit.getNumber())
                        .like(StringUtils.isNotEmpty(bdUnit.getName()), BdUnit::getName, bdUnit.getName())
        );

        return list;
    }

    @Override
    public BdUnit add(BdUnit bdUnit) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdUnit.getNumber())) {
            List<BdUnit> list = this.list(
                    new LambdaQueryWrapper<BdUnit>()
                            .eq(BdUnit::getNumber, bdUnit.getNumber())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同编码的工序");
            }
        } else {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String no = "DW" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            List<BdUnit> list = this.list(
                    new LambdaQueryWrapper<BdUnit>()
                            .likeRight(BdUnit::getNumber, no)
                            .orderByDesc(BdUnit::getNumber)
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
            bdUnit.setNumber(no);
        }

        bdUnit.setCreateTime(sdf.format(new Date()));
        bdUnit.setCreatorId(RequestUtils.getUserId());
        bdUnit.setCreator(RequestUtils.getNickname());
        if (!this.save(bdUnit)) {
            throw new BizException("保存失败");
        }

        return bdUnit;
    }

    @Override
    public BdUnit myUpdate(BdUnit bdUnit) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdUnit.getNumber())) {
            List<BdUnit> list = this.list(
                    new LambdaQueryWrapper<BdUnit>()
                            .eq(BdUnit::getNumber, bdUnit.getNumber())
                            .ne(BdUnit::getId, bdUnit.getId())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同编码的工序，请修改，异常码1");
            }
        } else {
            BdUnit thisUnit = this.getById(bdUnit.getId());
            if (thisUnit != null) {
                bdUnit.setNumber(thisUnit.getNumber());

                List<BdUnit> list = this.list(
                        new LambdaQueryWrapper<BdUnit>()
                                .eq(BdUnit::getNumber, bdUnit.getNumber())
                                .ne(BdUnit::getId, bdUnit.getId())
                );
                if (list != null && list.size() > 0) {
                    throw new BizException("已存在相同编码的工序，请修改，异常码2");
                }
            } else {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String no = "DW" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
                List<BdUnit> list = this.list(
                        new LambdaQueryWrapper<BdUnit>()
                                .likeRight(BdUnit::getNumber, no)
                                .orderByDesc(BdUnit::getNumber)
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
                bdUnit.setNumber(no);
            }
        }

        bdUnit.setUpdateTime(sdf.format(new Date()));
        bdUnit.setUpdaterId(RequestUtils.getUserId());
        bdUnit.setUpdater(RequestUtils.getNickname());
        if (!this.updateById(bdUnit)) {
            throw new BizException("修改失败");
        }

        return bdUnit;
    }

    Boolean chkIsUsed(List<String> ids) {
        List<BdMaterial> materials = bdMaterialService.list(
                new LambdaQueryWrapper<BdMaterial>()
                        .in(BdMaterial::getUnitId, ids)
        );
        if (materials != null && materials.size() > 0) {
            throw new BizException("当前单位已被 成品/辅料/原材料 引用，无法删除1");
        }

        List<BdTempMaterial> tempMaterials = bdTempMaterialService.list(
                new LambdaQueryWrapper<BdTempMaterial>()
                        .in(BdTempMaterial::getUnitId, ids)
        );
        if (tempMaterials != null && tempMaterials.size() > 0) {
            throw new BizException("当前单位已被 成品/辅料/原材料 引用，无法删除2");
        }

        return false;
    }

    @Override
    public Boolean deleteById(Long id) {
        BdUnit bdUnit = this.getById(id);
        if (bdUnit == null) {
            return true;
        }

        List<String> ids = new ArrayList<>();
        ids.add(id.toString());
        if (this.chkIsUsed(ids)) {
            throw new BizException("单位已被引用");
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
        List<BdUnit> list = this.list(
                new LambdaQueryWrapper<BdUnit>()
                        .in(BdUnit::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<String> delIds = list.stream().map(BdUnit::getId).collect(Collectors.toList());
            if (this.chkIsUsed(delIds)) {
                throw new BizException("单位已被引用");
            }

            if (!this.removeByIds(delIds)) {
                throw new BizException("删除失败");
            }
        }
    }

    @Override
    public BdUnit detail(String id) {
        BdUnit result = this.getById(id);
        return result;
    }
}
