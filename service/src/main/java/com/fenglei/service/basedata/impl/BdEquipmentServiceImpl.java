package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.common.util.StringUtils;
import com.fenglei.mapper.basedata.BdEquipmentMapper;
import com.fenglei.model.basedata.BdEquipment;
import com.fenglei.model.prd.entity.PrdCutting;
import com.fenglei.service.basedata.IBdEquipmentService;
import com.fenglei.service.prd.IPrdCuttingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 设备 服务实现类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-09
 */
@Service
public class BdEquipmentServiceImpl extends ServiceImpl<BdEquipmentMapper, BdEquipment> implements IBdEquipmentService {

    @Autowired
    IPrdCuttingService cuttingService;

    /**
     * 新增
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BdEquipment add(BdEquipment bdEquipment) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdEquipment.getNumber())) {
            List<BdEquipment> list = this.list(
                    new LambdaQueryWrapper<BdEquipment>()
                            .eq(BdEquipment::getNumber, bdEquipment.getNumber())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同编码的设备，请修改");
            }
        } else {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String no = "SB" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            List<BdEquipment> list = this.list(
                    new LambdaQueryWrapper<BdEquipment>()
                            .likeRight(BdEquipment::getNumber, no)
                            .orderByDesc(BdEquipment::getNumber)
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
            bdEquipment.setNumber(no);
        }

        bdEquipment.setCreateTime(sdf.format(new Date()));
        bdEquipment.setCreatorId(RequestUtils.getUserId());
        bdEquipment.setCreator(RequestUtils.getNickname());

        if (!this.save(bdEquipment)) {
            throw new BizException("保存失败");
        }

        return bdEquipment;
    }

    /**
     * 删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveById(String id) {
        int count = cuttingService.count(Wrappers.lambdaQuery(PrdCutting.class).eq(PrdCutting::getEquipmentId, id));
        if (count > 0) {
            throw new BizException("该设备已被使用，无法删除！");
        }
        return this.removeById(id);
    }

    /**
     * 批量删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean myRemoveByIds(List<String> ids) {
        int count = cuttingService.count(Wrappers.lambdaQuery(PrdCutting.class).in(PrdCutting::getEquipmentId, ids));
        if (count > 0) {
            throw new BizException("设备已被使用，无法删除！");
        }
        return this.removeByIds(ids);
    }

    /**
     * 更新
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BdEquipment myUpdate(BdEquipment bdEquipment) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdEquipment.getNumber())) {
            List<BdEquipment> list = this.list(
                    new LambdaQueryWrapper<BdEquipment>()
                            .eq(BdEquipment::getNumber, bdEquipment.getNumber())
                            .ne(BdEquipment::getId, bdEquipment.getId())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同编码的设备，请修改，异常码1");
            }
        } else {
            BdEquipment thisEquip = this.getById(bdEquipment.getId());
            if (thisEquip != null) {
                bdEquipment.setNumber(thisEquip.getNumber());

                List<BdEquipment> list = this.list(
                        new LambdaQueryWrapper<BdEquipment>()
                                .eq(BdEquipment::getNumber, bdEquipment.getNumber())
                                .ne(BdEquipment::getId, bdEquipment.getId())
                );
                if (list != null && list.size() > 0) {
                    throw new BizException("已存在相同编码的设备，请修改，异常码2");
                }
            } else {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String no = "SB" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
                List<BdEquipment> list = this.list(
                        new LambdaQueryWrapper<BdEquipment>()
                                .likeRight(BdEquipment::getNumber, no)
                                .orderByDesc(BdEquipment::getNumber)
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
                bdEquipment.setNumber(no);
            }
        }

        bdEquipment.setUpdateTime(sdf.format(new Date()));
        bdEquipment.setUpdaterId(RequestUtils.getUserId());
        bdEquipment.setUpdater(RequestUtils.getNickname());

        this.updateById(bdEquipment);
        return bdEquipment;
    }

    /**
     * 分页查询
     */
    @Override
    public IPage<BdEquipment> myPage(Page page, BdEquipment bdEquipment) {
        return this.page(page, Wrappers.lambdaQuery(BdEquipment.class)
                .like(StringUtils.isNotEmpty(bdEquipment.getName()), BdEquipment::getName, bdEquipment.getName())
                .like(StringUtils.isNotEmpty(bdEquipment.getNumber()), BdEquipment::getNumber, bdEquipment.getNumber())
        );
    }

    @Override
    public List<BdEquipment> myList(BdEquipment bdEquipment) {
        return this.list(Wrappers.lambdaQuery(BdEquipment.class)
                .like(StringUtils.isNotEmpty(bdEquipment.getName()), BdEquipment::getName, bdEquipment.getName())
                .like(StringUtils.isNotEmpty(bdEquipment.getNumber()), BdEquipment::getNumber, bdEquipment.getNumber())
        );
    }

    /**
     * 详情
     */
    @Override
    public BdEquipment detail(String id) {
        return this.getById(id);
    }
}
