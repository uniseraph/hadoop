docker rm -f rm1 nm1 nm2 nm3
docker-compose -f yarn.yml -p yarn up -d
docker inspect -f '{{.NetworkSettings.Networks.vlan217.IPAddress}}' rm1
