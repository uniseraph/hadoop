GIT_VERSION=`git log -1 --pretty=format:%h`

#docker build --rm -t  acs-reg.alipay.com/hadoop/hadoop:2.7.2-${GIT_VERSION} .
#docker tag   acs-reg.alipay.com/hadoop/hadoop:2.7.2-${GIT_VERSION} acs-reg.alipay.com/hadoop/hadoop:2.7.2-dev

docker build --rm -t  acs-reg.alipay.com/hadoop/hadoop:2.7.2-dev .
#docker push acs-reg.alipay.com/hadoop/hadoop:2.7.2-${GIT_VERSION} 
docker push acs-reg.alipay.com/hadoop/hadoop:2.7.2-dev
