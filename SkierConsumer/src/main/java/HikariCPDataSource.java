import com.zaxxer.hikari.HikariDataSource;

public class HikariCPDataSource {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String HOST_NAME = "skiers.c2gbmcsla0al.us-west-2.rds.amazonaws.com";
    private static final String PORT = "3306";
    private static final String DATABASE = "db_skiers";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "password";

    private static final HikariDataSource dataSource = new HikariDataSource();
    static {
        String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", HOST_NAME, PORT, DATABASE);
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setDriverClassName(JDBC_DRIVER);

        dataSource.setMaximumPoolSize(60);
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }
}
