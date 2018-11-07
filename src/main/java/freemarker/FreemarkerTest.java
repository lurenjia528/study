package freemarker;

import freemarker.template.*;
import util.FreemarkerUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lurenjia
 * @date 2018/11/7
 */
public class FreemarkerTest implements TemplateMethodModelEx {

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public Object exec(List args) throws TemplateModelException {
        if (args.size() != 1) {
            throw new TemplateModelException("Wrong arguments");
        }
        String fileOrPath = args.get(0).toString();
        File file = new File(fileOrPath);
        return file.isDirectory();
    }

    public static void main(String[] args) {
        Map<String, Object> map = new HashMap<>();
        map.put("isDirectory", new FreemarkerTest());
        String s = FreemarkerUtils.convertFtl(map, "test.ftl");
        System.out.println(s);

    }
}
