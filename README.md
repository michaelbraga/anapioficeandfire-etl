# anapioficeandfire-etl

### Start zookeeper, kafka, and postgres for local setup

```
cd local-docker
docker network create spark-etl-net
docker-compose up --build --remove-orphans
```

### Start Spark Streaming ETL
Structure streaming that reads from kafka and stores data to Postgres.
Wait till kafka has created the topic, and postgres has properly started.
To adjust number of cores for spark, edit `application.yaml`.

``` 
cd spark-etl/
mvn clean package
# should be java 8, spark 2.4.5 has issues with java 9 and above
java -jar target/spark-etl-1.0.jar
```

### Start API data extractor for anapioficeandfire.com/api
A Depth-First Search algorithm implemented in Java to get all books, characters, and houses retrievable in anapioficeandfire.com/api. The root url is anapioficeandfire.com/api which returns data about some books and houses, which is then used as neighbors to crawl connections on other data.

Data is then sent to kafka for Spark ETL app to consume, transform, and load to the database.

```
cd api-extractor/
mvn clean package
java -jar api-extractor-1.0.jar
```
