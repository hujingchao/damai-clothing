package com.fenglei.service.inv;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.inv.entity.InvPackageNo;

import java.util.List;

/**
 * <p>
 * 打包单包号明细 服务类
 * </p>
 *
 * @author zhouyiqiu
 * @since 2024-04-23
 */
public interface IInvPackageNoService extends IService<InvPackageNo> {

    /**
     * 批量新增
     */
    List<InvPackageNo> batchAdd(List<InvPackageNo> packageNos);

    /**
     * 根据pid删除
     */
    Boolean removeByPid(String pid);

    /**
     * 根据pid查询
     */
    List<InvPackageNo> listByPid(String pid);
}
