package com.fenglei.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.system.entity.Approving;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApprovingMapper extends BaseMapper<Approving> {

    IPage<Approving> getPage(Page page, @Param("approving") Approving approving);

    List<Approving> getList(@Param("approving") Approving approving);
}
