package com.fenglei.quartz.util;

import com.alibaba.fastjson.JSONObject;
import com.fenglei.common.util.SpringUtils;
import com.fenglei.common.util.StringUtils;
import com.fenglei.model.quartz.entity.SysJob;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 任务执行工具
 *
 * @author ruoyi
 */
public class JobInvokeUtil {
    /**
     * 执行方法
     *
     * @param sysJob 系统任务
     */
    public static void invokeMethod(SysJob sysJob) throws Exception {
        String invokeTarget = sysJob.getInvokeTarget();
        String beanName = getBeanName(invokeTarget);
        String methodName = getMethodName(invokeTarget);
        List<Object[]> methodParams = getMethodParams(invokeTarget);

        if (!isValidClassName(beanName)) {
            Object bean = SpringUtils.getBean(beanName).getClass();
            invokeMethod(bean, methodName, methodParams);
        } else {
            Object bean = Class.forName(beanName);
            invokeMethod(bean, methodName, methodParams);
        }
    }

    /**
     * 调用任务方法
     *
     * @param bean         目标对象
     * @param methodName   方法名称
     * @param methodParams 方法参数
     */
    private static void invokeMethod(Object bean, String methodName, List<Object[]> methodParams)
            throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {
        if (StringUtils.isNotNull(methodParams) && methodParams.size() > 0) {
            Class<?> aClass = (Class<?>)bean;
            Method method = aClass.getMethod(methodName, getMethodParamsType(methodParams));
            method.invoke(SpringUtils.getClassBean(aClass), getMethodParamsValue(methodParams));
        } else {
            Class<?> aClass = (Class<?>)bean;
            Method method = aClass.getMethod(methodName);
            method.invoke(SpringUtils.getClassBean(aClass));
        }
    }

    /**
     * 校验是否为为class包名
     *
     * @param invokeTarget 名称
     * @return true是 false否
     */
    public static boolean isValidClassName(String invokeTarget) {
        return StringUtils.countMatches(invokeTarget, ".") > 1;
    }

    /**
     * 获取bean名称
     *
     * @param invokeTarget 目标字符串
     * @return bean名称
     */
    public static String getBeanName(String invokeTarget) {
        String beanName = StringUtils.substringBefore(invokeTarget, "(");
        return StringUtils.substringBeforeLast(beanName, ".");
    }

    /**
     * 获取bean方法
     *
     * @param invokeTarget 目标字符串
     * @return method方法
     */
    public static String getMethodName(String invokeTarget) {
        String methodName = StringUtils.substringBefore(invokeTarget, "(");
        return StringUtils.substringAfterLast(methodName, ".");
    }

    /**
     * 获取method方法参数相关列表
     *
     * @param invokeTarget 目标字符串
     * @return method方法相关参数列表
     */
    public static List<Object[]> getMethodParams(String invokeTarget) {
        String methodStr = StringUtils.substringBetween(invokeTarget, "(", ")");
        if (StringUtils.isEmpty(methodStr)) {
            return null;
        }
//        String[] methodParams = methodStr.split(",(?=([^\"']*[\"'][^\"']*[\"'])*[^\"']*$)");
        String[] methodParams = methodStr.split(",(?!([\"]))");
        List<Object[]> classs = new LinkedList<>();
        for (int i = 0; i < methodParams.length; i++) {
            String str = StringUtils.trimToEmpty(methodParams[i]);
            // 对象json,格式"{"jobId":1,"jobName":"名称"}(com.fenglei.model.quartz.entity.SysJob)"
            if (match("^\\{.*\\}\\(.*\\)$",str)) {
                String objectJson=StringUtils.substring(str, str.indexOf("{"), str.lastIndexOf("}")+1);

                String objectFullPath=StringUtils.substring(str, str.indexOf("(")+1, str.lastIndexOf(")"));
                try {
                    Class<?> objectClass  = Class.forName(objectFullPath);
                    System.out.println(objectJson);
                    System.out.println(objectFullPath);

                    classs.add(new Object[]{JSONObject.parseObject(objectJson,objectClass), objectClass});
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    throw new IllegalArgumentException("参数异常");
                }


            }
            // String字符串类型，以'或"开头
            else if (StringUtils.startsWithAny(str, "'", "\"")) {
                classs.add(new Object[]{StringUtils.substring(str, 1, str.length() - 1), String.class});
            }
            else if (StringUtils.endsWith(str, "-V")) {
                classs.add(new Object[]{StringUtils.substring(str, 0, str.length() - 2), String.class});
            }
            // boolean布尔类型，等于true或者false
            else if ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str)) {
                classs.add(new Object[]{Boolean.valueOf(str), Boolean.class});
            }
            // long长整形，以L结尾
            else if (StringUtils.endsWith(str, "L")) {
                classs.add(new Object[]{Long.valueOf(StringUtils.substring(str, 0, str.length() - 1)), Long.class});
            }
            // LocalDateTime，以LDT结尾
            else if (StringUtils.endsWith(str, "LDT")) {
                classs.add(new Object[]{LocalDateTime.parse(StringUtils.substring(str, 0, str.length() - 3)), LocalDateTime.class});
            }
            // LocalDate类型，以LD结尾
            else if (StringUtils.endsWith(str, "LD")) {
                classs.add(new Object[]{LocalDate.parse(StringUtils.substring(str, 0, str.length() - 2)), LocalDate.class});
            }
            // double浮点类型，以D结尾
            else if (StringUtils.endsWith(str, "D")) {
                classs.add(new Object[]{Double.valueOf(StringUtils.substring(str, 0, str.length() - 1)), Double.class});
            }
            // BigDecimal，以B结尾
            else if (StringUtils.endsWith(str, "B")) {
                classs.add(new Object[]{new BigDecimal(StringUtils.substring(str, 0, str.length() - 1)), BigDecimal.class});
            }
            // 其他类型归类为整形
            else {
                classs.add(new Object[]{Integer.valueOf(str), Integer.class});
            }
        }
        return classs;
    }


    /**
     * 获取参数类型
     *
     * @param methodParams 参数相关列表
     * @return 参数类型列表
     */
    public static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
        Class<?>[] classs = new Class<?>[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classs[index] = (Class<?>) os[1];
            index++;
        }
        return classs;
    }

    /**
     * 获取参数值
     *
     * @param methodParams 参数相关列表
     * @return 参数值列表
     */
    public static Object[] getMethodParamsValue(List<Object[]> methodParams) {
        Object[] classs = new Object[methodParams.size()];
        int index = 0;
        for (Object[] os : methodParams) {
            classs[index] = (Object) os[0];
            index++;
        }
        return classs;
    }


    /**
     * @param regex
     * 正则表达式字符串
     * @param str
     * 要匹配的字符串
     * @return 如果str 符合 regex的正则表达式格式,返回true, 否则返回 false;
     */
    private static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
