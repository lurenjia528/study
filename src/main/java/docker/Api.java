package docker;

import com.alibaba.fastjson.JSONObject;
import util.SendRequestUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;

/**
 * @author lurenjia
 * @date 2018/10/28
 */
public class Api {
    public static void main(String[] args) throws Exception {
        String url = "";
        Map<String, String> result = null;
        SendRequestUtils sendRequestUtil = new SendRequestUtils();
        JSONObject jo = new JSONObject();

        jo.put("username", "admin");
        jo.put("password", "Harbor12345");
        jo.put("serveraddress", "harbor.ygt.cn");
        String auth = Base64.getEncoder().encodeToString(jo.toJSONString().getBytes("utf-8"));

        // docker images  OK
//        url = "http://harbor.ygt.cn:4243/images/json";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

        //docker load  OK
        // 当前目录下的文件
//        File tar = new File("E:\\softwarelocation\\code\\IdeaProjects\\study\\src\\main\\java\\docker\\lurenjia-busybox-0.0.1.tar");
//        url = "http://harbor.ygt.cn:4243/images/load";
//        result = sendRequestUtil.uploadImage(url, "POST", tar);
//        System.out.println(result);

        //docker tag  OK
//        url = "http://harbor.ygt.cn:4243/images/lurenjia/busybox:0.0.1/tag?repo=harbor.ygt.cn/ygt/busybox&tag=0.0.4";
//        result = sendRequestUtil.send("", url, "POST");
//        System.out.println(result);

        //docker push  OK
//        url = "http://harbor.ygt.cn:4243/images/harbor.ygt.cn/ygt/busybox:0.0.4/push";
//        result = sendRequestUtil.sendUrl(null, url, "POST", auth);
//        System.out.println(result);

        //docker pull  OK
//        url = "http://harbor.ygt.cn:4243/images/create?fromImage=harbor.ygt.cn/test/busybox:0.0.3";
//        result = sendRequestUtil.sendUrl(null, url, "POST", auth);
//        System.out.println(result);

        //docker rmi  OK  http://harbor.ygt.cn:4243/images/harbor.ygt.cn/ygt/busybox:0.0.4
        //docker rmi -f  OK  http://harbor.ygt.cn:4243/images/harbor.ygt.cn/ygt/busybox:0.0.4?force=true
//        url = "http://harbor.ygt.cn:4243/images/harbor.ygt.cn/ygt/busybox:0.0.4";
//        result = sendRequestUtil.send(null, url, "DELETE");
//        System.out.println(result);

        //docker save  OK
        //生成的文件在当前目录，下昂指定路径和文件名
//        url = "http://harbor.ygt.cn:4243/images/harbor.ygt.cn/ygt/busybox:0.0.3/get";
//        result = sendRequestUtil.send(null,url,"GET");
//        String responseData = result.get("responseData");
//        byte[] bytes = responseData.getBytes();
//        getFile(bytes,"E:\\softwarelocation\\code\\IdeaProjects\\study\\src\\main\\java\\docker","lurenjia-busybox-save.tar");
    }

    /**
     * byte[] -> file
     * @param bfile
     * @param filePath
     * @param fileName
     */
    public static void getFile(byte[] bfile, String filePath,String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if(!dir.exists()&&dir.isDirectory()){
                dir.mkdirs();
            }
            file = new File(filePath+"\\"+fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

}
