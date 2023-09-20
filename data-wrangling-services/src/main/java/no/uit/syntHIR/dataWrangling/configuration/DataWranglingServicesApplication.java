package no.uit.syntHIR.dataWrangling.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"no.uit.syntHIR.dataWrangling.endpoint", "no.uit.syntHIR.dataWrangling.engine", "no.uit.syntHIR.dataWrangling.persistence"})
@Import({AzureStorageConfiguration.class})
public class DataWranglingServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataWranglingServicesApplication.class, args);
	}

}
