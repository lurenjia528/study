package docker.tls;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.model.Version;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerCertificates;

import java.net.URI;
import java.nio.file.Paths;

/**
 * @author yanggt
 * @date 19-4-25
 */
public class TlsApi {
    public static void main(String[] args) throws Exception{
        docker_java();
        docker_client();
    }

    /**
     * docker-java 对低版本docker(例如1.10)不太友好
     */
    public static void docker_java() {
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost("tcp://127.0.0.1:4243")
                .withDockerTlsVerify(true)
                .withDockerCertPath("/home/ht061/.docker/tls")
                .withDockerConfig("/home/ht061/.docker")
                .withApiVersion("1.35")
//                .withRegistryUrl("https://index.docker.io/v1/")
//                .withRegistryUsername("dockeruser")
//                .withRegistryPassword("ilovedocker")
//                .withRegistryEmail("dockeruser@github.com")
                .build();
        DockerClient docker = DockerClientBuilder.getInstance(config).build();
        Version exec = docker.versionCmd().exec();
        System.out.println(exec.toString());
    }

    /**
     * docker-client 对低版本docker(例如1.10)也支持
     * @throws Exception
     */
    public static void docker_client() throws Exception {
        final com.spotify.docker.client.DockerClient docker = DefaultDockerClient.builder()
                .uri(URI.create("https://192.168.17.187:4243"))
                .dockerCertificates(new DockerCertificates(Paths.get("/home/ht061/.docker/tls")))
                .build();
        System.out.println(docker.version());
        docker.close();
    }
}
