package com.fenglei.mapper.prd;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.fenglei.model.prd.entity.PrdMoAttachFile;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PrdMoAttachFileMapper extends BaseMapper<PrdMoAttachFile> {

    List<PrdMoAttachFile> getList(@Param("prdMoAttachFile") PrdMoAttachFile prdMoAttachFile);
}
