package com.fenglei.service.system.aspect;


import com.alibaba.fastjson.JSONObject;
import com.fenglei.common.annotation.SysLogAnnotation;
import com.fenglei.common.util.StringUtils;
import com.fenglei.model.annotation.NoFieldResolveAnnotation;
import com.fenglei.model.system.entity.SysLog;
import com.fenglei.service.system.ISysLogService;
import io.swagger.annotations.ApiModelProperty;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统日志切面
 */
@Aspect  // 使用@Aspect注解声明一个切面
@Component
public class SysLogAspect {

    @Autowired
    private ISysLogService sysLogService;

    /**
     * 这里我们使用注解的形式
     * 当然，我们也可以通过切点表达式直接指定需要拦截的package,需要拦截的class 以及 method
     * 切点表达式:   execution(...)
     */
    @Pointcut("@annotation(com.fenglei.common.annotation.SysLogAnnotation)")
    public void logPointCut() {
    }

    /**
     * 环绕通知 @Around  ， 当然也可以使用 @Before (前置通知)  @After (后置通知)
     *
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("logPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object result = point.proceed();
        long time = System.currentTimeMillis() - beginTime;
        try {
            saveLog(point, time);
        } catch (Exception e) {
        }
        return result;
    }

    /**
     * 保存日志
     *
     * @param joinPoint
     * @param time
     */
    private void saveLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class[] parameterTypes = signature.getParameterTypes();
        Map<String, String> fieldChinese = new HashMap<>();
        for (Class parameterType : parameterTypes) {
            for (Field declaredField : parameterType.getDeclaredFields()) {
                ApiModelProperty annotation = declaredField.getAnnotation(ApiModelProperty.class);
                NoFieldResolveAnnotation noFieldResolveAnnotation = declaredField.getAnnotation(NoFieldResolveAnnotation.class);
                if (annotation != null && noFieldResolveAnnotation == null) {
                    fieldChinese.put(parameterType.getName() + "@" + declaredField.getName(), annotation.value());
                }
            }
        }
        SysLog sysLog = new SysLog();
        sysLog.setExeuTime(time);
        SysLogAnnotation sysLogAnnotation = method.getAnnotation(SysLogAnnotation.class);
        if (sysLogAnnotation != null) {
            //注解上的描述
            sysLog.setRemark(sysLogAnnotation.value());
        }
        //请求的 类名、方法名
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        sysLog.setClassName(className);
        sysLog.setMethodName(methodName);
        //请求的参数
        Object[] args = joinPoint.getArgs();
        try {
            List<String> list = new ArrayList<>();
            for (Object o : args) {
                JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(o));
                Class<?> aClass = o.getClass();
                for (String fieldKey : fieldChinese.keySet()) {
                    String[] split = fieldKey.split("@");
                    if (split[0].equals(aClass.getName())) {
                        String chinese = fieldChinese.get(fieldKey);
                        getValue(jsonObject, split[1], chinese);
                    }
                }
                list.add(jsonObject.toJSONString());
            }
            sysLog.setParams(list.toString());
        } catch (Exception e) {
        }
        sysLogService.add(sysLog);
    }

    private void getValue(JSONObject jsonObject, String key, String chineseKey) {
        String value = jsonObject.getString(key);
        if (StringUtils.isNotEmpty(value)) {
            jsonObject.put(chineseKey, value);
            jsonObject.remove(key);
        }
    }
}
