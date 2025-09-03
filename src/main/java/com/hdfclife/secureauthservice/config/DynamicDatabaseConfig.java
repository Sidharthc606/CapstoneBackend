package com.hdfclife.secureauthservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Configures dynamic PostgreSQL database connection.
 * Creates the target DB if it doesn't exist.
 *
 * @author Sidharth Chaudhary
 */
@Configuration
public class DynamicDatabaseConfig {

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPass;

    @Value("${spring.datasource.url}")
    private String Url;

    @Value("${spring.datasource.target-db}")
    private String targetDb;

    /**
     * Initializes and returns the DataSource.
     *
     * @return DataSource for target DB
     * @throws SQLException if DB access fails
     */
    @Bean
    public DataSource dataSource() throws SQLException {

        String baseUrl = Url.substring(0, Url.lastIndexOf("/") + 1);  // to extract base jdbc URL.
        String targetDbUrl = baseUrl + targetDb;

        try (Connection conn = DriverManager.getConnection(Url, dbUser, dbPass)) {
            ResultSet rs = conn.createStatement().executeQuery(
                    "SELECT 1 FROM pg_database WHERE datname = '" + targetDb + "'");
            if (!rs.next()) {
                conn.createStatement().executeUpdate("CREATE DATABASE " + targetDb);
                System.out.println("Created database: " + targetDb);
            } else {
                System.out.println("Database already exists: " + targetDb);
            }
        }

        return DataSourceBuilder.create()
                .url(targetDbUrl)
                .username(dbUser)
                .password(dbPass)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}
