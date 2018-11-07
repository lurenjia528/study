package util;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.StringWriter;


/**
 * @author yanggt
 */
public class FreemarkerUtils {

    public static String convertFtl(Object jsonObject, String ftl) {
        //创建Freemarker配置实例
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_25);
        cfg.setClassForTemplateLoading(FreemarkerUtils.class, "/templates");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setNumberFormat("#");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        Template template ;
        StringWriter sw = new StringWriter();
        try {
            template = cfg.getTemplate(ftl);
            template.process(jsonObject, sw);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if(null != sw){
                    sw.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sw.toString();
    }
}
