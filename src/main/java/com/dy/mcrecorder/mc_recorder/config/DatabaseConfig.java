package com.dy.mcrecorder.mc_recorder.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    // ========== MySQL 主 DataSource ==========
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties mysqlDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource mysqlDataSource(@Qualifier("mysqlDataSourceProperties") DataSourceProperties mysqlDataSourceProperties) {
        return mysqlDataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    @Primary
    public JdbcTemplate mysqlJdbcTemplate(@Qualifier("mysqlDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // ========== pgvector 副 DataSource ==========
    @Bean
    @ConfigurationProperties("spring.datasource.pgvector")
    public DataSourceProperties pgvectorDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    public DataSource pgvectorDataSource(@Qualifier("pgvectorDataSourceProperties") DataSourceProperties pgvectorDataSourceProperties) {
        return pgvectorDataSourceProperties.initializeDataSourceBuilder().build();
    }

    @Bean
    public JdbcTemplate pgvectorJdbcTemplate(@Qualifier("pgvectorDataSource") DataSource pgvectorDataSource) {
        return new JdbcTemplate(pgvectorDataSource);
    }
}