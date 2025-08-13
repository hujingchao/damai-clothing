package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.mapper.basedata.BdSpecificationMapper;
import com.fenglei.mapper.prd.PrdMoMapper;
import com.fenglei.model.basedata.BdSpecification;
import com.fenglei.model.prd.entity.PrdMo;
import com.fenglei.service.basedata.BdSpecificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdSpecificationServiceImpl extends ServiceImpl<BdSpecificationMapper, BdSpecification> implements BdSpecificationService {

    @Resource
    private PrdMoMapper prdMoMapper;

    @Override
    public IPage<BdSpecification> myPage(Page page, BdSpecification bdSpecification) {
        IPage<BdSpecification> iPage = this.page(page,
                new LambdaQueryWrapper<BdSpecification>()
                        .like(StringUtils.isNotEmpty(bdSpecification.getName()), BdSpecification::getName, bdSpecification.getName())
                        .notIn(bdSpecification.getExcludeIds() != null && bdSpecification.getExcludeIds().size() > 0, BdSpecification::getId, bdSpecification.getExcludeIds())
                        .orderByDesc(BdSpecification::getCreateTime)
        );

        return iPage;
    }

    @Override
    public List<BdSpecification> myList(BdSpecification bdSpecification) {
        List<BdSpecification> list = this.list(
                new LambdaQueryWrapper<BdSpecification>()
                        .notIn(bdSpecification.getExcludeIds() != null && bdSpecification.getExcludeIds().size() > 0, BdSpecification::getId, bdSpecification.getExcludeIds())
                        .like(StringUtils.isNotEmpty(bdSpecification.getName()), BdSpecification::getName, bdSpecification.getName())
                        .apply(StringUtils.isNotEmpty(bdSpecification.getCommFilter()), " (number like '%" + bdSpecification.getCommFilter() + "%' or name like '%" + bdSpecification.getCommFilter() + "%')")
        );

        return list;
    }

    @Override
    public BdSpecification add(BdSpecification bdSpecification) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdSpecification.getName())) {
            List<BdSpecification> list = this.list(
                    new LambdaQueryWrapper<BdSpecification>()
                            .eq(BdSpecification::getName, bdSpecification.getName())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同的标签");
            }
        } else {
            throw new BizException("请填写标签名");
        }

        bdSpecification.setCreateTime(sdf.format(new Date()));
        bdSpecification.setCreatorId(RequestUtils.getUserId());
        bdSpecification.setCreator(RequestUtils.getNickname());
        if (!this.save(bdSpecification)) {
            throw new BizException("保存失败");
        }

        return bdSpecification;
    }

    @Override
    public BdSpecification myUpdate(BdSpecification bdSpecification) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdSpecification.getName())) {
            List<BdSpecification> list = this.list(
                    new LambdaQueryWrapper<BdSpecification>()
                            .eq(BdSpecification::getName, bdSpecification.getName())
                            .ne(BdSpecification::getId, bdSpecification.getId())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同的标签");
            }
        } else {
            throw new BizException("请填写标签名");
        }

        bdSpecification.setUpdateTime(sdf.format(new Date()));
        bdSpecification.setUpdaterId(RequestUtils.getUserId());
        bdSpecification.setUpdater(RequestUtils.getNickname());
        if (!this.updateById(bdSpecification)) {
            throw new BizException("修改失败");
        }

        return bdSpecification;
    }

    @Override
    public Boolean deleteById(Long id) {
        // 删除校验
        BdSpecification tag = this.getById(id);
        if (tag == null) {
            return true;
        }

        List<PrdMo> prdMos = prdMoMapper.selectList(
                new LambdaQueryWrapper<PrdMo>()
                        .eq(PrdMo::getTags, tag.getId())
                        .or()
                        .apply("tags like '" + tag.getId() + ",%'")
                        .or()
                        .apply("tags like '%," + tag.getId() + ",%'")
                        .or()
                        .apply("tags like '%," + tag.getId() + "'")
        );
        if (prdMos != null && prdMos.size() > 0) {
            List<String> moNos = prdMos.stream().map(PrdMo::getNo).collect(Collectors.toList());
            throw new BizException("标签 " + tag.getName() + " 已被生产订单 " + String.join(",", moNos) + " 使用");
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
        List<BdSpecification> list = this.list(
                new LambdaQueryWrapper<BdSpecification>()
                        .in(BdSpecification::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<String> delIds = new ArrayList<>();

            // 删除校验
            for (BdSpecification tag : list) {
                delIds.add(tag.getId());

                List<PrdMo> prdMos = prdMoMapper.selectList(
                        new LambdaQueryWrapper<PrdMo>()
                                .eq(PrdMo::getTags, tag.getId())
                                .or()
                                .apply("tags like '" + tag.getId() + ",%'")
                                .or()
                                .apply("tags like '%," + tag.getId() + ",%'")
                                .or()
                                .apply("tags like '%," + tag.getId() + "'")
                );
                if (prdMos != null && prdMos.size() > 0) {
                    List<String> moNos = prdMos.stream().map(PrdMo::getNo).collect(Collectors.toList());
                    throw new BizException("标签 " + tag.getName() + " 已被生产订单 " + String.join(",", moNos) + " 使用");
                }
            }

            if (!this.removeByIds(delIds)) {
                throw new BizException("删除失败");
            }
        }
    }

    @Override
    public BdSpecification detail(String id) {
        BdSpecification result = this.getById(id);
        return result;
    }
}
