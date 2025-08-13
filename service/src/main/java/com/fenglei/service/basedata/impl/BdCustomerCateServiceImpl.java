package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.StringUtils;
import com.fenglei.mapper.basedata.BdCustomerCateMapper;
import com.fenglei.mapper.basedata.BdCustomerMapper;
import com.fenglei.model.basedata.BdCustomer;
import com.fenglei.model.basedata.BdCustomerCate;
import com.fenglei.service.basedata.BdCustomerCateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * BdCustomerCateService实现类
 * Created by ljw on 2022/7/12.
 */
@Service
public class BdCustomerCateServiceImpl extends ServiceImpl<BdCustomerCateMapper, BdCustomerCate> implements BdCustomerCateService {

    @Resource
    private BdCustomerMapper bdCustomerMapper;

    @Override
    public IPage<BdCustomerCate> myPage(BdCustomerCate bdCustomerCate, Page page) {
        IPage<BdCustomerCate> iPage = this.page(page,
                new LambdaQueryWrapper<BdCustomerCate>()
                        .like(StringUtils.isNotEmpty(bdCustomerCate.getName()), BdCustomerCate::getName, bdCustomerCate.getName())
        );

        return iPage;
    }

    @Override
    public List<BdCustomerCate> myList(BdCustomerCate bdCustomerCate) {
        List<BdCustomerCate> list = this.list(
                new LambdaQueryWrapper<BdCustomerCate>()
                        .like(StringUtils.isNotEmpty(bdCustomerCate.getName()), BdCustomerCate::getName, bdCustomerCate.getName())
        );

        return list;
    }

    @Override
    @Transactional
    public BdCustomerCate add(BdCustomerCate bdCustomerCate) {
        if (StringUtils.isEmpty(bdCustomerCate.getName())) {
            throw new BizException("请填写分类名称");
        } else {
            List<BdCustomerCate> list = this.list(
                    new LambdaQueryWrapper<BdCustomerCate>()
                            .eq(BdCustomerCate::getName, bdCustomerCate.getName())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("分类名称已存在，无法重复添加");
            }
        }

        if (!this.save(bdCustomerCate)) {
            throw new BizException("保存失败");
        }

        return bdCustomerCate;
    }

    @Override
    @Transactional
    public BdCustomerCate myUpdate(BdCustomerCate bdCustomerCate) {
        if (StringUtils.isEmpty(bdCustomerCate.getName())) {
            throw new BizException("请填写分类名称");
        } else {
            List<BdCustomerCate> list = this.list(
                    new LambdaQueryWrapper<BdCustomerCate>()
                            .eq(BdCustomerCate::getName, bdCustomerCate.getName())
                            .ne(BdCustomerCate::getId, bdCustomerCate.getId())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("分类名称已存在，无法重复添加");
            }
        }

        if (!this.updateById(bdCustomerCate)) {
            throw new BizException("更新失败");
        }

        return bdCustomerCate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteById(Long id) {
        BdCustomerCate bdCustomerCate = this.getById(id);
        if (bdCustomerCate == null) {
            return true;
        }

        List<BdCustomer> list = bdCustomerMapper.selectList(
                new LambdaQueryWrapper<BdCustomer>()
                        .eq(BdCustomer::getCustomerCateId, id)
        );
        if (list != null && list.size() > 0) {
            throw new BizException("该分类下存在客户，无法删除");
        }

        if (!this.removeById(id)) {
            throw new BizException("删除失败，异常码1");
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(String[] ids) {
        List<String> idList = Arrays.asList(ids);

        List<BdCustomer> list = bdCustomerMapper.selectList(
                new LambdaQueryWrapper<BdCustomer>()
                        .in(BdCustomer::getCustomerCateId, idList)
        );
        if (list != null && list.size() > 0) {
            throw new BizException("该分类下存在客户，无法删除");
        }

        if (!this.removeByIds(idList)) {
            throw new BizException("删除失败");
        }
    }

    @Override
    public BdCustomerCate detail(String id) {
        BdCustomerCate bdCustomerCate = this.getById(id);

        return bdCustomerCate;
    }
}
