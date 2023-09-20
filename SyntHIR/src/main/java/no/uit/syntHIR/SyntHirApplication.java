package no.uit.syntHIR;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {"no.uit.syntHIR.endpoint", "no.uit.syntHIR.engine"})
@PropertySource("classpath:application.properties")
public class SyntHirApplication {

	public static void main(String[] args) {
		SpringApplication.run(SyntHirApplication.class, args);
	}

}
