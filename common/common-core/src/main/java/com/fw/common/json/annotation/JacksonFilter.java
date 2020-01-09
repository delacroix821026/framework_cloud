package com.fw.common.json.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target(value = { ElementType.METHOD,FIELD})
@Repeatable(value= JacksonFilters.class)
public @interface JacksonFilter {

    Class<?> value();

    /**
     * include为对象需要包含的字段，默认使用include，如果include为空，则使用exclude字段
     */
    String[] include() default {};

    /**
     * exclude 要排除的字段
     */
    String[] exclude() default {};


    JscksonFilterType type() default  JscksonFilterType.RESPONSE;

    enum JscksonFilterType {
        REQUEST, RESPONSE;
    }

}
