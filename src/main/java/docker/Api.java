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
    static String url = "";
    static String str = "";
    static Map<String, String> result = null;
    static SendRequestUtils sendRequestUtil = new SendRequestUtils();
    static JSONObject jo = new JSONObject();

    public static void main(String[] args) throws Exception {
//        system();
//        image();
//        container();
//        network();
//        volumes();
    }

    /**
     * 存储卷操作
     */
    public static void volumes(){
        // docker volume ls
//        url = "http://harbor.ygt.cn:4243/volumes";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

        // docker docker volume inspect  volumeId
//        url = "http://harbor.ygt.cn:4243/volumes/628bc05fb2c4b2242a979e4c42b0df9738e625ea63c5e6f63379ee2549c0a8a1";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

        // docker volume create
//        url = "http://harbor.ygt.cn:4243/volumes/create";
//        result = sendRequestUtil.send(null, url, "POST");
//        System.out.println(result);

        // docker volume rm volumeId
//        url = "http://harbor.ygt.cn:4243/volumes/628bc05fb2c4b2242a979e4c42b0df9738e625ea63c5e6f63379ee2549c0a8a1";
//        result = sendRequestUtil.send(null, url, "DELETE");
//        System.out.println(result);

        // docker volume prune
//        url = "http://harbor.ygt.cn:4243/volumes/prune";
//        result = sendRequestUtil.send(null, url, "POST");
//        System.out.println(result);
    }

    /**
     * 网络操作
     */
    public static void network() {
        // docker network ls
//        url = "http://harbor.ygt.cn:4243/networks";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

        // docker inspect networkId
//        url = "http://harbor.ygt.cn:4243/networks/655e514df362c18a083a1d0b88b1a0c4cc2e210ab716fd636d5aa030453b6e51";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

        // docker network rm networkId
//        url = "http://harbor.ygt.cn:4243/networks/93ca51b4c470f8f2d2ea2ccda65ad28d13499ea3d4fe3858744c236440ffa5d2";
//        result = sendRequestUtil.send(null, url, "DELETE");
//        System.out.println(result);

        // docker network create networkName
//        url = "http://harbor.ygt.cn:4243/networks/create";
//        jo.put("Name","ygt-nw");
//        result = sendRequestUtil.send(jo.toJSONString(), url, "POST");
//        System.out.println(result);

        // docker network prune
//        url = "http://harbor.ygt.cn:4243/networks/prune";
//        result = sendRequestUtil.send(null, url, "POST");
//        System.out.println(result);

        // docker network connect networkId containerId
//        url = "http://harbor.ygt.cn:4243/networks/0663a62f13a3f86be6a164ef723cf4a375a598160e5582213bbf69580b111163/connect";
//        str = "{\n" +
//               "  \"Container\": \"5b1ff7369f1d\",\n" +
//               "  \"EndpointConfig\": {\n" +
//               "    \"IPAMConfig\": {\n" +
//               "    }\n" +
//               "  }\n" +
//               "}";
//        result = sendRequestUtil.send(str, url, "POST");
//        System.out.println(result);

        // docker network disconnect networkId containerId
//        url = "http://harbor.ygt.cn:4243/networks/0663a62f13a3f86be6a164ef723cf4a375a598160e5582213bbf69580b111163/disconnect";
//        jo.put("Container","5b1ff7369f1d");
//        jo.put("Force",true);
//        result = sendRequestUtil.send(jo.toJSONString(), url, "POST");
//        System.out.println(result);
    }

    /**
     * 容器操作
     */
    public static void container() {
        // docker ps
        // docker ps -a
        //  docker ps -f status=exited
//        url = "http://harbor.ygt.cn:4243/containers/json";
//        url = "http://harbor.ygt.cn:4243/containers/json?all=true";
//        url = "http://harbor.ygt.cn:4243/containers/json?filters={\"status\": [\"exited\"]}";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

        // docker inspect containerId
//        url = "http://harbor.ygt.cn:4243/containers/fab84b3e3e75/json";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

        // 显示容器内的进程
        // docker top containerId
//        url = "http://harbor.ygt.cn:4243/containers/fab84b3e3e75/top";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

        // docker restart containerId
//        url = "http://harbor.ygt.cn:4243/containers/fab84b3e3e75/restart";
//        result = sendRequestUtil.send(null, url, "POST");
//        System.out.println(result);

        // docker stop containerId
//        url = "http://harbor.ygt.cn:4243/containers/fab84b3e3e75/stop";
//        result = sendRequestUtil.send(null, url, "POST");
//        System.out.println(result);

        // docker kill containerId
//        url = "http://harbor.ygt.cn:4243/containers/fab84b3e3e75/kill";
//        result = sendRequestUtil.send(null, url, "POST");
//        System.out.println(result);

        // docker [container] diff containerId
//        url = "http://harbor.ygt.cn:4243/containers/5b1ff7369f1d/changes";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

//        url = "http://harbor.ygt.cn:4243/containers/5b1ff7369f1d/archive?path=/root";
//        result = sendRequestUtil.send(null, url, "HEAD");
//        System.out.println(result);

//        url = "http://harbor.ygt.cn:4243/containers/5b1ff7369f1d/archive?path=/root";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

        // docker container prune
//        url = "http://harbor.ygt.cn:4243/containers/prune";
//        result = sendRequestUtil.send(null, url, "POST");
//        System.out.println(result);

        // docker logs containerId
//        url = "http://harbor.ygt.cn:4243/containers/5b1ff7369f1d/logs?stdout=true";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

        // 向容器发送命令,接收返回
        // docker exec -it 5b1ff7369f1d ps -ef
//        url = "http://harbor.ygt.cn:4243/containers/5b1ff7369f1d/exec";
//        jo.put("AttachStdin", false);
//        jo.put("AttachStdout", true);
//        jo.put("AttachStderr", true);
//        jo.put("Tty", false);
//        jo.put("Cmd", new String[]{"ps","-ef"});
//        result = sendRequestUtil.send(jo.toJSONString(), url, "POST");
//        System.out.println(result);//{responseData={"Id":"f42352076f5e59937b90edb773253262c8e99c673364df105f831590c7b7b3b7"},responseCode=201}
//        String id = JSONObject.parseObject(result.get("responseData")).getString("Id");
//        url = "http://harbor.ygt.cn:4243/exec/" + id + "/start";
//        jo.put("Detach", false);
//        jo.put("Tty", false);
//        result = sendRequestUtil.send(jo.toJSONString(), url, "POST");
//        System.out.println(result);

        // docker attach containerId
        // todo error
//        url = "http://harbor.ygt.cn:4243/containers/5b1ff7369f1d/attach?logs=true&stdout=true&stream=true HTTP/1.1";
//        result = sendRequestUtil.send(null, url, "POST");
//        System.out.println(result);

    }

    /**
     * 镜像操作
     */
    public static void image() throws Exception {

        jo.put("username", "admin");
        jo.put("password", "Harbor12345");
        jo.put("serveraddress", "harbor.ygt.cn");
        String auth = Base64.getEncoder().encodeToString(jo.toJSONString().getBytes("utf-8"));

        // docker images  OK
//        url = "http://harbor.ygt.cn:4243/images/json";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

//        // docker inspect imageId
//        url = "http://harbor.ygt.cn:4243/images/busybox:latest/json";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

        // docker history
//        url = "http://harbor.ygt.cn:4243/images/busybox:latest/history";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

        // docker search
//        url = "http://harbor.ygt.cn:4243/images/search?term=istio";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

        //docker load  OK
        // 当前目录下的文件
//        File tar = new File("E:\\softwarelocation\\code\\IdeaProjects\\study\\src\\WebSocketClient\\java\\docker\\lurenjia-busybox-0.0.1.tar");
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
        //生成的文件在当前目录，下指定路径和文件名
//        url = "http://harbor.ygt.cn:4243/images/harbor.ygt.cn/ygt/busybox:0.0.3/get";
//        result = sendRequestUtil.send(null,url,"GET");
//        String responseData = result.get("responseData");
//        byte[] bytes = responseData.getBytes();
//        getFile(bytes,"E:\\softwarelocation\\code\\IdeaProjects\\study\\src\\WebSocketClient\\java\\docker","lurenjia-busybox-save.tar");

    }

    /**
     * 系统信息
     */
    public static void system() {
        // docker version
//        url = "http://harbor.ygt.cn:4243/version";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

        // docker info
//        url = "http://harbor.ygt.cn:4243/info";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

//        url = "http://harbor.ygt.cn:4243/_ping";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

        // docker events
//        url = "http://harbor.ygt.cn:4243/events";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

        // docker system df
//        url = "http://harbor.ygt.cn:4243/system/df";
//        result = sendRequestUtil.send(null, url, "GET");
//        System.out.println(result);

    }

    /**
     * byte[] -> file
     *
     */
    public static void getFile(byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }
            file = new File(filePath + "\\" + fileName);
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
