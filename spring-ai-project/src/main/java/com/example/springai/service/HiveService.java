package com.example.springai.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class HiveService {

    private final JdbcTemplate primaryHiveJdbcTemplate;
    private final JdbcTemplate secondaryHiveJdbcTemplate;

    public HiveService(
            @Qualifier("primaryHiveJdbcTemplate") JdbcTemplate primaryHiveJdbcTemplate,
            @Qualifier("secondaryHiveJdbcTemplate") JdbcTemplate secondaryHiveJdbcTemplate) {
        this.primaryHiveJdbcTemplate = primaryHiveJdbcTemplate;
        this.secondaryHiveJdbcTemplate = secondaryHiveJdbcTemplate;
    }

    /**
     * Execute a Hive query on the primary Hive connection
     * 
     * @param query The HiveQL query to execute
     * @return List of results as maps
     */
    public List<Map<String, Object>> executePrimaryQuery(String query) {
        return primaryHiveJdbcTemplate.queryForList(query);
    }
    
    /**
     * Execute a Hive query on the secondary Hive connection
     * 
     * @param query The HiveQL query to execute
     * @return List of results as maps
     */
    public List<Map<String, Object>> executeSecondaryQuery(String query) {
        return secondaryHiveJdbcTemplate.queryForList(query);
    }
    
    /**
     * Execute a Hive update statement on the primary Hive connection
     * 
     * @param sql The HiveQL statement to execute
     * @return The number of rows affected
     */
    public int executePrimaryUpdate(String sql) {
        return primaryHiveJdbcTemplate.update(sql);
    }
    
    /**
     * Execute a Hive update statement on the secondary Hive connection
     * 
     * @param sql The HiveQL statement to execute
     * @return The number of rows affected
     */
    public int executeSecondaryUpdate(String sql) {
        return secondaryHiveJdbcTemplate.update(sql);
    }
    
    /**
     * Check if a table exists in the primary Hive connection
     * 
     * @param database The database name
     * @param tableName The table name
     * @return true if the table exists, false otherwise
     */
    public boolean primaryTableExists(String database, String tableName) {
        String query = "SHOW TABLES IN " + database + " LIKE '" + tableName + "'";
        List<Map<String, Object>> result = primaryHiveJdbcTemplate.queryForList(query);
        return !result.isEmpty();
    }
    
    /**
     * Check if a table exists in the secondary Hive connection
     * 
     * @param database The database name
     * @param tableName The table name
     * @return true if the table exists, false otherwise
     */
    public boolean secondaryTableExists(String database, String tableName) {
        String query = "SHOW TABLES IN " + database + " LIKE '" + tableName + "'";
        List<Map<String, Object>> result = secondaryHiveJdbcTemplate.queryForList(query);
        return !result.isEmpty();
    }
}