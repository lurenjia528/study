package etcd.v2api;

import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.requests.EtcdKeyPutRequest;
import mousio.etcd4j.responses.EtcdKeysResponse;
import mousio.etcd4j.responses.EtcdVersionResponse;

import java.net.URI;

/**
 * @author lurenjia
 * @date 2018/10/23
 */
public class EtcdClientV2 {
    public static void main(String[] args) throws Exception {
        EtcdClient client = new EtcdClient(URI.create("http://192.168.200.222:2379"));

        getVersion(client);

        putKey(client, "/hello", "world");

        getKey(client, "/hello");

        deleteKey(client,"/hello");
//        deleteDirs(client,"/hello");

        client.close();
    }

    /**
     * 新建key-value，存储到etcd
     * @param client
     * @param key
     * @param value
     * @throws Exception
     */
    private static void putKey(EtcdClient client, String key, String value) throws Exception {
        EtcdKeyPutRequest world = client.put(key, value).value(value);
        EtcdKeysResponse etcdKeysResponse = world.send().get();
        System.out.println(etcdKeysResponse.getNode().value);
        System.out.println(etcdKeysResponse.getNode().getValue());
        System.out.println(etcdKeysResponse.node.value);
        System.out.println(etcdKeysResponse.getNode().key);
        System.out.println(etcdKeysResponse.getNode().getKey());
        System.out.println(etcdKeysResponse.node.key);
        System.out.println(etcdKeysResponse.getNode().dir);
    }

    /**
     * 删除目录
     * @param client
     * @param dir
     * @throws Exception
     */
    private static void deleteDirs(EtcdClient client, String dir) throws Exception {
        EtcdKeysResponse etcdKeysResponse = client.deleteDir(dir).recursive().send().get();
        System.out.println(etcdKeysResponse.node.key);
        System.out.println(etcdKeysResponse.node.value);
    }

    /**
     * 删除key
     * @param client
     * @param dir
     * @throws Exception
     */
    private static void deleteKey(EtcdClient client, String dir) throws Exception {
        EtcdKeysResponse etcdKeysResponse = client.delete(dir).send().get();
        System.out.println(etcdKeysResponse.node.key);
        System.out.println(etcdKeysResponse.node.value);
    }

    /**
     * 版本信息
     * @param client
     */
    private static void getVersion(EtcdClient client) {
        EtcdVersionResponse version = client.version();
        System.out.println(version.getCluster());
        System.out.println(version.getServer());
    }

    /**
     * 获取key的value
     * @param client
     * @param key
     * @return
     * @throws Exception
     */
    private static String getKey(EtcdClient client, String key) throws Exception {
        String value = client.get(key).consistent().send().get().node.value;
        return value;
    }
}
