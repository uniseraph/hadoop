docker rm -f rm1 nm1 nm2 nm3
docker pull acs-reg.alipay.com/hadoop/hadoop:2.7.2-dev
docker-compose -f yarn.yml -p yarn up -d
docker inspect -f '{{.NetworkSettings.Networks.vlan217.IPAddress}}' rm1
