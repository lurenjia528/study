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

## docker

安装haobor后

java 远程调用docker api