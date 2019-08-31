package aop;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author yanggt
 * @date 19-8-26
 */
@RestController
@RequestMapping("/aop")
public class DemoController {
    /**
     * 简单方法示例
     *
     * @param hello
     * @return
     */
    @GetMapping("/aop/{test}")
    @Log(value = "请求了aopDemo方法", desc = "描述")
    public String aopDemo(@PathVariable(value = "test") String hello) {
        try {
            Method method = DemoController.class.getMethod("aopDemo", String.class);
            Log audit = method.getAnnotation(Log.class);
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(audit);
            Field value = invocationHandler.getClass().getDeclaredField("memberValues");
            value.setAccessible(true);
            Map<String, Object> memberValues = (Map<String, Object>) value.get(invocationHandler);
            //向注解的属性中赋值
            memberValues.put("value", "测试值hello world");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "请求参数为：" + hello;
    }

    /**
     * 不拦截日志示例
     *
     * @param hello
     * @return
     */
    @RequestMapping("/notaop")
    @Log(ignore = true)
    public String notAopDemo(String hello) {
        return "此方法不记录日志，请求参数为：" + hello;
    }
}
