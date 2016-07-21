# kony-converter

主要技术点：


通过 openoffice 4.0.2 + jodconverter-core-3.0-beta-4-dist 将word\powerpoint\excel 转换到pdf
通过 swftools+xpdf 将pdf转换到swf

fastdfs文件系统存储上传的原视频和转换后的视频文件，一个国内开源的支持分布式部署的文件系统。


通过 spring 容器对服务接口进行依赖注入、事务管理

quartz 进行任务调度根据设置的cron trigger触发执行转换服务。Quartz是OpenSymphony开源组织在Job scheduling领域又一个开源项目，它可以与J2EE与J2SE应用程序相结合也可以单独使用。Quartz可以用来创建简单或为运行十个，百个，甚至是好几万个Jobs这样复杂的程序。Jobs可以做成标准的Java组件或 EJBs。

dubbo 分布式服务治理，服务提供者和消费者。DUBBO是一个分布式服务框架，致力于提供高性能和透明化的RPC远程服务调用方案，是阿里巴巴SOA服务化治理方案的核心框架，每天为2,000+个服务提供3,000,000,000+次访问量支持，并被广泛应用于阿里巴巴集团的各成员站点。

zookeeper 是一个分布式的，开放源码的分布式应用程序协调服务，是Google的Chubby一个开源的实现，是Hadoop和Hbase的重要组件。它是一个为分布式应用提供一致性服务的软件，提供的功能包括：配置维护、域名服务、分布式同步、组服务等。
MyBatis服务化的文档转换服务。


文档转换服务，支持在对文档转换过程中加水印、页头、页脚。
