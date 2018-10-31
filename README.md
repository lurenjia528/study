# study
## etcd
etcd etcd.v2api etcd.v3api java ssl

按照教程搭建带tls认证的etcd集群后，会生成三个文件
`etcd-ca.pem`  `etcd-key.pem`  `etcd.pem`
与下面命令对应  

``` bash
 etcdctl --cacert /etc/etcd/ssl/etcd-ca.pem --cert /etc/etcd/ssl/etcd.pem --key /etc/etcd/ssl/etcd-key.pem get /hello
```


v3版本api认证需要PKCS8类型的key.pem文件，由以下命令生成

` openssl pkcs8 -topk8 -nocrypt -in etcd-key.pem -out pkcs8-key.pem`

etcd restapi 

`http://editor.swagger.io/`

`https://github.com/etcd-io/etcd/blob/master/Documentation/dev-guide/apispec/swagger/rpc.swagger.json`

`https://github.com/etcd-io/etcd/blob/master/Documentation/dev-guide/api_grpc_gateway.md`


## docker

安装haobor后

java 远程调用docker api

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
