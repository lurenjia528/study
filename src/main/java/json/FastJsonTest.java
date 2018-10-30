package json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import static util.FileUtil.readToBuffer;

/**
 * @author lurenjia
 * @date 2018/10/30
 */
public class FastJsonTest {
    public static void main(String[] args) throws Exception {
        long l1 = System.currentTimeMillis();
        String s = readToBuffer("src/main/java/json/example1.json");
        JSONObject parse = JSONObject.parseObject(s);
        JSONArray users = parse.getJSONArray("users");
        JSONObject jsonObject = users.getJSONObject(0);
        int tags = jsonObject.getJSONArray("tags").size();
        System.out.println(tags);
        long l2 = System.currentTimeMillis();
        System.out.println(l2 - l1);
    }
}
