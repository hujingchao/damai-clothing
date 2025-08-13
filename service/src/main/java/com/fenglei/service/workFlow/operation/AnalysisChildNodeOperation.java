package com.fenglei.service.workFlow.operation;

import com.fenglei.model.workFlow.entity.ChildNode;
import com.fenglei.service.workFlow.util.ChildNodeUtil;
import com.fenglei.service.workFlow.util.ClassConversionTools;
import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User: yzy
 * Date: 2019/6/21 0021
 * Time: 15:42
 * Description: 解析正常节点
 * @author yzy
 */
public class AnalysisChildNodeOperation implements Command {

    @Override
    @SuppressWarnings("unchecked")
    public boolean execute(Context context) throws Exception {
        List<ChildNode> childNodeList =  ClassConversionTools.castList(context.get("childNodeList"), ChildNode.class);
        if (childNodeList.size() > ChildNodeUtil.NUMBER_INT_0) {
            List<ChildNode> reviewers = childNodeList.stream().filter(s -> ChildNodeUtil.NODE_TYPE_REVIEWER.equals(s.getType())).collect(Collectors.toList());
            List<ChildNode> routes = childNodeList.stream().filter(s -> ChildNodeUtil.NODE_TYPE_ROUTE.equals(s.getType())).collect(Collectors.toList());
            List<ChildNode> ccTos = childNodeList.stream().filter(s -> ChildNodeUtil.NODE_TYPE_CC.equals(s.getType())).collect(Collectors.toList());
            context.put("reviewers", reviewers);
            context.put("routes", routes);
            context.put("ccTos", ccTos);
        }
        return false;
    }

}
