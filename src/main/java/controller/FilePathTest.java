package controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author lurenjia
 * @date 2018/11/1
 */
@RestController
@RequestMapping("/v1")
public class FilePathTest {

    public static void main(String[] args) {
        File file = new File("E:\\softwarelocation\\code\\IdeaProjects\\study\\src\\main\\java\\json\\example.json");
        //true
        System.out.println(file.exists());
        //E:\softwarelocation\code\IdeaProjects\study\src\main\java\json\example.json
        System.out.println(file.getPath());
        //E:\softwarelocation\code\IdeaProjects\study\src\main\java\json\example.json
        System.out.println(file.getAbsolutePath());
    }

    @GetMapping("/test")
    public void test() {
        File file = new File("src\\main\\java\\json\\example.json");
        //在idea中运行项目     结果： src\main\java\json\example.json
        //java -jar 运行项目  结果： src\main\java\json\example.json
        System.out.println(file.getPath());

        //在idea中运行项目      结果： E:\softwarelocation\code\IdeaProjects\study\src\main\java\json\example.json
        //java -jar 运行项目   结果： E:\softwarelocation\code\IdeaProjects\study\build\libs\src\main\java\json\example.json
        System.out.println(file.getAbsolutePath());
        //在idea中运行项目      结果： true
        //java -jar 运行项目   结果： false
        System.out.println(file.exists());
    }

    @GetMapping("/testJar")
    public void testJar() throws Exception{
        InputStream is = this.getClass().getResourceAsStream("/test.txt");
        BufferedReader br=new BufferedReader(new InputStreamReader(is));
        String s="";
        while((s=br.readLine())!=null)
            //在idea中运行项目      结果： hello world
            //java -jar 运行项目   结果： hello world
            System.out.println(s);
    }
}
