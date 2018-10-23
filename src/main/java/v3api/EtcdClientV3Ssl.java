package v3api;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.data.KeyValue;
import com.coreos.jetcd.kv.GetResponse;
import io.grpc.netty.GrpcSslContexts;
import io.netty.handler.ssl.SslContext;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author lurenjia
 * @date 2018/10/23
 */
public class EtcdClientV3Ssl {
    public static void main(String[] args) throws Exception{
        File cert = new File("E:\\softwarelocation\\code\\IdeaProjects\\etcdstudy\\src\\main\\java\\v3api\\etcd.pem");
        Client client = Client.builder()
                .endpoints("http://192.168.200.222:2380")
                .sslContext(GrpcSslContexts.forClient()
                        .trustManager(cert)
                        .build())
                .build();
        KV kvClient = client.getKVClient();

        CompletableFuture<GetResponse> getResponseCompletableFuture = kvClient.get(ByteSequence.fromString("/ygt"));
        GetResponse getResponse = getResponseCompletableFuture.get();
        List<KeyValue> kvs = getResponse.getKvs();
        for (KeyValue kv : kvs) {
            System.out.println("key=" + kv.getKey().toStringUtf8());
            System.out.println("value=" + kv.getValue().toStringUtf8());
        }
    }
}
