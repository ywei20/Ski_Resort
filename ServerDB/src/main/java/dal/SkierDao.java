package dal;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SkierDao {
    private static HikariDataSource dataSource;

    public SkierDao() {
        dataSource = HikariSkierDataSource.getDataSource();
    }

    public int getSkierVerticalOnDay(int skierId, int seasonId, int dayId) {
        int vertical = 0;
        String query = "SELECT SUM(Vertical) AS VerValue " +
                "FROM LiftRides "+
                "WHERE SkierId=? AND SeasonId=? AND DayId=?;";
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, skierId);
            preparedStatement.setInt(2, seasonId);
            preparedStatement.setInt(3, dayId);
            ResultSet results = preparedStatement.executeQuery();
            if (results.next()) {
                vertical += results.getInt("VerValue");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vertical;
    }

    public int getTotalVerticalOnSeason(int skierId, int seasonId) {
        int vertical = 0;
        String query = "SELECT SUM(Vertical) AS VerValue " +
                "FROM LiftRides "+
                "WHERE SkierId=? AND SeasonId=?;";
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, skierId);
            preparedStatement.setInt(2, seasonId);
            ResultSet results = preparedStatement.executeQuery();
            if (results.next()) {
                vertical += results.getInt("VerValue");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vertical;
    }

    public int getTotalVerticalAllSeasons(int skierId) {
        int vertical = 0;
        String query = "SELECT SUM(Vertical) AS VerValue " +
                "FROM LiftRides "+
                "WHERE SkierId=?;";
        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, skierId);
            ResultSet results = preparedStatement.executeQuery();
            if (results.next()) {
                vertical += results.getInt("VerValue");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vertical;
    }


}
