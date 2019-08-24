# study
## etcd
etcd etcd.v2api etcd.v3api java ssl

按照教程搭建带tls认证的etcd集群后，会生成三个文件
`etcd-ca.pem`  `etcd-key.pem`  `etcd.pem`
与下面命令对应  

``` bash
 etcdctl --cacert /etc/etcd/ssl/etcd-ca.pem --cert /etc/etcd/ssl/etcd.pem --key /etc/etcd/ssl/etcd-key.pem get /hello
```
显示所有头key
```bash
 etcdctl --cacert /etc/etcd/ssl/etcd-ca.pem --cert /etc/etcd/ssl/etcd.pem --key /etc/etcd/ssl/etcd-key.pem get / --prefix --keys-only
```

v3版本api认证需要PKCS8类型的key.pem文件，由以下命令生成

` openssl pkcs8 -topk8 -nocrypt -in etcd-key.pem -out pkcs8-key.pem`

etcd restapi 

`http://editor.swagger.io/`

`https://github.com/etcd-io/etcd/blob/master/Documentation/dev-guide/apispec/swagger/rpc.swagger.json`

`https://github.com/etcd-io/etcd/blob/master/Documentation/dev-guide/api_grpc_gateway.md`

arm架构下连接etcd，需要netty-tcnative-2.0.12.Final-linux-aarch_64.jar，添加进依赖

restapi
例如：v3

`curl -X POST -H 'Content-type: application/json' -d '{}' --cacert /etc/etcd/ssl/etcd-ca.pem --cert /etc/etcd/ssl/etcd.pem --key /etc/etcd/ssl/etcd-key.pem https://127.0.0.1:2379/v3alpha/cluster/member/list`

`{"header":{"cluster_id":"9387297561454088963","member_id":"8976880612339134383","raft_term":"115"},"members":[{"ID":"8679419496537157493","name":"kube-etcd2","peerURLs":["http://192.168.17.161:2380"],"clientURLs":["https://192.168.17.161:2379"]},{"ID":"8976880612339134383","name":"kube-etcd1","peerURLs":["http://192.168.17.160:2380"],"clientURLs":["https://192.168.17.160:2379"]},{"ID":"12506278219037213787","name":"kube-etcd3","peerURLs":["http://192.168.17.162:2380"],"clientURLs":["https://192.168.17.162:2379"]}]}`

`curl -X POST -H 'Content-type: application/json' -d '{}' --cacert /etc/etcd/ssl/etcd-ca.pem --cert /etc/etcd/ssl/etcd.pem --key /etc/etcd/ssl/etcd-key.pem https://127.0.0.1:2379/metrics`



## harbor

安装harbor后

harbor token使用
https://github.com/goharbor/harbor/wiki/Harbor-FAQs#api

harbor api 

`https://github.com/goharbor/harbor/blob/master/docs/swagger.yaml` 

复制到

`http://editor.swagger.io/`

访问时：

`curl -u "admin:Harbor12345" http......`

相当于 `curl -H "Authorization: Basic YWRtaW46SGFyYm9yMTIzNDU="`(admin:Harbor12345的base64编码，注意Basic中间的空格)

转换到java 添加请求头 

`key=authorization` `value= Basic YWRtaW46SGFyYm9yMTIzNDU=`

## json

jsoniter fastjson

## jar

增加读取jar包中文件测试

## k8s

k8s Restful Api 和客户端的使用

需要把k8s集群master节点上的~/.kube/config 复制到此电脑的相同目录(~/.kube/config)

## freemarker 

freemarker 调用java方法

## 添加发送邮件测试

## 添加actuator监控测试

访问路径：http://localhost:8888/actuator/metrics  health  info  ...


## 添加文件上传/下载测试

http://localhost:8888/file/upload

http://localhost:8888/file/download

## 添加简单websocket

## docker remote api

开启普通http接口
```bash
dockerd -H tcp://0.0.0.0:4243 -H unix:///var/run/docker.sock

cat docker.socket
[Unit]
Description=Docker Socket for the API
PartOf=docker.service

[Socket]
ListenStream=/var/run/docker.sock
SocketMode=0660
SocketUser=root
SocketGroup=docker

[Install]
WantedBy=sockets.target

```

开启tls认证的remote 接口

参考 https://docs.docker.com/engine/security/https/

服务端
```bash
ls /root/.docker/tls
ca.pem  server-cert.pem  server-key.pem
```

客户端
```bash
ls /home/ht061/.docker/tls
ca.pem  cert.pem  key.pem
```

```bash
dockerd -H tcp://0.0.0.0:4243 -H unix:///var/run/docker.sock --tlsverify --tlscacert=/root/.docker/tls/ca.pem --tlscert=/root/.docker/tls/server-cert.pem --tlskey=/root/.docker/tls/server-key.pem
```

