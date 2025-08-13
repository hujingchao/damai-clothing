package com.fenglei.service.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.BdTempMaterial;
import com.fenglei.model.basedata.BdTempMaterialColor;
import com.fenglei.model.basedata.BdTempMaterialSpecification;

import java.util.List;

/**
 * @author ljw
 */
public interface BdTempMaterialService extends IService<BdTempMaterial> {

    IPage<BdTempMaterial> myPage(Page page, BdTempMaterial bdTempMaterial);

    List<BdTempMaterial> myList(BdTempMaterial bdTempMaterial);

    BdTempMaterial add(BdTempMaterial bdTempMaterial) throws Exception;

    BdTempMaterial saveFin(BdTempMaterial bdTempMaterial) throws Exception;

    BdTempMaterial myUpdate(BdTempMaterial bdTempMaterial) throws Exception;

    Boolean deleteById(String id);

    void deleteBatch(String[] ids);

    BdTempMaterial detail(String id);

    BdTempMaterial submit(String id) throws Exception;

    String batchSubmitByIds(String[] ids) throws Exception;

    Result doAction(BdTempMaterial bdTempMaterial) throws Exception;

    Result batchDoAction(BdTempMaterial bdTempMaterial) throws Exception;

    BdTempMaterial unAudit(String id);

    String batchUnAuditByIds(String[] ids);

    List<BdTempMaterialColor> listColor(BdTempMaterial bdTempMaterial);

    List<BdTempMaterialSpecification> listSpecification(BdTempMaterial bdTempMaterial);
}
