TARGET=$1
#curl -d "" http://${TARGET}:8088/ws/v1/cluster/apps/new-application
curl -H "Accept: application/json" -H "Content-Type: application/json"  -d "@commitapp.data"   http://${TARGET}:8088/ws/v1/cluster/apps
