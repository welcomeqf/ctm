package eqlee.ctm.user.jwt.islogin;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author qf
 * @Date 2019/9/24
 * @Version 1.0
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckToken {
    boolean required() default true;
}
