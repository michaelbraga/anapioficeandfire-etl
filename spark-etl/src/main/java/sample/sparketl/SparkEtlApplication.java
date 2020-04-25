package sample.sparketl;

import lombok.extern.log4j.Log4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sample.sparketl.config.KafkaConfig;
import sample.sparketl.etl.transformer.manager.TransformManager;
import sample.sparketl.spark.EtlSparkSession;

@Log4j
@SpringBootApplication
public class SparkEtlApplication implements CommandLineRunner {
	@Autowired
	private EtlSparkSession etlSparkSession;
	@Autowired
	private TransformManager transformManager;
	@Autowired
	private KafkaConfig kafkaConfig;

	public static void main(String[] args) {
		SpringApplication.run(SparkEtlApplication.class, args);
	}

	@Override
	public void run(String... args) {
		try {
			Dataset<String> logStream = etlSparkSession.getSparkSession()
					.readStream()
					.format("kafka")
					.option("kafka.bootstrap.servers", kafkaConfig.getBootstrapServers())
					.option("kafkaConsumer.pollTimeoutMs", kafkaConfig.getConsumerPollTimeout())
					.option("subscribe", kafkaConfig.getTopic())
					.option("startingOffsets", kafkaConfig.getStartingOffsets())
					.option("failOnDataLoss", kafkaConfig.isFailOnDataLoss())
					.load()
					.selectExpr("CAST(value AS STRING)")
					.na().drop()
					.as(Encoders.STRING());
			transformManager.transform(logStream);
			etlSparkSession.getSparkSession().streams().awaitAnyTermination();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR");
		}
	}
}