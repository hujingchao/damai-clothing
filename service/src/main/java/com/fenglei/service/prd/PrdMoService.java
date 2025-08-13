package com.fenglei.service.prd;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.common.result.Result;
import com.fenglei.model.prd.entity.PrdMo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface PrdMoService extends IService<PrdMo> {

    IPage<PrdMo> myPage(Page page, PrdMo prdMo);

    PrdMo getPageSummaryData(PrdMo prdMo);

    List<PrdMo> myList(PrdMo prdMo);

    PrdMo add(PrdMo prdMo) throws Exception;

    PrdMo myUpdate(PrdMo prdMo) throws Exception;

    Boolean deleteById(Long id);

    void deleteBatch(String[] ids);

    PrdMo detail(String id);

    PrdMo submit(String id) throws Exception;

    String batchSubmitByIds(String[] ids) throws Exception;

    Result doAction(PrdMo prdMo) throws Exception;

    Result batchDoAction(PrdMo prdMo) throws Exception;

    PrdMo unAudit(String id) throws Exception;

    String batchUnAuditByIds(String[] ids) throws Exception;

    PrdMo top(String id);

    PrdMo unTop(String id);

    void exportPrdMo(HttpServletResponse response, PrdMo prdMo);

    boolean closeOrder(String id);
}
