FROM java:8
MAINTAINER zhengtao.wuzt@alibaba-inc.com


USER root

ENV HADOOP_PREFIX /usr/local/hadoop
ENV HADOOP_COMMON_HOME /usr/local/hadoop
ENV HADOOP_HDFS_HOME /usr/local/hadoop
ENV HADOOP_MAPRED_HOME /usr/local/hadoop
ENV HADOOP_YARN_HOME /usr/local/hadoop
ENV HADOOP_CONF_DIR /usr/local/hadoop/etc/hadoop
ENV YARN_CONF_DIR $HADOOP_PREFIX/etc/hadoop

ADD ./target/hadoop-2.7.2.tar.gz /usr/local/
RUN ln -s /usr/local/hadoop-2.7.2 /usr/local/hadoop


WORKDIR /usr/local/hadoop
