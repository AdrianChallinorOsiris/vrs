package uk.co.osiris.vrs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
@Slf4j
@EnableJpaRepositories("uk.co.osiris")
public class VrsApplication {

	static void main(String[] args) {
        String version = getVersion();
        log.info("Starting VRS application version {}", version);
		SpringApplication.run(VrsApplication.class, args);
	}

    private static String getVersion() {
        try (InputStream is = VrsApplication.class.getResourceAsStream("/application.yaml")) {
            if (is != null) {
                Properties props = new Properties();
                props.load(is);
                return props.getProperty("version", "unknown");
            }
        } catch (Exception e) {
            log.warn("Could not read version from application.yaml", e);
        }
        return "unknown";
    }
}
