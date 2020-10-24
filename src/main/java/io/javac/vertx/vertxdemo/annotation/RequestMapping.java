package io.javac.vertx.vertxdemo.annotation;

import io.javac.vertx.vertxdemo.enums.RequestMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {

    /**
     * api path
     *
     * @return
    */
    String[] value() default {};

    /**
     * request method
     *
     * @return
    */
    RequestMethod method() default RequestMethod.GET;
}