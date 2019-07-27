package docker;

import javax.websocket.*;
import java.io.*;
import java.net.URI;

/**
 * 向容器发送命令,但未接收到返回
 */
@ClientEndpoint
public class WebSocketClientT {
    private String uri = "ws://localhost:4243/containers/867a99982579/attach/ws?stream=true&stdout=true&logs=true&stdin=true&stderr=true";
    public Session session;

    @OnOpen
    public void onOpen(Session session) throws Exception{
        this.session = session;
        System.out.println("open");
    }

    @OnMessage
    public void onMessage(String message) throws Exception{
        System.out.println(message);

    }

    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        System.out.println("close");
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }


    public void start() {
        WebSocketContainer container = null;
        try {
            container = ContainerProvider.getWebSocketContainer();
        } catch (Exception ex) {
            System.out.println("error" + ex);
        }
        try {
            URI r = URI.create(uri);
            session = container.connectToServer(WebSocketClientT.class, r);

        } catch (DeploymentException | IOException e) {
            e.printStackTrace();
        }

    }
}

class C {
    public static void main(String[] args) {
        WebSocketClientT client = new WebSocketClientT();
        client.start();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = "";
        try {
            do {
                input = br.readLine();
                if (!input.equals("exit"))
                    client.sendMessage(input + "\n");
            } while (!input.equals("exit"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
