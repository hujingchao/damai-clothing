package com.fenglei.mapper.prd;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.prd.entity.PrdMoAttachImg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrdMoAttachImgMapper extends BaseMapper<PrdMoAttachImg> {

    List<PrdMoAttachImg> getList(@Param("prdMoAttachImg") PrdMoAttachImg prdMoAttachImg);
}
