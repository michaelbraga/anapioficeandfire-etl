package sample.sparketl.etl.transformer.manager;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.streaming.StreamingQueryException;
import org.apache.spark.sql.streaming.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sample.sparketl.config.PostgresConfig;
import sample.sparketl.etl.transformer.book.BookTransformer;
import sample.sparketl.etl.transformer.character.CharacterTransformer;
import sample.sparketl.etl.transformer.house.HouseTransformer;
import sample.sparketl.model.Book;
import sample.sparketl.model.Character;
import sample.sparketl.model.House;
import sample.sparketl.model.TypedEntity;
import sample.sparketl.spark.functions.TypedEntityTransformer;
import sample.sparketl.writer.impl.PostgresWriter;

@Component
public class TransformManager {
	@Autowired
	PostgresConfig postgresConfig;

	public void transform(Dataset<String> dataStream) throws StreamingQueryException {
		Dataset<TypedEntity> typedStream = dataStream
				.map(TypedEntityTransformer.transform(), Encoders.bean(TypedEntity.class))
				.na().drop()
				.as(Encoders.bean(TypedEntity.class));

		typedStream.filter(functions.col("type").equalTo("books"))
				.map(new BookTransformer().transform(), Encoders.bean(Book.class))
				.na().drop()
				.writeStream()
				.trigger(Trigger.ProcessingTime(postgresConfig.getTrigger()))
				.foreachBatch(new PostgresWriter(postgresConfig, "book").writeDataset())
				.start();

		typedStream.filter(functions.col("type").equalTo("characters"))
				.map(new CharacterTransformer().transform(), Encoders.bean(Character.class))
				.na().drop()
				.writeStream()
				.trigger(Trigger.ProcessingTime(postgresConfig.getTrigger()))
				.foreachBatch(new PostgresWriter(postgresConfig, "character").writeDataset())
				.start();

		typedStream.filter(functions.col("type").equalTo("houses"))
				.map(new HouseTransformer().transform(), Encoders.bean(House.class))
				.na().drop()
				.writeStream()
				.trigger(Trigger.ProcessingTime(postgresConfig.getTrigger()))
				.foreachBatch(new PostgresWriter(postgresConfig, "house").writeDataset())
				.start();
	}
}