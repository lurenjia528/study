package filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: lurenjia
 * Date: 19-8-24
 * Time: 上午9:27
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Resource
    MyInterceptor myInterceptor;

    @Bean
    public FilterRegistrationBean<MyFilter> repeatedlyReadFilter() {
        FilterRegistrationBean<MyFilter> registration = new FilterRegistrationBean<>();
        MyFilter repeatedlyReadFilter = new MyFilter();
        registration.setFilter(repeatedlyReadFilter);
        registration.addUrlPatterns("/*");
        return registration;
    }


    /**
     * 添加静态资源
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    /**
     * 添加拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
        registry.addInterceptor(myInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/api/account/sendLoginSMS")
                .excludePathPatterns("/api/account/login")
                .excludePathPatterns("/api/index/**")
                .excludePathPatterns("/api/public/**");
    }

}
