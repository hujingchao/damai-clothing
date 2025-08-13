package com.fenglei.service.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.BdTempWages;

import java.util.List;

/**
 * @author ljw
 */
public interface BdTempWagesService extends IService<BdTempWages> {

    IPage<BdTempWages> myPage(Page page, BdTempWages bdTempWages);

    List<BdTempWages> myList(BdTempWages bdTempWages);

    BdTempWages add(BdTempWages bdTempWages) throws Exception;

    BdTempWages myUpdate(BdTempWages bdTempWages) throws Exception;

    Boolean deleteById(String id);

    void deleteBatch(String[] ids);

    BdTempWages detail(String id);

    BdTempWages submit(String id) throws Exception;

    String batchSubmitByIds(String[] ids) throws Exception;

    Result doAction(BdTempWages bdTempWages) throws Exception;

    Result batchDoAction(BdTempWages bdTempWages) throws Exception;

    BdTempWages unAudit(String id);

    String batchUnAuditByIds(String[] ids);

    BdTempWages cancel(String id);

    String batchCancelByIds(String[] ids);

    BdTempWages unCancel(String id);

    String batchUnCancelByIds(String[] ids);
}
