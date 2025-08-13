package com.fenglei.service.prd;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.prd.entity.PrdMoColorSpecData;

import java.util.List;

public interface PrdMoColorSpecDataService extends IService<PrdMoColorSpecData> {
    List<PrdMoColorSpecData> listByPid(String pid);

    List<PrdMoColorSpecData> listDetailByIds(List<String> moItemIds);
}
