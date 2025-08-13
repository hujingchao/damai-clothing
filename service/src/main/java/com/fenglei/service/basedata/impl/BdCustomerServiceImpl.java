package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.mapper.basedata.BdCustomerMapper;
import com.fenglei.model.basedata.BdCustomer;
import com.fenglei.service.basedata.BdCustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdCustomerServiceImpl extends ServiceImpl<BdCustomerMapper, BdCustomer> implements BdCustomerService {

    @Override
    public IPage<BdCustomer> myPage(Page page, BdCustomer bdCustomer) {
        IPage<BdCustomer> iPage = baseMapper.getPage(page, bdCustomer);

        return iPage;
    }

    @Override
    public List<BdCustomer> myList(BdCustomer bdCustomer) {
        List<BdCustomer> list = baseMapper.getList(bdCustomer);

        return list;
    }

    @Override
    public BdCustomer add(BdCustomer bdCustomer) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdCustomer.getNumber())) {
            List<BdCustomer> list = this.list(
                    new LambdaQueryWrapper<BdCustomer>()
                            .eq(BdCustomer::getNumber, bdCustomer.getNumber())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同编码的客户");
            }
        } else {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            int day = cal.get(Calendar.DAY_OF_MONTH);
            String no = "KH" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
            List<BdCustomer> list = this.list(
                    new LambdaQueryWrapper<BdCustomer>()
                            .likeRight(BdCustomer::getNumber, no)
                            .orderByDesc(BdCustomer::getNumber)
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
            bdCustomer.setNumber(no);
        }

        bdCustomer.setCreateTime(sdf.format(new Date()));
        bdCustomer.setCreatorId(RequestUtils.getUserId());
        bdCustomer.setCreator(RequestUtils.getNickname());
        if (!this.save(bdCustomer)) {
            throw new BizException("保存失败");
        }

        return bdCustomer;
    }

    @Override
    public BdCustomer myUpdate(BdCustomer bdCustomer) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdCustomer.getNumber())) {
            List<BdCustomer> list = this.list(
                    new LambdaQueryWrapper<BdCustomer>()
                            .eq(BdCustomer::getNumber, bdCustomer.getNumber())
                            .ne(BdCustomer::getId, bdCustomer.getId())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同编码的客户，请修改，异常码1");
            }
        } else {
            BdCustomer thisCust = this.getById(bdCustomer.getId());
            if (thisCust != null) {
                bdCustomer.setNumber(thisCust.getNumber());

                List<BdCustomer> list = this.list(
                        new LambdaQueryWrapper<BdCustomer>()
                                .eq(BdCustomer::getNumber, bdCustomer.getNumber())
                                .ne(BdCustomer::getId, bdCustomer.getId())
                );
                if (list != null && list.size() > 0) {
                    throw new BizException("已存在相同编码的客户，请修改，异常码2");
                }
            } else {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String no = "KH" + year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" + day : day) + "-";
                List<BdCustomer> list = this.list(
                        new LambdaQueryWrapper<BdCustomer>()
                                .likeRight(BdCustomer::getNumber, no)
                                .orderByDesc(BdCustomer::getNumber)
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
                bdCustomer.setNumber(no);
            }
        }

        bdCustomer.setUpdateTime(sdf.format(new Date()));
        bdCustomer.setUpdaterId(RequestUtils.getUserId());
        bdCustomer.setUpdater(RequestUtils.getNickname());
        if (!this.updateById(bdCustomer)) {
            throw new BizException("修改失败");
        }

        return bdCustomer;
    }

    @Override
    public Boolean deleteById(Long id) {
        BdCustomer supplier = this.getById(id);
        if (supplier != null) {
            if (!this.removeById(id)) {
                throw new BizException("删除失败");
            }
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<BdCustomer> list = this.list(
                new LambdaQueryWrapper<BdCustomer>()
                        .in(BdCustomer::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<String> delIds = list.stream().map(BdCustomer::getId).collect(Collectors.toList());
            if (!this.removeByIds(delIds)) {
                throw new BizException("删除失败");
            }
        }
    }

    @Override
    public BdCustomer detail(String id) {
        BdCustomer result = baseMapper.infoById(id);
        return result;
    }
}
