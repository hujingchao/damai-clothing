package com.fenglei.service.basedata;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.common.result.Result;
import com.fenglei.model.basedata.*;
import com.fenglei.model.inv.entity.InvInventory;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author ljw
 */
public interface BdMaterialService extends IService<BdMaterial> {

    IPage<BdMaterial> myPage(Page page, BdMaterial bdMaterial);

    List<BdMaterial> myList(BdMaterial bdMaterial);

    List<BdMaterial> listMtrlNum(BdMaterial bdMaterial);

    BdMaterial detailMtrlNum(BdMaterial bdMaterial);

    BdMaterial add(BdMaterial bdMaterial) throws Exception;

    BdMaterial myUpdate(BdMaterial bdMaterial) throws Exception;

    Boolean deleteById(String id);

    void deleteBatch(String[] ids);

    BdMaterial detail(String id);

    BdMaterial submit(String id) throws Exception;

    String batchSubmitByIds(String[] ids) throws Exception;

    Result doAction(BdMaterial bdMaterial) throws Exception;

    Result batchDoAction(BdMaterial bdMaterial) throws Exception;

    BdMaterial unAudit(String id) throws Exception;

    String batchUnAuditByIds(String[] ids) throws Exception;

    List<BdMaterialColor> listColor(BdMaterial bdMaterial);

    List<BdMaterialSpecification> listSpecification(BdMaterial bdMaterial);

    List<BdMaterialSupplier> listSupplier(BdMaterial bdMaterial);


    BdMaterial saveFin(BdMaterial bdMaterial) throws Exception;

    Boolean deleteByIdFin(String id);

    void deleteBatchFin(String[] ids);

    BdMaterial detailFin(String id);

    BdMaterialDetail submitFin(String id) throws Exception;

    String batchSubmitByIdsFin(String[] ids) throws Exception;

    Result doActionFin(BdMaterialDetail bdMaterialDetail) throws Exception;

    Result batchDoActionFin(BdMaterialDetail bdMaterialDetail) throws Exception;

    BdMaterialDetail unAuditFin(String id) throws Exception;

    String batchUnAuditByIdsFin(String[] ids) throws Exception;

    IPage<BdMaterial> myPageFin(Page page, BdMaterial bdMaterial);

    List<BdMaterial> myListFin(BdMaterial bdMaterial);

    List<BdMaterialRaw> getRawsFin(BdMaterial bdMaterial);

    List<BdMaterial> listForSelect(BdMaterial bdMaterial);

    void updateProcessPrice(String id);

    IPage<InvInventory> pageForInv(Page<InvInventory> page, InvInventory invInventory);

    void exportMaterialInventory(HttpServletResponse response, InvInventory invInventory);
}
