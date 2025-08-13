package com.fenglei.service.inv;

import com.fenglei.common.result.Result;
import com.fenglei.model.inv.entity.InvPackage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fenglei.model.inv.entity.InvPackageItem;
import com.fenglei.model.inv.entity.InvPackageNo;
import com.fenglei.model.inv.vo.InvPackageItemVo;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * <p>
 * 入库后打包 服务类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-23
 */
public interface IInvPackageService extends IService<InvPackage> {
    /**
     * 新增
     */
    InvPackage add(InvPackage invPackage);

    /**
     * 删除
     */
    Boolean myRemoveById(String id);

    /**
     * 批量删除
     */
    Boolean myRemoveByIds(List<String> ids);

    /**
     * 更新
     */
    boolean myUpdate(InvPackage invPackage);

    /**
     * 分页查询
     */
    IPage<InvPackage> myPage(Page page, InvPackage invPackage);

    /**
     * 分录分页查询
     */
    IPage<InvPackageItemVo> itemPage(Page page, InvPackage invPackage);

    /**
     * 详情
     */
    InvPackage detail(String id);

    /**
     * 获取编号
     */
    String getNo();

    InvPackage submit(String id);

    String batchSubmitByIds(String[] ids);

    Result<InvPackage> doAction(InvPackage invPackage) throws Exception;

    Result<String> batchDoAction(InvPackage invPackage) throws Exception;

    InvPackage unAudit(String id);

    String batchUnAuditByIds(String[] ids);

    List<InvPackageNo> listPackageNoById(String id);

    void itemExport(HttpServletResponse response, InvPackage invPackage);
}
