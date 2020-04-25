package sample.sparketl.writer.impl;

import org.apache.spark.api.java.function.VoidFunction2;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import sample.sparketl.config.PostgresConfig;
import sample.sparketl.writer.DatasetWriter;

import java.io.Serializable;

public class PostgresWriter implements DatasetWriter, Serializable {
    private final static long serialVersionUID = 600L;
    private String url, user, password, table, driver;

    public PostgresWriter(PostgresConfig postgresConfig, String table) {
        url = postgresConfig.getUrl();
        user = postgresConfig.getUser();
        password = postgresConfig.getPassword();
        driver = postgresConfig.getDriver();
        this.table = table;
    }

    @Override
    public VoidFunction2<Dataset<Row>, Long> writeDataset() {
        return new VoidFunction2<Dataset<Row>, Long>() {
            private final static long serialVersionUID = 600L;
            @Override
            public void call(Dataset<Row> rowDataset, Long aLong) throws Exception {
                rowDataset.distinct()
                        .write()
                        .format("jdbc")
                        .mode(SaveMode.Append)
                        .option("driver", driver)
                        .option("url", url)
                        .option("dbtable", table)
                        .option("user", user)
                        .option("password", password)
                        .save();
            }
        };
    }
}