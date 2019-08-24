package filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created with IntelliJ IDEA.
 * User: lurenjia
 * Date: 19-8-24
 * Time: 上午9:16
 */
@Component
public class MyInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("---------------拦截器开始------------------");
        try {
            String requestMethod = request.getRequestURI();//请求方法
            if (requestMethod == null) {
                return false;
            }
            String user = request.getParameter("user");
            System.out.println("path:" + user);
            String user1 = request.getHeader("user");
            System.out.println("header:" + user1);
            //获取请求参数
//            JSONObject parameterMap = JSON.parseObject(new BodyReaderHttpServletRequestWrapper(request).getBodyString(request));
            JSONObject parameterMap = JSON.parseObject(new RequestWrapper(request).getBody());
            System.out.println(parameterMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("拦截器拦截完成");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        System.out.println("---------------拦截器方法二开始------------------");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        System.out.println("---------------拦截器方法三开始------------------");
    }
}
