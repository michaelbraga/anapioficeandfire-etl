# sample-etl

docker network create spark-etl-net
docker-compose up --build --remove-orphans

 wait till kafka has created the topic, and postgres has properly started


 
cd spark-etl/
mvn clean package
* should be java 8, spark 2.4.5 has issues with java 9 and above
 java -jar target/spark-etl-1.0.jar
 
cd api-extractor/
mvn clean package
