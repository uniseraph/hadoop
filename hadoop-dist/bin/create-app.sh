TARGET=`docker inspect -f '{{.NetworkSettings.Networks.vlan217.IPAddress}}' rm1`
APPLICATION_ID=`curl -d "" http://${TARGET}:8088/ws/v1/cluster/apps/new-application | jq '."application-id"' `
sed -i "s/application_id/${APPLICATION_ID}/g" commitapp.data
curl -H "Accept: application/json" -H "Content-Type: application/json"  -d "@commitapp.data"   http://${TARGET}:8088/ws/v1/cluster/apps
