package com.fenglei.service.workFlow.util;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.math.BigDecimal;

/**
 * @author yzy
 */
public class FormulaCalculationUtil {

    public static Boolean logicOperation(String content) throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        System.out.println(content);
        return (Boolean) engine.eval(content);
    }

    public static BigDecimal mathematicsOperation(String content) throws Exception {
        Object object = JepUtil.direct(content);
        if (object != null) {
            return new BigDecimal(object.toString());
        }
        return null;
    }

}
