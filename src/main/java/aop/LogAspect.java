package aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author yanggt
 * @date 19-8-26
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    //设置切入点：这里直接拦截被@RestController注解的类
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void pointcut() {

    }

    /**
     * 切面方法,记录日志
     *
     * @return
     * @throws Throwable
     */
    @Around("pointcut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        //利用RequestContextHolder获取requst对象
        ServletRequestAttributes requestAttr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        String uri = requestAttr.getRequest().getRequestURI();


        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("暂不支持非方法注解");
        }
        //调用实际方法
        Object object = joinPoint.proceed();
        log.info("返回值{}", object);
//访问目标方法的参数 可动态改变参数值
        Object[] args = joinPoint.getArgs();
        log.info("请求方法：{}, 请求参数: {}", joinPoint.getSignature().getName(), Arrays.toString(args));
        Class<?>[] classes = null;
        if (null == args) {

        } else {
            classes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                classes[i] = args[i].getClass();
            }
        }

        //获取执行的方法
        long l1 = System.nanoTime();
        MethodSignature methodSign = (MethodSignature) signature;
        Method method = methodSign.getMethod();
        Log logAnno = AnnotationUtils.getAnnotation(method, Log.class);
        if (logAnno != null && logAnno.ignore()) {
            return object;
        }
        //判断是否包含了 无需记录日志的方法
        log.info("方法:AnnotationUtils.getAnnotation");
        log.warn("log注解值value：{}", logAnno.value());
        long l2 = System.nanoTime();
        log.error("时间:{}", l2 - l1);

        l1 = System.nanoTime();
        Method method1 = joinPoint.getTarget().getClass().getMethod(signature.getName(), classes);
        Log audit = method1.getAnnotation(Log.class);
        if (audit != null && audit.ignore()) {
            return object;
        }
        log.info("方法:joinPoint.getTarget().getClass().getMethod");
        log.warn("log注解值value：{}", audit.value());
        l2 = System.nanoTime();
        log.error("时间:{}", l2-l1);

        l1 = System.nanoTime();
        Method method2 = joinPoint.getSignature().getDeclaringType().getMethod(signature.getName(), classes);
        Log audit3 = method2.getAnnotation(Log.class);
        if (audit3 != null && audit3.ignore()) {
            return object;
        }
        log.info("方法:joinPoint.getSignature().getDeclaringType().getMethod");
        log.warn("log注解值value：{}", audit3.value());
        l2 = System.nanoTime();
        log.error("时间:{}", l2-l1);

        l1 = System.nanoTime();
        Method method3 = Class.forName(signature.getDeclaringTypeName()).getMethod(signature.getName(), classes);
        Log audit4 = method3.getAnnotation(Log.class);
        if (audit4 != null && audit4.ignore()) {
            return object;
        }
        log.info("方法:Class.forName(signature.getDeclaringTypeName()).getMethod");
        log.warn("log注解值value：{}", audit4.value());
        l2 = System.nanoTime();
        log.error("时间:{}", l2 - l1);


        log.info("---------------------------------");
        log.info("综合来看");
        log.info("joinPoint.getSignature().getDeclaringType().getMethod");
        log.info("效率最快,而且能获取到改变之后的注解值");
        log.info("---------------------------------");
        //模拟异常
        //System.out.println(1/0);
        return object;
    }

    /**
     * 指定拦截器规则；也可直接使用within(@org.springframework.web.bind.annotation.RestController *)
     * 这样简单点 可以通用
     *
     * @param e 异常对象
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void afterThrowable(Throwable e) {
        log.error("切面发生了异常：", e);
        //这里可以做个统一异常处理
        //自定义一个异常 包装后排除
        //throw new AopException("xxx);
    }

    /**
     * 在存在代理的情况下,获取远程ip
     *
     * @param request 请求
     * @return ip
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            return ip.split(",")[0];
        } else {
            return ip;
        }
    }
}
