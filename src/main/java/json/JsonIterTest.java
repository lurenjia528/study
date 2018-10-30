package json;

import com.jsoniter.JsonIterator;
import com.jsoniter.any.Any;

import java.io.*;
import java.util.List;
import java.util.Map;

import static util.FileUtil.readToBuffer;

/**
 * @author lurenjia
 * @date 2018/10/30
 */
public class JsonIterTest {
    public static void main(String[] args) throws Exception {
//        test();

        String s = readToBuffer("src/main/java/json/example.json");
        Any deserialize = JsonIterator.deserialize(s);
        List<Any> anies = deserialize.asList();
        Map<String, Any> stringAnyMap = anies.get(0).asMap();
        String balance = stringAnyMap.get("balance").toString();
        System.out.println("balance:" + balance);
        String name = stringAnyMap.get("friends").asList().get(1).asMap().get("name").toString();
        System.out.println("name:" + name);

        long l1 = System.currentTimeMillis();
        String s1 = readToBuffer("src/main/java/json/example1.json");
        JsonIterator parse = JsonIterator.parse(s1);
        int calc = calc(parse);
        long l2 = System.currentTimeMillis();
        System.out.println(calc);
        System.out.println(l2-l1);
    }


    public static void test() {
        String input = "{'numbers': ['1', '2', ['3', '4']]}".replace('\'', '"');
        //获取数组的第三个元素
        String[] array = JsonIterator.deserialize(input).get("numbers", 2).as(String[].class);
        for (String str : array) {
            System.out.println(str);
        }

        int value = JsonIterator.deserialize(input).toInt("numbers", 2, 0); // value is 3, converted from string
        System.out.println(value);
    }



    public static int calc(JsonIterator iter) throws IOException {
        int totalTagsCount = 0;
        for (String field = iter.readObject(); field != null; field = iter.readObject()) {
            switch (field) {
                case "users":
                    while (iter.readArray()) {
                        for (String field2 = iter.readObject(); field2 != null; field2 = iter.readObject()) {
                            switch (field2) {
                                case "tags":
                                    while (iter.readArray()) {
                                        iter.skip();
                                        totalTagsCount++;
                                    }
                                    break;
                                default:
                                    iter.skip();
                            }
                        }
                    }
                    break;
                default:
                    iter.skip();
            }
        }
        return totalTagsCount;
    }
}
