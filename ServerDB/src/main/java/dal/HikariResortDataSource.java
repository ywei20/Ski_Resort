package dal;

import com.zaxxer.hikari.HikariDataSource;

public class HikariResortDataSource {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String HOST_NAME = "resorts.cocji8i2y47b.us-east-1.rds.amazonaws.com";
    private static final String PORT = "3306";
    private static final String DATABASE = "db_resorts";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password";

    private static final HikariDataSource dataSource = new HikariDataSource();
    static {
        String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", HOST_NAME, PORT, DATABASE);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setDriverClassName(JDBC_DRIVER);

        dataSource.setMaximumPoolSize(250);
        dataSource.setConnectionTimeout(30000);
        dataSource.setLeakDetectionThreshold(30000);
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }
}
