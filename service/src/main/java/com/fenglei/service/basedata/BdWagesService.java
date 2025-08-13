package com.fenglei.service.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.BdWages;

import java.util.List;

/**
 * @author ljw
 */
public interface BdWagesService extends IService<BdWages> {

    IPage<BdWages> myPage(Page page, BdWages bdWages);

    IPage<BdWages> myPageWithTemp(Page page, BdWages bdWages) throws Exception;

    List<BdWages> myList(BdWages bdWages);

    BdWages add(BdWages bdWages) throws Exception;

    BdWages myUpdate(BdWages bdWages) throws Exception;

    Boolean deleteById(Long id);

    void deleteBatch(String[] ids);

    BdWages detail(String id);

    BdWages submit(String id) throws Exception;

    String batchSubmitByIds(String[] ids) throws Exception;

    Result doAction(BdWages bdWages) throws Exception;

    Result batchDoAction(BdWages bdWages) throws Exception;

    BdWages unAudit(String id) throws Exception;

    String batchUnAuditByIds(String[] ids) throws Exception;

    BdWages cancel(String id);

    String batchCancelByIds(String[] ids);

    BdWages unCancel(String id);

    String batchUnCancelByIds(String[] ids);
}
