package br.com.gpiagentini.api.infraestructure.configuration.rest;

import br.com.gpiagentini.api.infraestructure.configuration.rest.handlers.FinancialTemplateErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class FinancialTemplate {

    @Value("${application.finance.api.key}")
    private String key;
    @Value("${application.finance.api.url}")
    private String url;

    @Bean
    public RestTemplate financialRestTemplate(RestTemplateBuilder builder) {
        return builder
                .rootUri(url)
                .errorHandler(new FinancialTemplateErrorHandler())
                .setConnectTimeout(Duration.ofSeconds(10))
                .additionalInterceptors((HttpRequest request, byte[] body, ClientHttpRequestExecution execution) -> {
                    request.getHeaders().set("accept", "application/json");
                    request.getHeaders().set("X-API-KEY", key);
                    return execution.execute(request, body);
                })
                .build();
    }
}
