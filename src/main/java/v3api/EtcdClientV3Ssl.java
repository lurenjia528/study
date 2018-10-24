package v3api;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.data.KeyValue;
import com.coreos.jetcd.kv.GetResponse;
import io.grpc.netty.GrpcSslContexts;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author lurenjia
 * @date 2018/10/23
 */
public class EtcdClientV3Ssl {

    private static Client client;
    private static KV kvClient;

    private static void initClient() throws Exception {
        File cert = new File("E:\\softwarelocation\\code\\IdeaProjects\\etcdstudy\\src\\main\\java\\v3api\\etcd.pem");
        File pkcs8Key = new File("E:\\softwarelocation\\code\\IdeaProjects\\etcdstudy\\src\\main\\java\\v3api\\pkcs8-key.pem");

        client = Client.builder()
                .endpoints("https://192.168.200.222:2379")
                .sslContext(GrpcSslContexts.forClient()
                        .trustManager(cert)
                        .keyManager(cert, pkcs8Key)
                        .build())
                .build();
    }

    public static void main(String[] args) throws Exception {

        initClient();

        kvClient = client.getKVClient();

        putKey(kvClient, "/hello", "world");

        getKey(kvClient, "/hello");

        deleteKey(kvClient, "/hello");

        closeClient();
    }

    /**
     * 获取指定key
     *
     * @param kvClient
     * @param key
     * @throws Exception
     */
    private static void getKey(KV kvClient, String key) throws Exception {
        CompletableFuture<GetResponse> getResponseCompletableFuture = kvClient.get(ByteSequence.fromString(key));
        GetResponse getResponse = getResponseCompletableFuture.get();
        List<KeyValue> kvs = getResponse.getKvs();
        for (KeyValue kv : kvs) {
            System.out.println("key=" + kv.getKey().toStringUtf8());
            System.out.println("value=" + kv.getValue().toStringUtf8());
        }
    }

    /**
     * 添加key-value
     *
     * @param kvClient
     * @param key
     * @param value
     * @throws Exception
     */
    private static void putKey(KV kvClient, String key, String value) throws Exception {
        kvClient.put(ByteSequence.fromString(key), ByteSequence.fromString(value)).get();
    }

    /**
     * 删除key
     *
     * @param kvClient
     * @param key
     * @throws Exception
     */
    private static void deleteKey(KV kvClient, String key) throws Exception {
        kvClient.delete(ByteSequence.fromString(key)).get();
    }

    /**
     * 关闭客户端
     */
    private static void closeClient() {
        kvClient.close();
        client.close();
    }
}
