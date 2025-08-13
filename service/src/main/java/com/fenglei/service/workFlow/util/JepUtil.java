package com.fenglei.service.workFlow.util;


import com.fenglei.common.exception.BizException;
import org.nfunk.jep.JEP;

import java.util.ArrayList;
import java.util.List;

/**
 * User: yzy
 * Date: 2019/6/28 0028
 * Time: 11:21
 * Description: No Description
 * @author yzy
 */
public class JepUtil {

    public static Object operation(String exe, String variable,Object number) throws Exception{
        JEP jep= new JEP();
        // 给变量赋值
        jep.addVariableAsObject(variable, number);
        // 运算
        jep.parseExpression(exe);
        // 得出结果
        return jep.getValueAsObject();
    }

    public static Object direct(String formula) {
        JEP jep= new JEP();
        // 运算
        jep.parseExpression(formula);
        // 得出结果
        return jep.getValueAsObject();
    }

    public static Boolean toBoolean(Object object) throws Exception{
        if (object == null) {
            return false;
        }
        if (Double.parseDouble(object.toString()) == 1) {
            return true;
        } else if (Double.parseDouble(object.toString()) == 0){
            return false;
        } else {

            throw new BizException("jep conversion error");
        }
    }

    /**
     * 提取两个符号中内容，忽略两个符号中的相同符号
     * @param str 公式内容
     * @return List<String>
     */
    public static List<String> extractString(String str, char symbol1, char symbol2) {

        List<String> list = new ArrayList<>();
        int start = 0;
        int startFlag = 0;
        int endFlag = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == symbol1) {
                startFlag++;
                if (startFlag == endFlag + 1) {
                    start = i;
                }
            } else if (str.charAt(i) == symbol2) {
                endFlag++;
                if (endFlag == startFlag) {
                    list.add(str.substring(start + 1, i));
                }
            }
        }
        return list;
    }

}
