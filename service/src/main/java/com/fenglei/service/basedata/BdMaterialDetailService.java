package com.fenglei.service.basedata;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.basedata.BdMaterialDetail;

import java.util.List;

/**
 * @author ljw
 */
public interface BdMaterialDetailService extends IService<BdMaterialDetail> {
    List<BdMaterialDetail> listDetailByIds(List<String> ids);

    BdMaterialDetail getDetailByIds(String id);
}
