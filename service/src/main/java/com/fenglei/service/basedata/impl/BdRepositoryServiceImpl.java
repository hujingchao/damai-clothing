package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.exception.BizException;
import com.fenglei.common.util.RequestUtils;
import com.fenglei.mapper.basedata.BdRepositoryMapper;
import com.fenglei.mapper.pur.PurPurchaseInstockItemMapper;
import com.fenglei.model.basedata.*;
import com.fenglei.model.pur.entity.PurPurchaseInstockItem;
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
public class BdRepositoryServiceImpl extends ServiceImpl<BdRepositoryMapper, BdRepository> implements BdRepositoryService {

    @Resource
    private BdRepositoryShelfService bdRepositoryShelfService;
    @Resource
    private BdRepositoryRowService bdRepositoryRowService;
    @Resource
    private BdRepositoryColumnService bdRepositoryColumnService;
    @Resource
    private BdPositionService bdPositionService;

    @Resource
    private PurPurchaseInstockItemMapper purPurchaseInstockItemMapper;

    @Override
    public IPage<BdRepository> myPage(Page page, BdRepository bdRepository) {
        IPage<BdRepository> iPage = baseMapper.getPage(page, bdRepository);
        return iPage;
    }

    @Override
    public List<BdRepository> myList(BdRepository bdRepository) {
        List<BdRepository> list = baseMapper.getList(bdRepository);
        return list;
    }

