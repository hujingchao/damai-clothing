package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.mapper.basedata.BdColorMapper;
import com.fenglei.mapper.prd.PrdMoMapper;
import com.fenglei.model.basedata.BdColor;
import com.fenglei.model.prd.entity.PrdMo;
import com.fenglei.service.basedata.BdColorService;
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
public class BdColorServiceImpl extends ServiceImpl<BdColorMapper, BdColor> implements BdColorService {

    @Resource
    private PrdMoMapper prdMoMapper;

    @Override
    public IPage<BdColor> myPage(Page page, BdColor bdColor) {
        IPage<BdColor> iPage = this.page(page,
                new LambdaQueryWrapper<BdColor>()
                        .like(StringUtils.isNotEmpty(bdColor.getName()), BdColor::getName, bdColor.getName())
                        .notIn(bdColor.getExcludeIds() != null && bdColor.getExcludeIds().size() > 0, BdColor::getId, bdColor.getExcludeIds())
                        .orderByDesc(BdColor::getCreateTime)
        );

        return iPage;
    }

    @Override
    public List<BdColor> myList(BdColor bdColor) {
        List<BdColor> list = this.list(
                new LambdaQueryWrapper<BdColor>()
                        .notIn(bdColor.getExcludeIds() != null && bdColor.getExcludeIds().size() > 0, BdColor::getId, bdColor.getExcludeIds())
                        .like(StringUtils.isNotEmpty(bdColor.getName()), BdColor::getName, bdColor.getName())
                        .apply(StringUtils.isNotEmpty(bdColor.getCommFilter()), " (number like '%" + bdColor.getCommFilter() + "%' or name like '%" + bdColor.getCommFilter() + "%')")
        );

        return list;
    }

    @Override
    public BdColor add(BdColor bdColor) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdColor.getName())) {
            List<BdColor> list = this.list(
                    new LambdaQueryWrapper<BdColor>()
                            .eq(BdColor::getName, bdColor.getName())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同的标签");
            }
        } else {
            throw new BizException("请填写标签名");
        }

        bdColor.setCreateTime(sdf.format(new Date()));
        bdColor.setCreatorId(RequestUtils.getUserId());
        bdColor.setCreator(RequestUtils.getNickname());
        if (!this.save(bdColor)) {
            throw new BizException("保存失败");
        }

        return bdColor;
    }

    @Override
    public BdColor myUpdate(BdColor bdColor) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdColor.getName())) {
            List<BdColor> list = this.list(
                    new LambdaQueryWrapper<BdColor>()
                            .eq(BdColor::getName, bdColor.getName())
                            .ne(BdColor::getId, bdColor.getId())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同的标签");
            }
        } else {
            throw new BizException("请填写标签名");
        }

        bdColor.setUpdateTime(sdf.format(new Date()));
        bdColor.setUpdaterId(RequestUtils.getUserId());
        bdColor.setUpdater(RequestUtils.getNickname());
        if (!this.updateById(bdColor)) {
            throw new BizException("修改失败");
        }

        return bdColor;
    }

    @Override
    public Boolean deleteById(Long id) {
        // 删除校验
        BdColor tag = this.getById(id);
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
        if (prdMos != null && !prdMos.isEmpty()) {
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
        List<BdColor> list = this.list(
                new LambdaQueryWrapper<BdColor>()
                        .in(BdColor::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<String> delIds = new ArrayList<>();

            // 删除校验
            for (BdColor tag : list) {
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
    public BdColor detail(String id) {
        BdColor result = this.getById(id);
        return result;
    }
}
