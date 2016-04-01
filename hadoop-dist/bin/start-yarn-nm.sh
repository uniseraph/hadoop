docker run -d --net=vlan217  \
    -e YARN_OPTS="$YARN_OPTS -Dyarn.resourcemanager.hostname=10.209.164.36 -Dyarn.nodemanager.docker-container-executor.exec-name=/usr/bin/docker -Dyarn.nodemanager.container-executor.class=org.apache.hadoop.yarn.server.nodemanager.DockerContainerExecutor " \
   -v /usr/bin/docker:/usr/bin/docker \
   -v /var/run/docker.sock:/var/run/docker.sock \
   acs-reg.alipay.com/hadoop/hadoop:2.7.2-acs ./bin/yarn --config ./etc/hadoop nodemanager
