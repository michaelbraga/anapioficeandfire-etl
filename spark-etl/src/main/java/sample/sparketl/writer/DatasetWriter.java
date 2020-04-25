package sample.sparketl.writer;

import org.apache.spark.api.java.function.VoidFunction2;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface DatasetWriter {
    VoidFunction2<Dataset<Row>, Long> writeDataset();
}