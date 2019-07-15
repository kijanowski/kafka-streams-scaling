# Scaling Kafka Streams applications with Kubernetes

Detailed explanation what this repo is about is available at [Autoscaling Kafka Streams applications with Kubernetes](https://blog.softwaremill.com/autoscaling-kafka-streams-applications-with-kubernetes-9aed2e37d3a0).

## TL;DR

```
$ cd $CONFLUENT_HOME
$ ./bin/zookeeper-server-start etc/kafka/zookeeper.properties
$ ./bin/kafka-server-start etc/kafka/server.properties
       
$ ./bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 3 --topic inScalingTopic
$ ./bin/kafka-topics --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic outScalingTopic

$ ./bin/kafka-console-producer --broker-list localhost:9092 --topic inScalingTopic --property parse.key=true --property key.separator=:
>b4320f5d-4d16-4999-827d-190d7d44da45:SomeRandomMessage
>0336df45-c5f4-486d-8f15-871881b158ec:done
>9acec74a-cbfb-4bd5-90da-5f661b06f2b6:AnotherRandomMessage

$ ./bin/kafka-console-consumer --bootstrap-server localhost:9092 --topic outScalingTopic --from-beginning --property print.key=true

$ cd $APP_HOME
$ ./gradlew clean build fatJar
$ java -Dcom.sun.management.jmxremote.port=5555 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -jar build/libs/kafka-streams-scaling-all.jar
$ java -Dcom.sun.management.jmxremote.port=5556 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -jar build/libs/kafka-streams-scaling-all.jar
$ java -Dcom.sun.management.jmxremote.port=5557 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false -jar build/libs/kafka-streams-scaling-all.jar

$ java -cp build/libs/kafka-streams-scaling-all.jar kafka.streams.scaling.Sender

```

## Misc / Scratchpad
```
$ ./bin/kafka-run-class kafka.tools.GetOffsetShell  --topic inScalingTopic --broker-list localhost:9092 
$ ./kafka-consumer-groups --bootstrap-server --group --describe
$ ./bin/kafka-console-consumer --bootstrap-server localhost:9092 --topic outScalingTopic --from-beginning --property print.timestamp=true --property print.key=true  --property value.deserializer=org.apache.kafka.common.serialization.IntegerDeserializer
$ echo "get -s -b kafka.consumer:type=consumer-fetch-manager-metrics,client-id=ks-scaling-app-app-id-0db65a38-f83e-4e23-a049-7c66bff16bf3-StreamThread-1-consumer,topic=inSimpleScalingTopic2,partition=1 records-lead" | java -jar jmxterm-1.0.0-uber.jar -l localhost:5556 -v silent -n
```
