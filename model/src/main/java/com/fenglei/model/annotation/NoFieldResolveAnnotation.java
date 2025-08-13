package com.fenglei.model.annotation;

import java.lang.annotation.*;

/**
 * 字段解释注解
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NoFieldResolveAnnotation {
    String value() default "";
}
