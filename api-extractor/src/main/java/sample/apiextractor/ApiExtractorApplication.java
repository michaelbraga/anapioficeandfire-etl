package sample.apiextractor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sample.apiextractor.services.Crawler;

@Log4j2
@SpringBootApplication
public class ApiExtractorApplication implements CommandLineRunner {

	@Autowired
	private Crawler crawler;

	public static void main(String[] args) {
		SpringApplication.run(ApiExtractorApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("========================================");
		log.info("    ===== STARTING CRAWLER APP =====");
		log.info("========================================");

		crawler.crawl("https://www.anapioficeandfire.com/api");

		log.info("========================================");
		log.info("            ===== DONE =====");
		log.info("========================================");

	}
}
