docker run -d --net=vlan217 --name yarn-rm \
  acs-reg.alipay.com/hadoop/hadoop:2.7.2-acs \
  ./bin/yarn --config ./etc/hadoop  resourcemanager