    @Override
    public BdRepository add(BdRepository bdRepository) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdRepository.getName())) {
            List<BdRepository> list = this.list(
                    new LambdaQueryWrapper<BdRepository>()
                            .eq(BdRepository::getName, bdRepository.getName())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同名称的仓库");
            }
        } else {
            throw new BizException("请填写名称");
        }

        IdentifierGenerator identifierGenerator = new DefaultIdentifierGenerator();
        String id = identifierGenerator.nextId(new Object()).toString();
        bdRepository.setId(id);

        bdRepository.setCreateTime(sdf.format(new Date()));
        bdRepository.setCreatorId(RequestUtils.getUserId());
        bdRepository.setCreator(RequestUtils.getNickname());

        // 货架
        List<BdRepositoryShelf> shelfList = bdRepository.getShelfList();
        if (shelfList != null && shelfList.size() > 0) {
            for (BdRepositoryShelf shelf : shelfList) {
                shelf.setPid(bdRepository.getId());

                shelf.setCreateTime(sdf.format(new Date()));
                shelf.setCreatorId(RequestUtils.getUserId());
                shelf.setCreator(RequestUtils.getNickname());
            }
            if (!bdRepositoryShelfService.saveBatch(shelfList)) {
                throw new BizException("保存失败，异常码1");
            }
        }

        // 行
        List<BdRepositoryRow> rowList = bdRepository.getRowList();
        if (rowList != null && rowList.size() > 0) {
            for (BdRepositoryRow row : rowList) {
                row.setPid(bdRepository.getId());

                row.setCreateTime(sdf.format(new Date()));
                row.setCreatorId(RequestUtils.getUserId());
                row.setCreator(RequestUtils.getNickname());
            }
            if (!bdRepositoryRowService.saveBatch(rowList)) {
                throw new BizException("保存失败，异常码2");
            }
        }

        // 列
        List<BdRepositoryColumn> columnList = bdRepository.getColumnList();
        if (columnList != null && columnList.size() > 0) {
            for (BdRepositoryColumn column : columnList) {
                column.setPid(bdRepository.getId());

                column.setCreateTime(sdf.format(new Date()));
                column.setCreatorId(RequestUtils.getUserId());
                column.setCreator(RequestUtils.getNickname());
            }
            if (!bdRepositoryColumnService.saveBatch(columnList)) {
                throw new BizException("保存失败，异常码3");
            }
        }

        // 货位明细
        List<BdPosition> positions = bdRepository.getPositions();
        if (positions != null && positions.size() > 0) {
            for (BdPosition position : positions) {
                position.setPid(bdRepository.getId());

                position.setCreateTime(sdf.format(new Date()));
                position.setCreatorId(RequestUtils.getUserId());
                position.setCreator(RequestUtils.getNickname());
            }
            if (!bdPositionService.saveBatch(positions)) {
                throw new BizException("保存失败，异常码4");
            }

            bdRepository.setUsePosition(true);
        } else {
            bdRepository.setUsePosition(false);
        }

        if (!this.save(bdRepository)) {
            throw new BizException("保存失败");
        }

        return bdRepository;
    }

    @Override
    public BdRepository myUpdate(BdRepository bdRepository) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        if (StringUtils.isNotEmpty(bdRepository.getName())) {
            List<BdRepository> list = this.list(
                    new LambdaQueryWrapper<BdRepository>()
                            .eq(BdRepository::getName, bdRepository.getName())
                            .ne(BdRepository::getId, bdRepository.getId())
            );
            if (list != null && list.size() > 0) {
                throw new BizException("已存在相同名称的仓库");
            }
        } else {
            throw new BizException("请填写名称");
        }

        bdRepository.setUpdateTime(sdf.format(new Date()));
        bdRepository.setUpdaterId(RequestUtils.getUserId());
        bdRepository.setUpdater(RequestUtils.getNickname());

        // 删除旧数据
        // 删除货架
        List<BdRepositoryShelf> oldShelfList = bdRepositoryShelfService.list(
                new LambdaQueryWrapper<BdRepositoryShelf>()
                        .eq(BdRepositoryShelf::getPid, bdRepository.getId())
        );
        if (oldShelfList != null && oldShelfList.size() > 0) {
            List<String> oldShelfIds = oldShelfList.stream().map(BdRepositoryShelf::getId).collect(Collectors.toList());
            if (oldShelfIds != null && oldShelfIds.size() > 0) {
                if (!bdRepositoryShelfService.removeByIds(oldShelfIds)) {
                    throw new BizException("货架更新失败");
                }
            }
        }

        // 删除行
        List<BdRepositoryRow> oldRowList = bdRepositoryRowService.list(
                new LambdaQueryWrapper<BdRepositoryRow>()
                        .eq(BdRepositoryRow::getPid, bdRepository.getId())
        );
        if (oldRowList != null && oldRowList.size() > 0) {
            List<String> oldRowIds = oldRowList.stream().map(BdRepositoryRow::getId).collect(Collectors.toList());
            if (oldRowIds != null && oldRowIds.size() > 0) {
                if (!bdRepositoryRowService.removeByIds(oldRowIds)) {
                    throw new BizException("排更新失败");
                }
            }
        }

        // 删除列
        List<BdRepositoryColumn> oldColumnList = bdRepositoryColumnService.list(
                new LambdaQueryWrapper<BdRepositoryColumn>()
                        .eq(BdRepositoryColumn::getPid, bdRepository.getId())
        );
        if (oldColumnList != null && oldColumnList.size() > 0) {
            List<String> oldColumnIds = oldColumnList.stream().map(BdRepositoryColumn::getId).collect(Collectors.toList());
            if (oldColumnIds != null && oldColumnIds.size() > 0) {
                if (!bdRepositoryColumnService.removeByIds(oldColumnIds)) {
                    throw new BizException("列更新失败");
                }
            }
        }

        // 删除货位
        List<BdPosition> oldPositions = bdPositionService.list(
                new LambdaQueryWrapper<BdPosition>()
                        .eq(BdPosition::getPid, bdRepository.getId())
        );
        if (oldPositions != null && oldPositions.size() > 0) {
            List<String> oldPositionIds = oldPositions.stream().map(BdPosition::getId).collect(Collectors.toList());
            if (oldPositionIds != null && oldPositionIds.size() > 0) {
                if (!bdPositionService.removeByIds(oldPositionIds)) {
                    throw new BizException("货位更新失败");
                }
            }
        }

        // 保存新数据
        // 货架
        List<BdRepositoryShelf> shelfList = bdRepository.getShelfList();
        if (shelfList != null && shelfList.size() > 0) {
            for (BdRepositoryShelf shelf : shelfList) {
                shelf.setPid(bdRepository.getId());

                shelf.setCreateTime(sdf.format(new Date()));
                shelf.setCreatorId(RequestUtils.getUserId());
                shelf.setCreator(RequestUtils.getNickname());
            }
            if (!bdRepositoryShelfService.saveBatch(shelfList)) {
                throw new BizException("保存失败，异常码1");
            }
        }

        // 行
        List<BdRepositoryRow> rowList = bdRepository.getRowList();
        if (rowList != null && rowList.size() > 0) {
            for (BdRepositoryRow row : rowList) {
                row.setPid(bdRepository.getId());

                row.setCreateTime(sdf.format(new Date()));
                row.setCreatorId(RequestUtils.getUserId());
                row.setCreator(RequestUtils.getNickname());
            }
            if (!bdRepositoryRowService.saveBatch(rowList)) {
                throw new BizException("保存失败，异常码2");
            }
        }

        // 列
        List<BdRepositoryColumn> columnList = bdRepository.getColumnList();
        if (columnList != null && columnList.size() > 0) {
            for (BdRepositoryColumn column : columnList) {
                column.setPid(bdRepository.getId());

                column.setCreateTime(sdf.format(new Date()));
                column.setCreatorId(RequestUtils.getUserId());
                column.setCreator(RequestUtils.getNickname());
            }
            if (!bdRepositoryColumnService.saveBatch(columnList)) {
                throw new BizException("保存失败，异常码3");
            }
        }

        // 货位明细
        List<BdPosition> positions = bdRepository.getPositions();
        if (positions != null && positions.size() > 0) {
            for (BdPosition position : positions) {
                position.setPid(bdRepository.getId());

                position.setCreateTime(sdf.format(new Date()));
                position.setCreatorId(RequestUtils.getUserId());
                position.setCreator(RequestUtils.getNickname());
            }
            if (!bdPositionService.saveBatch(positions)) {
                throw new BizException("保存失败，异常码4");
            }

            bdRepository.setUsePosition(true);
        } else {
            bdRepository.setUsePosition(false);
        }

        if (!this.updateById(bdRepository)) {
            throw new BizException("修改失败");
        }

        return bdRepository;
    }

    Boolean chkIsUsed(List<String> ids) {
        List<PurPurchaseInstockItem> purchaseInstockItems = purPurchaseInstockItemMapper.selectList(
                new LambdaQueryWrapper<PurPurchaseInstockItem>()
                        .in(PurPurchaseInstockItem::getRepositoryId, ids)
        );
        if (purchaseInstockItems != null && purchaseInstockItems.size() > 0) {
            throw new BizException("当前仓库已被 采购入库单 引用，无法删除1");
        }

        return false;
    }

    @Override
    public Boolean deleteById(Long id) {
        BdRepository bdRepository = this.getById(id);
        if (bdRepository == null) {
            return true;
        }

        List<String> ids = new ArrayList<>();
        ids.add(id.toString());
        if (this.chkIsUsed(ids)) {
            throw new BizException("仓库已被引用");
        }

        if (!this.removeById(id)) {
            throw new BizException("删除失败");
        }

        // 删除旧数据
        // 删除货架
        List<BdRepositoryShelf> oldShelfList = bdRepositoryShelfService.list(
                new LambdaQueryWrapper<BdRepositoryShelf>()
                        .eq(BdRepositoryShelf::getPid, bdRepository.getId())
        );
        if (oldShelfList != null && oldShelfList.size() > 0) {
            List<String> oldShelfIds = oldShelfList.stream().map(BdRepositoryShelf::getId).collect(Collectors.toList());
            if (oldShelfIds != null && oldShelfIds.size() > 0) {
                if (!bdRepositoryShelfService.removeByIds(oldShelfIds)) {
                    throw new BizException("删除失败，异常码1");
                }
            }
        }

        // 删除行
        List<BdRepositoryRow> oldRowList = bdRepositoryRowService.list(
                new LambdaQueryWrapper<BdRepositoryRow>()
                        .eq(BdRepositoryRow::getPid, bdRepository.getId())
        );
        if (oldRowList != null && oldRowList.size() > 0) {
            List<String> oldRowIds = oldRowList.stream().map(BdRepositoryRow::getId).collect(Collectors.toList());
            if (oldRowIds != null && oldRowIds.size() > 0) {
                if (!bdRepositoryRowService.removeByIds(oldRowIds)) {
                    throw new BizException("删除失败，异常码2");
                }
            }
        }

        // 删除列
        List<BdRepositoryColumn> oldColumnList = bdRepositoryColumnService.list(
                new LambdaQueryWrapper<BdRepositoryColumn>()
                        .eq(BdRepositoryColumn::getPid, bdRepository.getId())
        );
        if (oldColumnList != null && oldColumnList.size() > 0) {
            List<String> oldColumnIds = oldColumnList.stream().map(BdRepositoryColumn::getId).collect(Collectors.toList());
            if (oldColumnIds != null && oldColumnIds.size() > 0) {
                if (!bdRepositoryColumnService.removeByIds(oldColumnIds)) {
                    throw new BizException("删除失败，异常码3");
                }
            }
        }

        // 删除货位
        List<BdPosition> oldPositions = bdPositionService.list(
                new LambdaQueryWrapper<BdPosition>()
                        .eq(BdPosition::getPid, bdRepository.getId())
        );
        if (oldPositions != null && oldPositions.size() > 0) {
            List<String> oldPositionIds = oldPositions.stream().map(BdPosition::getId).collect(Collectors.toList());
            if (oldPositionIds != null && oldPositionIds.size() > 0) {
                if (!bdPositionService.removeByIds(oldPositionIds)) {
                    throw new BizException("删除失败，异常码4");
                }
            }
        }

        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(String[] ids) {
        List<String> idList = Arrays.asList(ids);
        List<BdRepository> list = this.list(
                new LambdaQueryWrapper<BdRepository>()
                        .in(BdRepository::getId, idList)
        );
        if (list != null && list.size() > 0) {
            List<String> delIds = list.stream().map(BdRepository::getId).collect(Collectors.toList());
            if (this.chkIsUsed(delIds)) {
                throw new BizException("仓库已被引用");
            }

            if (!this.removeByIds(delIds)) {
                throw new BizException("删除失败");
            }

            // 删除旧数据
            // 删除货架
            List<BdRepositoryShelf> oldShelfList = bdRepositoryShelfService.list(
                    new LambdaQueryWrapper<BdRepositoryShelf>()
                            .in(BdRepositoryShelf::getPid, delIds)
            );
            if (oldShelfList != null && oldShelfList.size() > 0) {
                List<String> oldShelfIds = oldShelfList.stream().map(BdRepositoryShelf::getId).collect(Collectors.toList());
                if (oldShelfIds != null && oldShelfIds.size() > 0) {
                    if (!bdRepositoryShelfService.removeByIds(oldShelfIds)) {
                        throw new BizException("删除失败，异常码1");
                    }
                }
            }

            // 删除行
            List<BdRepositoryRow> oldRowList = bdRepositoryRowService.list(
                    new LambdaQueryWrapper<BdRepositoryRow>()
                            .in(BdRepositoryRow::getPid, delIds)
            );
            if (oldRowList != null && oldRowList.size() > 0) {
                List<String> oldRowIds = oldRowList.stream().map(BdRepositoryRow::getId).collect(Collectors.toList());
                if (oldRowIds != null && oldRowIds.size() > 0) {
                    if (!bdRepositoryRowService.removeByIds(oldRowIds)) {
                        throw new BizException("删除失败，异常码2");
                    }
                }
            }

            // 删除列
            List<BdRepositoryColumn> oldColumnList = bdRepositoryColumnService.list(
                    new LambdaQueryWrapper<BdRepositoryColumn>()
                            .in(BdRepositoryColumn::getPid, delIds)
            );
            if (oldColumnList != null && oldColumnList.size() > 0) {
                List<String> oldColumnIds = oldColumnList.stream().map(BdRepositoryColumn::getId).collect(Collectors.toList());
                if (oldColumnIds != null && oldColumnIds.size() > 0) {
                    if (!bdRepositoryColumnService.removeByIds(oldColumnIds)) {
                        throw new BizException("删除失败，异常码3");
                    }
                }
            }

            // 删除货位
            List<BdPosition> oldPositions = bdPositionService.list(
                    new LambdaQueryWrapper<BdPosition>()
                            .in(BdPosition::getPid, delIds)
            );
            if (oldPositions != null && oldPositions.size() > 0) {
                List<String> oldPositionIds = oldPositions.stream().map(BdPosition::getId).collect(Collectors.toList());
                if (oldPositionIds != null && oldPositionIds.size() > 0) {
                    if (!bdPositionService.removeByIds(oldPositionIds)) {
                        throw new BizException("删除失败，异常码4");
                    }
                }
            }
        }
    }

    @Override
    public BdRepository detail(String id) {
        BdRepository result = this.getById(id);
        if (result != null) {
            // 货架
            List<BdRepositoryShelf> shelfList = bdRepositoryShelfService.list(
                    new LambdaQueryWrapper<BdRepositoryShelf>()
                            .eq(BdRepositoryShelf::getPid, result.getId())
            );
            result.setShelfList(shelfList);

            // 行
            List<BdRepositoryRow> rowList = bdRepositoryRowService.list(
                    new LambdaQueryWrapper<BdRepositoryRow>()
                            .eq(BdRepositoryRow::getPid, result.getId())
            );
            result.setRowList(rowList);

            // 列
            List<BdRepositoryColumn> columnList = bdRepositoryColumnService.list(
                    new LambdaQueryWrapper<BdRepositoryColumn>()
                            .eq(BdRepositoryColumn::getPid, result.getId())
            );
            result.setColumnList(columnList);

            // 货位
            List<BdPosition> positions = bdPositionService.list(
                    new LambdaQueryWrapper<BdPosition>()
                            .eq(BdPosition::getPid, result.getId())
            );
            result.setPositions(positions);
            result.setPositionCount(positions.size());
        }

        return result;
    }
}
