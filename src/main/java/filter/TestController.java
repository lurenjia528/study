package filter;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 * User: lurenjia
 * Date: 19-8-24
 * Time: 上午9:19
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @PostMapping("/pt")
    public void test(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        System.out.println("---------controller----------");
        System.out.println(jsonObject);
        String user = request.getParameter("user");
        System.out.println("path:" + user);
        String user1 = request.getHeader("user");
        System.out.println("header:" + user1);
        System.out.println("---------controller----------");
    }
}
