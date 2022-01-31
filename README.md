# spring-kafka-registry

## create package model (generate Avro and ProtoBuf sources)
mvn clean install -f model/pom.xml

## Run Kafka Cluster
```
cd docker/
docker-compose up -d
```

## Run applications
```
cd ..
mvn spring-boot:run -f producer/pom.xml
mvn spring-boot:run -f consumer/pom.xml
```

curl http://localhost:8080/generateAvro?count=1&pause=10

curl http://localhost:8080/generateProto?count=1&pause=10

curl http://localhost:8080/generateJson?count=1&pause=10
