package docker.websocket;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.TimeUnit;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Component;


/**
 * docker logs -f
 */
@ServerEndpoint("/log/{sid}/{containerId}")
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

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid,@PathParam("containerId") String cid) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        log.info("有新窗口开始监听:" + sid + ",当前在线人数为" + getOnlineCount());
        this.sid = sid;
        this.cid = cid;
        try {
            sendMessage("连接成功");
        } catch (IOException e) {
            log.error("docker.websocket IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
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
        //群发消息
        for (DockerLogsWebSocketServer item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
    public void sendMessage(String message) throws IOException {
//        for(int i=0;i<100;i++){
        String url = "http://harbor.ygt.cn:4243/containers/" + cid + "/logs?stdout=true&follow=true&tail=50";
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS).build();
        RequestBody body = null;
        Request request = new Request.Builder()
                .url(url).method("GET", body)
                .build();
        Response response;
        response = client.newCall(request).execute();
        System.out.println(response.code());
        InputStream inputStream = response.body().byteStream();
        byte[] buf = new byte[1024];
        // 定义一个StringBuffer用来存放字符串
        StringBuffer sb = new StringBuffer();
        // 开始读取数据
        int len = 0;// 每次读取到的数据的长度
        while ((len = inputStream.read(buf)) != -1) {// len值为-1时，表示没有数据了
            // append方法往sb对象里面添加数据
            sb.append(new String(buf, 0, len, "utf-8"));
            this.session.getBasicRemote().sendText(sb.toString());
        }
    }


    /**
     * 群发自定义消息
     */
    public static void sendInfo(String message, @PathParam("sid") String sid) throws IOException {
        log.info("推送消息到窗口" + sid + "，推送内容:" + message);
        for (DockerLogsWebSocketServer item : webSocketSet) {
            try {
                //这里可以设定只推送给这个sid的，为null则全部推送
                if (sid == null) {
                    item.sendMessage(message);
                } else if (item.sid.equals(sid)) {
                    item.sendMessage(message);
                }
            } catch (IOException e) {
                continue;
            }
        }
    }

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
