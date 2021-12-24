package dal;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ResortDao {
    private static HikariDataSource dataSource;

    public ResortDao() {
        dataSource = HikariSkierDataSource.getDataSource();
    }

    public int getNumSkiersOnResortDay(int resortId, int dayId) {
        int numSkiers = 0;
        System.out.println(" resortDao: resortId=" + resortId + "dayId" + dayId);
        String query = "SELECT COUNT(DISTINCT(SkierId)) AS NumSkiers " +
                "FROM LiftRides " +
//                "WHERE RessortId=? AND DayId=?;";
                "WHERE DayId=?;";  // resortId is constant

        try {
            Connection conn = dataSource.getConnection();
            PreparedStatement preparedStatement = conn.prepareStatement(query);
//            preparedStatement.setInt(1, resortId);
            preparedStatement.setInt(1, dayId);
            ResultSet results = preparedStatement.executeQuery();
            if (results.next()) {
                numSkiers += results.getInt("NumSkiers");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return numSkiers;
    }
}
