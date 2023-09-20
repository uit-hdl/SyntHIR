package no.uit.syntHIR.FHIRServer.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"no.uit.syntHIR.FHIRServer.endpoint", "no.uit.syntHIR.FHIRServer.engine"})
@EnableCaching
public class FhirServerServicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FhirServerServicesApplication.class, args);
	}

}
