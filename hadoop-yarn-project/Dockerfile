FROM java:8
MAINTAINER zhengtao.wuzt@alibaba-inc.com


USER root

#ENV HADOOP_PREFIX /usr/local/hadoop
#ENV HADOOP_COMMON_HOME /usr/local/hadoop
#ENV HADOOP_HDFS_HOME /usr/local/hadoop
#ENV HADOOP_MAPRED_HOME /usr/local/hadoop
#ENV HADOOP_YARN_HOME /usr/local/hadoop
#ENV HADOOP_CONF_DIR /usr/local/hadoop/etc/hadoop
ENV YARN_PREFIX /usr/local/yarn
ENV YARN_CONF_DIR $HADOOP_PREFIX/etc/hadoop
ENV YARN_VERSION 2.7.2
#RUN mkdir /usr/local/hadoop

ADD ./target/hadoop-yarn-project-2.7.2.tar.gz /usr/local/
RUN ln -s /usr/local/hadoop-yarn-project-2.7.2 /usr/local/yarn
