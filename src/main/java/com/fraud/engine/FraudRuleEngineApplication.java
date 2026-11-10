package com.fraud.engine;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FraudRuleEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(FraudRuleEngineApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fraud Rule Engine API")
                        .version("1.0.0")
                        .description("Production-grade fraud detection system using rule-based processing for transaction analysis")
                        .contact(new Contact()
                                .name("Fraud Detection Team")
                                .email("fraud-team@company.com")));
    }
}
