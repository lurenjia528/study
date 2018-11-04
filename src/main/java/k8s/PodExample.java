package k8s;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.fabric8.kubernetes.client.dsl.base.OperationSupport.JSON;

/**
 * @author yanggt
 * @date 18-9-14
 */
public class PodExample {
    public static void main(String[] args) {
        //需要把k8s集群master节点上的~/.kube/config 复制到此电脑的相同目录
        Config config1 = Config.autoConfigure(null);
        DefaultKubernetesClient client = new DefaultKubernetesClient(config1);

        //此httpClient可以向k8s集群发请求(Restful Api) 直接发送https请求即可
        OkHttpClient httpClient = client.getHttpClient();

        Pod pod = new PodBuilder().build();

        //设置metadata
        ObjectMeta meta = new ObjectMeta();
        meta.setName("busybox-test-istio");
        meta.setNamespace("ygt");
        Map<String,String> label = new HashMap<>();
        label.put("app","honor");
        meta.setLabels(label);
        //设置spec
        PodSpec spec = new PodSpecBuilder().build();
        List<Container> containerList = new ArrayList<>();
        Container container = new ContainerBuilder().build();
        container.setName("my-busybox");
        container.setImage("kylincloud2.hub/busybox:0.0.1");
        container.setImagePullPolicy("IfNotPresent");
        List<String> command = new ArrayList<>();
        command.add("/bin/sh");
        command.add("-c");
        container.setCommand(command);
        List<String> args1 = new ArrayList<>();
        args1.add("sleep 3600");
        container.setArgs(args1);
        containerList.add(container);
        spec.setContainers(containerList);
        spec.setNodeName("k8s-1");

        pod.setMetadata(meta);
        pod.setSpec(spec);
        client.inNamespace("ygt").pods().create(pod);

        //删除指定namespace的pod
//        client.inNamespace("ygt").pods().withName("busybox-test").delete();
    }

    private static void sendRequestToK8s(OkHttpClient httpClient, String json, String url) throws IOException {

        //创建 namesapce
//        Namespace namespace1 = new Namespace();
//        ObjectMeta metadata = new ObjectMeta();
//        metadata.setName(namespace);
//        namespace1.setMetadata(metadata);
//        client.namespaces().createOrReplace(namespace1);


        RequestBody body = RequestBody.create(JSON, json);
        Request request1 = new Request.Builder()
                .post(body)
                .url(url)
                .build();
        Response response = httpClient.newCall(request1).execute();
        String body1 = response.body().string();
        System.out.println(body1);
    }
}























