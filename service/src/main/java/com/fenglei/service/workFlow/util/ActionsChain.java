package com.fenglei.service.workFlow.util;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.impl.ChainBase;

import java.util.List;

/**
 * User: yzy
 * Date: 2019/6/21 0021
 * Time: 13:09
 * Description: 组装器
 * @author yzy
 */

public class ActionsChain extends ChainBase {

    public ActionsChain(List<Command> commandList) {
        for (Command command : commandList) {
            addCommand(command);
        }
    }

}
