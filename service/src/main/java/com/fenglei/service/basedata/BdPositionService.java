package com.fenglei.service.basedata;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fenglei.model.basedata.BdPosition;

import java.util.List;

/**
 * @author ljw
 */
public interface BdPositionService extends IService<BdPosition> {

    List<BdPosition> myList(BdPosition bdPosition);

}
