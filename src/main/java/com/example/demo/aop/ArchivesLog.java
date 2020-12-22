package com.example.demo.aop;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface ArchivesLog {
    /**
     * 操作说明
     */
    public String operteContent() default "";
}
