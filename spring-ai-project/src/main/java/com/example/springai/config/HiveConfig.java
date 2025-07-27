package com.example.springai.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class HiveConfig {

    @Primary
    @Bean(name = "primaryHiveDataSource")
    @ConfigurationProperties(prefix = "hive.datasources.primary")
    public DataSource primaryHiveDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "secondaryHiveDataSource")
    @ConfigurationProperties(prefix = "hive.datasources.secondary")
    public DataSource secondaryHiveDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "primaryHiveJdbcTemplate")
    public JdbcTemplate primaryHiveJdbcTemplate(@Qualifier("primaryHiveDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean(name = "secondaryHiveJdbcTemplate")
    public JdbcTemplate secondaryHiveJdbcTemplate(@Qualifier("secondaryHiveDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}