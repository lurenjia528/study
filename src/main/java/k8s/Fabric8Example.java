package k8s;

import io.fabric8.kubernetes.api.model.*;
import io.fabric8.kubernetes.client.Config;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yanggt
 * @date 18-9-14
 */
public class Fabric8Example {
    public static void main(String[] args) throws IOException {

        Config config = new ConfigBuilder().withMasterUrl("https://192.168.17.160:6443").build();
        Config config1 = Config.autoConfigure(null);
        DefaultKubernetesClient client = new DefaultKubernetesClient(config1);

        //获取所有namespace
//        List<Namespace> items = client.namespaces().list().getItems();
//        for (Namespace namespace : items) {
//            System.out.println(namespace.toString());
//        }

        //获取所有nodes
//        List<Node> items1 = client.nodes().list().getItems();
//        for (Node node : items1) {
//            System.out.println(node.toString());
//        }

        //获取单独namespace
//        Namespace namespace = client.namespaces().withName("kube-system").get();
//        System.out.println(namespace.toString());

        //获取单独namespace的pod
//        List<Pod> items2 = client.pods().inNamespace("kube-system").list().getItems();
//        for(Pod pod : items2){
//            System.out.println(pod.toString());
//        }


        //创建namespace
//        Namespace ns = new NamespaceBuilder().build();
//        ObjectMeta metadata = new ObjectMeta();
//        Map<String,String> labels = new HashMap<>();
//        labels.put("label1", "qwe");
//        metadata.setLabels(labels);
//        metadata.setName("ygt");
//        ns.setMetadata(metadata);
//        client.namespaces().create(ns);


        //删除指定namespace
//        client.namespaces().withName("busybox").delete();

//        OkHttpClient httpClient = client.getHttpClient();
//
//        Request request = new Request.Builder()
//                .url("https://192.168.17.160:6443/apis/apps/v1/namespaces/kube-system/deployments")
//                .method("GET",null)
//                .build();
//        Response response;
//
//        response = httpClient.newCall(request).execute();
//        String string = response.body().string();
//        System.out.println(string);

        Pod pod = new PodBuilder().build();
        //设置metadata
        ObjectMeta meta = new ObjectMeta();
        meta.setName("busybox-test");
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
//        command.add("sleep 3600");
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
}



























