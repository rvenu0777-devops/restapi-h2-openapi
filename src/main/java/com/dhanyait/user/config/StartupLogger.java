package com.dhanyait.user.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class StartupLogger implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger log = LoggerFactory.getLogger(StartupLogger.class);

    private final Environment env;

    public StartupLogger(Environment env) {
        this.env = env;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        String port = env.getProperty("local.server.port", env.getProperty("server.port", "8080"));
        log.info("\n------------------------------------------------------------\n" +
                        "Application is ready! Useful URLs:\n" +
                        "Swagger UI:     http://localhost:{}/swagger-ui.html\n" +
                        "OpenAPI JSON:   http://localhost:{}/v3/api-docs\n" +
                        "H2 Console:     http://localhost:{}/h2-console\n" +
                        "------------------------------------------------------------",
                port, port, port);
    }
}