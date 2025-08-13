package com.fenglei.service.basedata.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fenglei.common.util.StringUtils;
import com.fenglei.mapper.basedata.BdMaterialDetailMapper;
import com.fenglei.model.basedata.BdMaterialDetail;
import com.fenglei.model.system.entity.SysFiles;
import com.fenglei.service.basedata.BdMaterialDetailService;
import com.fenglei.service.system.SysFilesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ljw
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BdMaterialDetailServiceImpl extends ServiceImpl<BdMaterialDetailMapper, BdMaterialDetail> implements BdMaterialDetailService {
    @Autowired
    SysFilesService filesService;
    @Override
    public List<BdMaterialDetail> listDetailByIds(List<String> ids) {
        List<BdMaterialDetail> bdMaterialDetails = this.listByIds(ids);
        List<String> collect = bdMaterialDetails.stream().map(BdMaterialDetail::getMainPicId).collect(Collectors.toList());
        if (!collect.isEmpty()){
            List<SysFiles> sysFiles = filesService.listByIds(collect);
            for (BdMaterialDetail bdMaterialDetail : bdMaterialDetails) {
                SysFiles files = sysFiles.stream().filter(t -> t.getId().equals(bdMaterialDetail.getMainPicId())).findFirst().orElse(null);
                if (null != files) {
                    bdMaterialDetail.setMainPic(files);
                    bdMaterialDetail.setMainPicUrl(files.getUrl());
                }
            }
        }
        return bdMaterialDetails;
    }

    @Override
    public BdMaterialDetail getDetailByIds(String id) {
        BdMaterialDetail materialDetail = this.getById(id);
        if(StringUtils.isNotEmpty(materialDetail.getMainPicId())){
            SysFiles sysFiles = filesService.getById(materialDetail.getMainPicId());
            materialDetail.setMainPic(sysFiles);
            materialDetail.setMainPicUrl(sysFiles.getUrl());
        }
        return materialDetail;
    }
}
