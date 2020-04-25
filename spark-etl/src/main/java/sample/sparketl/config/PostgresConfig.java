package sample.sparketl.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "postgres")
public class PostgresConfig {
    private String url;
    private String user;
    private String password;
    private String driver;
    private String trigger;
}