package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.mapper.basedata.BdTagMapper;
import com.fenglei.mapper.prd.PrdMoMapper;
import com.fenglei.model.basedata.*;
import com.fenglei.model.prd.entity.PrdMo;
import com.fenglei.service.basedata.*;
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
public class BdTagServiceImpl extends ServiceImpl<BdTagMapper, BdTag> implements BdTagService {

    @Resource
    private PrdMoMapper prdMoMapper;

    @Override
    public IPage<BdTag> myPage(Page page, BdTag bdTag) {
        IPage<BdTag> iPage = this.page(page,
                new LambdaQueryWrapper<BdTag>()
                        .like(StringUtils.isNotEmpty(bdTag.getName()), BdTag::getName, bdTag.getName())
                        .notIn(bdTag.getExcludeIds() != null && bdTag.getExcludeIds().size() > 0, BdTag::getId, bdTag.getExcludeIds())
                        .orderByDesc(BdTag::getCreateTime)
        );

        return iPage;
    }

    @Override
    public List<BdTag> myList(BdTag bdTag) {
        List<BdTag> list = this.list(
                new LambdaQueryWrapper<BdTag>()
                        .notIn(bdTag.getExcludeIds() != null && bdTag.getExcludeIds().size() > 0, BdTag::getId, bdTag.getExcludeIds())
                        .like(StringUtils.isNotEmpty(bdTag.getName()), BdTag::getName, bdTag.getName())
                        .apply(StringUtils.isNotEmpty(bdTag.getCommFilter()), " (number like '%" + bdTag.getCommFilter() + "%' or name like '%" + bdTag.getCommFilter() + "%')")
        );

        return list;
    }

    @Override
    public BdTag add(BdTag bdTag) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdTag.getName())) {
            List<BdTag> list = this.list(
                    new LambdaQueryWrapper<BdTag>()
                            .eq(BdTag::getName, bdTag.getName())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同的标签");
            }
        } else {
            throw new BizException("请填写标签名");
        }

        bdTag.setCreateTime(sdf.format(new Date()));
        bdTag.setCreatorId(RequestUtils.getUserId());
        bdTag.setCreator(RequestUtils.getNickname());
        if (!this.save(bdTag)) {
            throw new BizException("保存失败");
        }

        return bdTag;
    }

    @Override
    public BdTag myUpdate(BdTag bdTag) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdTag.getName())) {
            List<BdTag> list = this.list(
                    new LambdaQueryWrapper<BdTag>()
                            .eq(BdTag::getName, bdTag.getName())
                            .ne(BdTag::getId, bdTag.getId())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同的标签");
            }
        } else {
            throw new BizException("请填写标签名");
        }

        bdTag.setUpdateTime(sdf.format(new Date()));
        bdTag.setUpdaterId(RequestUtils.getUserId());
        bdTag.setUpdater(RequestUtils.getNickname());
        if (!this.updateById(bdTag)) {
            throw new BizException("修改失败");
        }

        return bdTag;
    }

    @Override
    public Boolean deleteById(Long id) {
        // 删除校验
        BdTag tag = this.getById(id);
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
        List<BdTag> list = this.list(
                new LambdaQueryWrapper<BdTag>()
                        .in(BdTag::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<String> delIds = new ArrayList<>();

            // 删除校验
            for (BdTag tag : list) {
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
    public BdTag detail(String id) {
        BdTag result = this.getById(id);
        return result;
    }
}