添加依赖
//对低版本不太友好(https remote api)
com.github.docker-java:docker-java:3.1.2
//对低版本也支持
com.spotify:docker-client:8.15.2

## springboot 热更新配置文件

@RefreshScope注解

启动项目,访问

`http://127.0.0.1:8888/file/test-refresh`

更改application.yml

发送请求更新配置

`curl -v  -X POST http://127.0.0.1:8888/actuator/refresh`

再次访问

`http://127.0.0.1:8888/file/test-refresh`

## 拦截器,过滤器
### 全局拦截器中读取request流之后,到controller中@RequestBody读取不到流数据,原因:在interceptor中流被消费了,解决办法:src/main/java/filter
` curl -X POST -H "Content-Type:application/json" -H "user:tom" -d '{"just":"for test"}' http://127.0.0.1:8888/test/pt?user=jerry`
```bash
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.1.6.RELEASE)

2019-08-24 09:58:27.115 - INFO 28605 --- [study] c.c.c.ConfigServicePropertySourceLocator : Fetching config from server at : http://localhost:8888
-2019-08-24 09:58:27.207 - INFO 28605 --- [study] c.c.c.ConfigServicePropertySourceLocator : Connect Timeout Exception on Url - http://localhost:8888. Will be trying the next url if available
-2019-08-24 09:58:27.207 - WARN 28605 --- [study] c.c.c.ConfigServicePropertySourceLocator : Could not locate PropertySource: I/O error on GET request for "http://localhost:8888/application/default": 拒绝连接 (Connection refused); nested exception is java.net.ConnectException: 拒绝连接 (Connection refused)
-2019-08-24 09:58:27.209 - INFO 28605 --- [study] StudyApplication                         : No active profile set, falling back to default profiles: default
-2019-08-24 09:58:27.970 - INFO 28605 --- [study] o.s.cloud.context.scope.GenericScope     : BeanFactory id=0e388660-a51b-3b0d-a36f-9d3b084d8e1b
-2019-08-24 09:58:27.994 - INFO 28605 --- [study] trationDelegate$BeanPostProcessorChecker : Bean 'org.springframework.cloud.autoconfigure.ConfigurationPropertiesRebinderAutoConfiguration' of type [org.springframework.cloud.autoconfigure.ConfigurationPropertiesRebinderAutoConfiguration$$EnhancerBySpringCGLIB$$ecfaff4f] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
-2019-08-24 09:58:28.185 - INFO 28605 --- [study] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 8888 (http)
-2019-08-24 09:58:28.208 - INFO 28605 --- [study] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
-2019-08-24 09:58:28.208 - INFO 28605 --- [study] org.apache.catalina.core.StandardEngine  : Starting Servlet engine: [Apache Tomcat/9.0.21]
-2019-08-24 09:58:28.296 - INFO 28605 --- [study] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
-2019-08-24 09:58:28.296 - INFO 28605 --- [study] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 1070 ms
---------------过滤器初始化------------
2019-08-24 09:58:28.857 - INFO 28605 --- [study] o.s.s.concurrent.ThreadPoolTaskExecutor  : Initializing ExecutorService 'applicationTaskExecutor'
-2019-08-24 09:58:29.227 - INFO 28605 --- [study] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 19 endpoint(s) beneath base path '/actuator'
-2019-08-24 09:58:29.325 - INFO 28605 --- [study] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8888 (http) with context path ''
-2019-08-24 09:58:29.327 - INFO 28605 --- [study] StudyApplication                         : Started StudyApplication in 3.352 seconds (JVM running for 3.884)
-2019-08-24 09:58:29.376 - INFO 28605 --- [study] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
-2019-08-24 09:58:29.377 - INFO 28605 --- [study] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
-2019-08-24 09:58:29.385 - INFO 28605 --- [study] o.s.web.servlet.DispatcherServlet        : Completed initialization in 8 ms
----------------拦截器开始------------------
path:tom
header:jerry
{"just":"for test"}
拦截器拦截完成
---------controller----------
{"just":"for test"}
path:tom
header:jerry
---------controller----------
---------------拦截器方法二开始------------------
---------------拦截器方法三开始------------------

----------------拦截器开始------------------
path:null
header:null
null
拦截器拦截完成
---------------拦截器方法二开始------------------
---------------拦截器方法三开始------------------
---------------拦截器开始------------------
path:null
header:null
null
拦截器拦截完成
---------------拦截器方法二开始------------------
---------------拦截器方法三开始------------------
2019-08-24 09:58:49.768 - WARN 28605 --- [study] c.c.c.ConfigServicePropertySourceLocator : Could not locate PropertySource: label not found
-2019-08-24 09:58:49.913 - INFO 28605 --- [study] o.s.s.concurrent.ThreadPoolTaskExecutor  : Shutting down ExecutorService 'applicationTaskExecutor'
---------------过滤器销毁------------

Process finished with exit code 130 (interrupted by signal 2: SIGINT)

```