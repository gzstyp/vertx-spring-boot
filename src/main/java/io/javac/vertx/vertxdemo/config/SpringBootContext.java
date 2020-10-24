package io.javac.vertx.vertxdemo.config;

import org.springframework.context.ConfigurableApplicationContext;

public class SpringBootContext {
    private static ConfigurableApplicationContext applicationContext;

    public static ConfigurableApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(final ConfigurableApplicationContext applicationContext) {
        SpringBootContext.applicationContext = applicationContext;
    }
}