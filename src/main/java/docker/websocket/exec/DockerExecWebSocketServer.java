package docker.websocket.exec;

import com.alibaba.fastjson.JSONObject;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.messages.ExecCreation;
import docker.websocket.exec.util.DockerHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author lurenjia
 */
@ServerEndpoint("/ws/container/exec/{ip}/{containerId}/{width}/{height}")
@Component
@Slf4j
public class DockerExecWebSocketServer implements Serializable {
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        DockerExecWebSocketServer that = (DockerExecWebSocketServer) o;

        if (!Objects.equals(session, that.session)) {
            return false;
        }
        if (!Objects.equals(width, that.width)) {
            return false;
        }
        if (!Objects.equals(height, that.height)) {
            return false;
        }
        if (!Objects.equals(containerId, that.containerId)) {
            return false;
        }
        if (!Objects.equals(ip, that.ip)) {
            return false;
        }
        if (!Objects.equals(sid, that.sid)) {
            return false;
        }
        return Objects.equals(socket, that.socket);
    }

    @Override
    public int hashCode() {
        int result = session != null ? session.hashCode() : 0;
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        result = 31 * result + (containerId != null ? containerId.hashCode() : 0);
        result = 31 * result + (ip != null ? ip.hashCode() : 0);
        result = 31 * result + (sid != null ? sid.hashCode() : 0);
        result = 31 * result + (socket != null ? socket.hashCode() : 0);
        return result;
    }

    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;
    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    private static CopyOnWriteArraySet<DockerExecWebSocketServer> webSocketSet = new CopyOnWriteArraySet<DockerExecWebSocketServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;


    private String width = "";
    private String height = "";
    /**
     * 容器id
     */
    private String containerId = "";
    /**
     * 容器所在物理机ip
     */
    private String ip = "";
    /**
     * 在线人数,可以说没啥用
     */
    private String sid = "";
    private Socket socket;
    /**
     * 连接建立成功调用的方法
     * onOpen  存在String类型参数,则必须使用@PathParam
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("ip") String ip, @PathParam("containerId") String containerId, @PathParam("width") String width, @PathParam("height") String height) throws Exception {
        this.width = width;
        this.height = height;
        this.ip = ip;
        this.containerId = containerId;
        this.sid = sid;
        this.session = session;
        webSocketSet.add(this);
        addOnlineCount();
        log.info("有新窗口开始监听:" + sid + ",当前在线人数为" + getOnlineCount());
        System.out.println("连接成功");
        String[] shells = {"/bin/bash","/bin/sh"};
        for(String shell: shells){
            //创建bash
            String execId = createExec(ip, containerId,shell);
            //连接bash
            Socket socket = connectExec(ip, execId);
            //获得输出
            getExecMessage(session, ip, containerId, socket);
            //修改tty大小
            try {
                resizeTty(ip, width, height, execId);
                break;
            }catch (Exception e){
                log.warn("bash不存在,换为sh");
            }
        }

    }

    private String createExec(String ip, String containerId,String shell) throws Exception {
        return DockerHelper.query(ip, docker -> {
            ExecCreation execCreation = docker.execCreate(containerId, new String[]{shell},
                    DockerClient.ExecCreateParam.attachStdin(), DockerClient.ExecCreateParam.attachStdout(), DockerClient.ExecCreateParam.attachStderr(),
                    DockerClient.ExecCreateParam.tty(true));
            return execCreation.id();
        });
    }

    private Socket connectExec(String ip, String execId) throws IOException {
        socket = new Socket(ip, 4243);
        socket.setKeepAlive(true);
        OutputStream out = socket.getOutputStream();
        StringBuilder pw = new StringBuilder();
        pw.append("POST /exec/").append(execId).append("/start HTTP/1.1\r\n");
        pw.append("Host: ").append(ip).append(":4243\r\n");
        pw.append("User-Agent: Docker-Client\r\n");
        pw.append("Content-Type: application/json\r\n");
        pw.append("Connection: Upgrade\r\n");
        JSONObject obj = new JSONObject();
        obj.put("Detach", false);
        obj.put("Tty", true);
        String json = obj.toJSONString();
        pw.append("Content-Length: ").append(json.length()).append("\r\n");
        pw.append("Upgrade: tcp\r\n");
        pw.append("\r\n");
        pw.append(json);
        out.write(pw.toString().getBytes(StandardCharsets.UTF_8));
        out.flush();
        return socket;
    }

    private void getExecMessage(Session session, String ip, String containerId, Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        StringBuilder returnMsg = new StringBuilder();
        while (true) {
            int n = inputStream.read(bytes);
            String msg = new String(bytes, 0, n);
            returnMsg.append(msg);
            bytes = new byte[10240];
            if (returnMsg.indexOf("\r\n\r\n") != -1) {
                session.getBasicRemote().sendText(returnMsg.substring(returnMsg.indexOf("\r\n\r\n") + 4));
                break;
            }
        }
        Thread thread = new Thread(() -> {
            try {
                byte[] bytes1 = new byte[1024];
                while (!Thread.interrupted()) {
                    int n = inputStream.read(bytes1);
                    String msg = new String(bytes1, 0, n);
                    if (msg.contains("stat /bin/bash: no such file or directory")) {
                        continue;
                    }
                    session.getBasicRemote().sendText(msg);
                    bytes1 = new byte[1024];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void resizeTty(String ip, String width, String height, String execId) throws Exception {
        DockerHelper.execute(ip, docker -> docker.execResizeTty(execId, Integer.parseInt(height), Integer.parseInt(width)));
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() throws Exception {
        webSocketSet.remove(this);
        subOnlineCount();
        session.close();
        log.info("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) throws Exception{

        OutputStream out = socket.getOutputStream();
        out.write(message.getBytes());
        out.flush();
    }

    /**
     * @param session session
     * @param error error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("发生错误");
        error.printStackTrace();
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        DockerExecWebSocketServer.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        DockerExecWebSocketServer.onlineCount--;
    }
}
