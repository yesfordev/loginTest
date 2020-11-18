package me.yes.practice.utils.auth;

import java.lang.annotation.*;

/**
 * Created by yes on 2020/11/18
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Auth {
}
