package v3api;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.data.KeyValue;
import com.coreos.jetcd.kv.GetResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author lurenjia
 * @date 2018/10/23
 */
public class EtcdClientV3 {

    public static void main(String[] args) throws Exception {

        Client client = Client.builder().endpoints("http://192.168.200.222:2379").build();

        KV kvClient = client.getKVClient();

        putKeyValue(kvClient, "/hello", "world");

        getKeyValue(kvClient, "/hello", "utf-8");

        deleteKey(kvClient, "/hello");

        kvClient.close();
        client.close();
    }

    /**
     * 获取指定key的value
     *
     * @param kvClient
     * @param key
     * @param charset
     * @throws Exception
     */
    private static void getKeyValue(KV kvClient, String key, String charset) throws Exception {
        CompletableFuture<GetResponse> getResponseCompletableFuture = kvClient.get(ByteSequence.fromString(key));
        GetResponse getResponse = getResponseCompletableFuture.get();
        List<KeyValue> kvs = getResponse.getKvs();
        for (KeyValue kv : kvs) {
            System.out.println("key=" + kv.getKey().toString(charset));
            System.out.println("value=" + kv.getValue().toStringUtf8());
        }
    }

    /**
     * 添加或修改指定的key-value
     *
     * @param kvClient
     * @param key
     * @param value
     * @throws Exception
     */
    private static void putKeyValue(KV kvClient, String key, String value) throws Exception {
        ByteSequence key1 = ByteSequence.fromString(key);
        ByteSequence value1 = ByteSequence.fromString(value);
        kvClient.put(key1, value1).get();
    }

    /**
     * 删除指定的key
     *
     * @param kvClient
     * @param key
     * @throws Exception
     */
    private static void deleteKey(KV kvClient, String key) throws Exception {
        kvClient.delete(ByteSequence.fromString(key)).get();
    }
}
