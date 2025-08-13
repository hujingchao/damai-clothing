package com.fenglei.model.annotation;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD}) //注解应用类型
@Retention(RetentionPolicy.RUNTIME) // 注解的类型
public @interface FieldInterface {
    String name() default "";
}
