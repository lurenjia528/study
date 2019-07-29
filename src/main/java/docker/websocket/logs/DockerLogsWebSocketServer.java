package docker.websocket.logs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;


/**
 * docker logs -f
 */
@ServerEndpoint("/log/{sid}/{containerId}/{ip}")
@Component
@Slf4j
public class DockerLogsWebSocketServer {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<DockerLogsWebSocketServer> webSocketSet = new CopyOnWriteArraySet<DockerLogsWebSocketServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    //接收sid
    private String sid = "";
    private String cid = "";
    private String ip = "";
    private Socket socket;

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid, @PathParam("containerId") String cid, @PathParam("ip") String ip) throws IOException {
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        log.info("有新窗口开始监听:" + sid + ",当前在线人数为" + getOnlineCount());
        this.sid = sid;
        this.cid = cid;
        this.ip = ip;
        sendMessage("连接成功");
//        socket = connectExec(ip, cid);
        //获得输出
//        getExecMessage(session, ip, cid, socket);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);
        subOnlineCount();
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("收到来自窗口" + sid + "的信息:" + message);
    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     * docker logs -f  前台console.log输出乱码,推测是终端颜色的问题
     */
    public void sendMessage(String message) {
        String url = "http://192.168.40.103:4243/containers/" + cid + "/logs?stdout=true&stderr=true&follow=true";
//        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
        OkHttpClient client = new OkHttpClient.Builder().build();
        RequestBody body = null;
        Request request = new Request.Builder()
                .url(url).method("GET", body)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(response.code());
        InputStream inputStream = response.body().byteStream();
        byte[] buf = new byte[10240];
        // 定义一个StringBuffer用来存放字符串
        StringBuffer sb = new StringBuffer();
        // 开始读取数据
        int len = 0;// 每次读取到的数据的长度
        try {
            while ((len = inputStream.read(buf)) != -1) {// len值为-1时，表示没有数据了
                // append方法往sb对象里面添加数据
                sb.append(new String(buf, 0, len, "utf-8"));
//                System.out.println(sb.toString());
                this.session.getBasicRemote().sendText(sb.toString());
            }
        }catch (Exception e){
            e.printStackTrace();
        }


//        byte[] bytes = new byte[1024];
//        StringBuilder returnMsg = new StringBuilder();
//        try {
//            while (true) {
//                int n = inputStream.read(bytes);
//                String msg = new String(bytes, 0, n);
//                returnMsg.append(msg);
//                bytes = new byte[10240];
//                if (returnMsg.indexOf("\r\n\r\n") != -1) {
//                    session.getBasicRemote().sendText(returnMsg.substring(returnMsg.indexOf("\r\n\r\n") + 4));
//                    break;
//                }
//            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
    }

    private Socket connectExec(String ip, String containerId) throws IOException {
        socket = new Socket(ip, 4243);
        socket.setKeepAlive(true);
        OutputStream out = socket.getOutputStream();
        StringBuilder pw = new StringBuilder();
        pw.append("GET /container/").append(containerId).append("/logs?stdout=true&follow=true HTTP/1.1\r\n");
        pw.append("Host: ").append(ip).append(":4243\r\n");
        pw.append("User-Agent: Docker-Client\r\n");
        pw.append("Content-Type: application/json\r\n");
//        pw.append("Connection: Upgrade\r\n");
//        JSONObject obj = new JSONObject();
//        obj.put("Detach", false);
//        obj.put("Tty", true);
//        String json = obj.toJSONString();
//        pw.append("Content-Length: ").append(json.length()).append("\r\n");
//        pw.append("Upgrade: tcp\r\n");
        pw.append("\r\n");
//        pw.append(json);
        out.write(pw.toString().getBytes(StandardCharsets.UTF_8));
        out.flush();
        return socket;
    }
//    private void getExecMessage(Session session, String ip, String containerId, Socket socket) throws IOException {
//        InputStream inputStream = socket.getInputStream();
//        byte[] bytes = new byte[1024];
//        StringBuilder returnMsg = new StringBuilder();
//        while (true) {
//            int n = inputStream.read(bytes);
//            String msg = new String(bytes, 0, n);
//            returnMsg.append(msg);
//            bytes = new byte[10240];
//            if (returnMsg.indexOf("\r\n\r\n") != -1) {
//                session.getBasicRemote().sendText(returnMsg.substring(returnMsg.indexOf("\r\n\r\n") + 4));
//                break;
//            }
//        }
////        Thread thread = new Thread(() -> {
////            try {
////                byte[] bytes1 = new byte[1024];
////                while (!Thread.interrupted()) {
////                    int n = inputStream.read(bytes1);
////                    String msg = new String(bytes1, 0, n);
//////                    if (msg.contains("stat /bin/bash: no such file or directory")) {
//////                        continue;
//////                    }
////                    session.getBasicRemote().sendText(msg);
////                    bytes1 = new byte[1024];
////                }
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        });
////        thread.start();
//    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        DockerLogsWebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        DockerLogsWebSocketServer.onlineCount--;
    }
}
