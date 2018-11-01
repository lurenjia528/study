
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author lurenjia
 * @date 2018/11/1
 */
@SpringBootApplication
@ComponentScan(value = "controller")
public class StudyApplication {
    public static void main(String[] args) {

        SpringApplication.run(StudyApplication.class, args);

    }
}
