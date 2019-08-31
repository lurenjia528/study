package aop;

import java.lang.annotation.*;

/**
 * @author yanggt
 * @date 19-8-26
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})//只能在方法上使用此注解
public @interface Log {
    /**
     * 日志描述，这里使用了@AliasFor 别名。spring提供的
     * @return
     */
    String value() default "";

    /**
     * 日志描述
     * @return
     */
    String desc() default "";

    /**
     * 是否不记录日志
     * @return
     */
    boolean ignore() default false;
}
