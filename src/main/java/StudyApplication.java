
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 * @author lurenjia
 * @date 2018/11/1
 */
@SpringBootApplication
//@ComponentScan(value = {"controller", "docker.websocket"})
//@ComponentScan(value = {"filter"})
@ComponentScan(value = {"aop"})
//@EnableWebSocket
//@EnableAspectJAutoProxy
public class StudyApplication {
    public static void main(String[] args) {

        SpringApplication.run(StudyApplication.class, args);

    }
}
