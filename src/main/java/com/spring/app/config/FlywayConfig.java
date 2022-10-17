package com.spring.app.config;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {
    @Bean
    public FlywayMigrationStrategy strategy() {
        return flyway -> {
            // マイグレーション実行
            flyway.migrate();
        };
    }
}
