package com.fenglei.service.workFlow.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yzy
 */
public class ClassConversionTools {

    /**
     * 转换为列表
     * @param obj 列表原数据
     * @param clazz 类型
     * @param <T> 占位符
     * @return List<T>
     */
    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        if(obj instanceof List<?>)
        {
            for (Object o : (List<?>) obj)
            {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return null;
    }

}
