package com.fenglei.service.prd.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.mapper.prd.PrdCuttingRawMapper;
import com.fenglei.model.basedata.BdMaterial;
import com.fenglei.model.basedata.BdMaterialDetail;
import com.fenglei.model.basedata.BdUnit;
import com.fenglei.model.prd.entity.PrdCuttingRaw;
import com.fenglei.model.system.entity.SysFiles;
import com.fenglei.service.basedata.BdMaterialDetailService;
import com.fenglei.service.basedata.BdMaterialService;
import com.fenglei.service.basedata.BdUnitService;
import com.fenglei.service.prd.IPrdCuttingRawService;
import com.fenglei.service.system.SysFilesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 裁床单 - 原材料信息 服务实现类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-11
 */
@Service
public class PrdCuttingRawServiceImpl extends ServiceImpl<PrdCuttingRawMapper, PrdCuttingRaw> implements IPrdCuttingRawService {

    @Autowired
    BdMaterialService materialService;
    @Autowired
    BdMaterialDetailService materialDetailService;
    @Autowired
    BdUnitService unitService;
    @Autowired
    SysFilesService filesService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<PrdCuttingRaw> batchAdd(List<PrdCuttingRaw> cuttingRaws) {
        this.saveBatch(cuttingRaws);
        return cuttingRaws;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeByPid(String pid) {
        return this.remove(Wrappers.lambdaQuery(PrdCuttingRaw.class).eq(PrdCuttingRaw::getPid, pid));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeByPids(List<String> pids) {
        return this.remove(Wrappers.lambdaQuery(PrdCuttingRaw.class).in(PrdCuttingRaw::getPid, pids));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean batchSaveOrUpdate(List<PrdCuttingRaw> cuttingRaws) {
        return this.saveOrUpdateBatch(cuttingRaws);
    }

    @Override
    public List<PrdCuttingRaw> listByPid(String pid) {
        return this.baseMapper.listByPid(pid);
    }
}
