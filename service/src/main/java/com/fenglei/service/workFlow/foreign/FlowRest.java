package com.fenglei.service.workFlow.foreign;

/**
 * @author yzy
 */
public class FlowRest {

    public static String content = "{\n" +
            "\t\"tableId\": 1,\n" +
            "\t\"workFlowVersionId\": \"\",\n" +
            "\t\"workFlowDef\": {\n" +
            "\t\t\"name\": \"合同审批\",\n" +
            "\t\t\"publicFlag\": 1,\n" +
            "\t\t\"sortNo\": 5,\n" +
            "\t\t\"duplicateRemovelFlag\": 1,\n" +
            "\t\t\"optionTip\": \"\",\n" +
            "\t\t\"optionNotNull\": 0,\n" +
            "\t\t\"status\": 1\n" +
            "\t},\n" +
            "\t\"directorMaxLevel\": 4,\n" +
            "\t\"flowPermission\": [],\n" +
            "\t\"nodeConfig\": {\n" +
            "\t\t\"pkId\": \"sid-start-node\",\n" +
            "\t\t\"nodeName\": \"发起人\",\n" +
            "\t\t\"type\": 0,\n" +
            "\t\t\"priorityLevel\": \"\",\n" +
            "\t\t\"settype\": \"\",\n" +
            "\t\t\"selectMode\": \"\",\n" +
            "\t\t\"selectRange\": \"\",\n" +
            "\t\t\"examineRoleId\": \"\",\n" +
            "\t\t\"directorLevel\": \"\",\n" +
            "\t\t\"replaceByUp\": \"\",\n" +
            "\t\t\"examineMode\": \"\",\n" +
            "\t\t\"noHanderAction\": \"\",\n" +
            "\t\t\"examineEndType\": \"\",\n" +
            "\t\t\"examineEndRoleId\": \"\",\n" +
            "\t\t\"examineEndDirectorLevel\": \"\",\n" +
            "\t\t\"ccSelfSelectFlag\": \"\",\n" +
            "\t\t\"conditionList\": [],\n" +
            "\t\t\"nodeUserList\": [],\n" +
            "\t\t\"childNode\": {\n" +
            "\t\t\t\"nodeName\": \"审核人\",\n" +
            "\t\t\t\"error\": false,\n" +
            "\t\t\t\"type\": 1,\n" +
            "\t\t\t\"settype\": 3,\n" +
            "\t\t\t\"selectMode\": 2,\n" +
            "\t\t\t\"selectRange\": 3,\n" +
            "\t\t\t\"directorLevel\": 1,\n" +
            "\t\t\t\"replaceByUp\": 0,\n" +
            "\t\t\t\"examineMode\": 1,\n" +
            "\t\t\t\"noHanderAction\": 2,\n" +
            "\t\t\t\"examineEndDirectorLevel\": 0,\n" +
            "\t\t\t\"childNode\": {\n" +
            "\t\t\t\t\"nodeName\": \"路由\",\n" +
            "\t\t\t\t\"type\": 4,\n" +
            "\t\t\t\t\"priorityLevel\": 1,\n" +
            "\t\t\t\t\"settype\": 1,\n" +
            "\t\t\t\t\"selectMode\": 0,\n" +
            "\t\t\t\t\"selectRange\": 0,\n" +
            "\t\t\t\t\"examineRoleId\": 0,\n" +
            "\t\t\t\t\"directorLevel\": 1,\n" +
            "\t\t\t\t\"replaceByUp\": 0,\n" +
            "\t\t\t\t\"examineMode\": 1,\n" +
            "\t\t\t\t\"noHanderAction\": 2,\n" +
            "\t\t\t\t\"examineEndType\": 0,\n" +
            "\t\t\t\t\"examineEndRoleId\": 0,\n" +
            "\t\t\t\t\"examineEndDirectorLevel\": 1,\n" +
            "\t\t\t\t\"ccSelfSelectFlag\": 1,\n" +
            "\t\t\t\t\"conditionList\": [],\n" +
            "\t\t\t\t\"nodeUserList\": [],\n" +
            "\t\t\t\t\"childNode\": {\n" +
            "\t\t\t\t\t\"nodeName\": \"抄送人\",\n" +
            "\t\t\t\t\t\"type\": 2,\n" +
            "\t\t\t\t\t\"ccSelfSelectFlag\": 1,\n" +
            "\t\t\t\t\t\"childNode\": null,\n" +
            "\t\t\t\t\t\"nodeUserList\": [],\n" +
            "\t\t\t\t\t\"error\": false\n" +
            "\t\t\t\t},\n" +
            "\t\t\t\t\"conditionNodes\": [\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"nodeName\": \"条件1\",\n" +
            "\t\t\t\t\t\t\"type\": 3,\n" +
            "\t\t\t\t\t\t\"priorityLevel\": 1,\n" +
            "\t\t\t\t\t\t\"settype\": 1,\n" +
            "\t\t\t\t\t\t\"selectMode\": 0,\n" +
            "\t\t\t\t\t\t\"selectRange\": 0,\n" +
            "\t\t\t\t\t\t\"examineRoleId\": 0,\n" +
            "\t\t\t\t\t\t\"directorLevel\": 1,\n" +
            "\t\t\t\t\t\t\"replaceByUp\": 0,\n" +
            "\t\t\t\t\t\t\"examineMode\": 1,\n" +
            "\t\t\t\t\t\t\"noHanderAction\": 2,\n" +
            "\t\t\t\t\t\t\"examineEndType\": 0,\n" +
            "\t\t\t\t\t\t\"examineEndRoleId\": 0,\n" +
            "\t\t\t\t\t\t\"examineEndDirectorLevel\": 1,\n" +
            "\t\t\t\t\t\t\"ccSelfSelectFlag\": 1,\n" +
            "\t\t\t\t\t\t\"conditionList\": [\n" +
            "\t\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\t\"columnId\": 0,\n" +
            "\t\t\t\t\t\t\t\t\"type\": 1,\n" +
            "\t\t\t\t\t\t\t\t\"conditionEn\": \"\",\n" +
            "\t\t\t\t\t\t\t\t\"conditionCn\": \"\",\n" +
            "\t\t\t\t\t\t\t\t\"optType\": \"\",\n" +
            "\t\t\t\t\t\t\t\t\"zdy1\": \"\",\n" +
            "\t\t\t\t\t\t\t\t\"zdy2\": \"\",\n" +
            "\t\t\t\t\t\t\t\t\"opt1\": \"\",\n" +
            "\t\t\t\t\t\t\t\t\"opt2\": \"\",\n" +
            "\t\t\t\t\t\t\t\t\"columnDbname\": \"\",\n" +
            "\t\t\t\t\t\t\t\t\"columnType\": \"\",\n" +
            "\t\t\t\t\t\t\t\t\"showType\": \"\",\n" +
            "\t\t\t\t\t\t\t\t\"showName\": \"\",\n" +
            "\t\t\t\t\t\t\t\t\"fixedDownBoxValue\": \"\"\n" +
            "\t\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t\t],\n" +
            "\t\t\t\t\t\t\"nodeUserList\": [\n" +
            "\t\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\t\"targetId\": 85,\n" +
            "\t\t\t\t\t\t\t\t\"type\": 1,\n" +
            "\t\t\t\t\t\t\t\t\"name\": \"天旭\"\n" +
            "\t\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t\t],\n" +
            "\t\t\t\t\t\t\"childNode\": {\n" +
            "\t\t\t\t\t\t\t\"nodeName\": \"路由\",\n" +
            "\t\t\t\t\t\t\t\"type\": 4,\n" +
            "\t\t\t\t\t\t\t\"childNode\": null,\n" +
            "\t\t\t\t\t\t\t\"conditionNodes\": [\n" +
            "\t\t\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\t\t\"nodeName\": \"条件1\",\n" +
            "\t\t\t\t\t\t\t\t\t\"error\": false,\n" +
            "\t\t\t\t\t\t\t\t\t\"type\": 3,\n" +
            "\t\t\t\t\t\t\t\t\t\"priorityLevel\": 1,\n" +
            "\t\t\t\t\t\t\t\t\t\"conditionList\": [\n" +
            "\t\t\t\t\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\"showType\": \"1\",\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\"columnId\": \"1093\",\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\"type\": 2,\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\"showName\": \"园区面积\",\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\"optType\": \"1\",\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\"zdy1\": \"2\",\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\"opt1\": \"<\",\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\"zdy2\": \"\",\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\"opt2\": \"<\",\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\"columnDbname\": \"parkArea\",\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\"columnType\": \"Double\"\n" +
            "\t\t\t\t\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t\t\t\t\t],\n" +
            "\t\t\t\t\t\t\t\t\t\"nodeUserList\": [],\n" +
            "\t\t\t\t\t\t\t\t\t\"childNode\": {\n" +
            "\t\t\t\t\t\t\t\t\t\t\"nodeName\": \"审核人\",\n" +
            "\t\t\t\t\t\t\t\t\t\t\"type\": 1,\n" +
            "\t\t\t\t\t\t\t\t\t\t\"priorityLevel\": 1,\n" +
            "\t\t\t\t\t\t\t\t\t\t\"settype\": 1,\n" +
            "\t\t\t\t\t\t\t\t\t\t\"selectMode\": 0,\n" +
            "\t\t\t\t\t\t\t\t\t\t\"selectRange\": 0,\n" +
            "\t\t\t\t\t\t\t\t\t\t\"examineRoleId\": 0,\n" +
            "\t\t\t\t\t\t\t\t\t\t\"directorLevel\": 1,\n" +
            "\t\t\t\t\t\t\t\t\t\t\"replaceByUp\": 0,\n" +
            "\t\t\t\t\t\t\t\t\t\t\"examineMode\": 1,\n" +
            "\t\t\t\t\t\t\t\t\t\t\"noHanderAction\": 2,\n" +
            "\t\t\t\t\t\t\t\t\t\t\"examineEndType\": 0,\n" +
            "\t\t\t\t\t\t\t\t\t\t\"examineEndRoleId\": 0,\n" +
            "\t\t\t\t\t\t\t\t\t\t\"examineEndDirectorLevel\": 1,\n" +
            "\t\t\t\t\t\t\t\t\t\t\"ccSelfSelectFlag\": 1,\n" +
            "\t\t\t\t\t\t\t\t\t\t\"conditionList\": [],\n" +
            "\t\t\t\t\t\t\t\t\t\t\"nodeUserList\": [\n" +
            "\t\t\t\t\t\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\"targetId\": 2515744,\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\"type\": 1,\n" +
            "\t\t\t\t\t\t\t\t\t\t\t\t\"name\": \"哈哈哈哈\"\n" +
            "\t\t\t\t\t\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t\t\t\t\t\t],\n" +
            "\t\t\t\t\t\t\t\t\t\t\"childNode\": null,\n" +
            "\t\t\t\t\t\t\t\t\t\t\"conditionNodes\": [],\n" +
            "\t\t\t\t\t\t\t\t\t\t\"error\": false\n" +
            "\t\t\t\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t\t\t\t},\n" +
            "\t\t\t\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\t\t\t\"nodeName\": \"条件2\",\n" +
            "\t\t\t\t\t\t\t\t\t\"type\": 3,\n" +
            "\t\t\t\t\t\t\t\t\t\"priorityLevel\": 2,\n" +
            "\t\t\t\t\t\t\t\t\t\"conditionList\": [],\n" +
            "\t\t\t\t\t\t\t\t\t\"nodeUserList\": [],\n" +
            "\t\t\t\t\t\t\t\t\t\"childNode\": null,\n" +
            "\t\t\t\t\t\t\t\t\t\"error\": false\n" +
            "\t\t\t\t\t\t\t\t}\n" +
            "\t\t\t\t\t\t\t]\n" +
            "\t\t\t\t\t\t},\n" +
            "\t\t\t\t\t\t\"conditionNodes\": [],\n" +
            "\t\t\t\t\t\t\"error\": false\n" +
            "\t\t\t\t\t},\n" +
            "\t\t\t\t\t{\n" +
            "\t\t\t\t\t\t\"nodeName\": \"条件2\",\n" +
            "\t\t\t\t\t\t\"type\": 3,\n" +
            "\t\t\t\t\t\t\"priorityLevel\": 2,\n" +
            "\t\t\t\t\t\t\"settype\": 1,\n" +
            "\t\t\t\t\t\t\"selectMode\": 0,\n" +
            "\t\t\t\t\t\t\"selectRange\": 0,\n" +
            "\t\t\t\t\t\t\"examineRoleId\": 0,\n" +
            "\t\t\t\t\t\t\"directorLevel\": 1,\n" +
            "\t\t\t\t\t\t\"replaceByUp\": 0,\n" +
            "\t\t\t\t\t\t\"examineMode\": 1,\n" +
            "\t\t\t\t\t\t\"noHanderAction\": 2,\n" +
            "\t\t\t\t\t\t\"examineEndType\": 0,\n" +
            "\t\t\t\t\t\t\"examineEndRoleId\": 0,\n" +
            "\t\t\t\t\t\t\"examineEndDirectorLevel\": 1,\n" +
            "\t\t\t\t\t\t\"ccSelfSelectFlag\": 1,\n" +
            "\t\t\t\t\t\t\"conditionList\": [],\n" +
            "\t\t\t\t\t\t\"nodeUserList\": [],\n" +
            "\t\t\t\t\t\t\"childNode\": null,\n" +
            "\t\t\t\t\t\t\"conditionNodes\": [],\n" +
            "\t\t\t\t\t\t\"error\": false\n" +
            "\t\t\t\t\t}\n" +
            "\t\t\t\t]\n" +
            "\t\t\t},\n" +
            "\t\t\t\"nodeUserList\": [\n" +
            "\t\t\t\t{\n" +
            "\t\t\t\t\t\"type\": 2,\n" +
            "\t\t\t\t\t\"targetId\": \"4\",\n" +
            "\t\t\t\t\t\"name\": \"销售部\"\n" +
            "\t\t\t\t}\n" +
            "\t\t\t]\n" +
            "\t\t},\n" +
            "\t\t\"conditionNodes\": []\n" +
            "\t}\n" +
            "}";

    public static void main(String[] args) {
//        JSONObject jsonObject = JSON.parseObject(content);
//        String workFlowId = UUIDUTIL.getUUID();
//        if (Objects.nonNull(jsonObject.get("nodeConfig"))) {
//            JSONObject nodeConfig = (JSONObject)jsonObject.get("nodeConfig");
//            if (Objects.nonNull(nodeConfig)) {
//                ChildNode childNode1 = JSONObject.toJavaObject(nodeConfig, ChildNode.class);
//                WorkFlowInstantiate workFlowInstantiate = new WorkFlowInstantiate();
//                workFlowInstantiate.setChildNode(childNode1);
//                try {
//                    ExportOperation.start("com.fenglei.oa.flowable.service.impl.TestServiceImpl", null, workFlowInstantiate, "", null);
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }
//                System.out.println("------>");
//                System.out.println(childNode1);
//            }
//        }
    }
}
