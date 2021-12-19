import com.zaxxer.hikari.HikariDataSource;
import java.sql.*;
import org.apache.commons.dbcp2.*;

public class LiftRideDao {
    private static HikariDataSource dataSource;

    public LiftRideDao() {
        dataSource = HikariCPDataSource.getDataSource();
    }

    public void createLiftRide(LiftRide newLiftRide) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        String insertQueryStatement = "INSERT INTO LiftRides (SkierId, RessortId, DayId, LiftId, Time) " +
                "VALUES (?,?,?,?,?)";
        try {
            conn = dataSource.getConnection();
            preparedStatement = conn.prepareStatement(insertQueryStatement);
            preparedStatement.setInt(1, newLiftRide.getSkierID());
            preparedStatement.setInt(2, newLiftRide.getResortID());
            preparedStatement.setInt(3, newLiftRide.getDayID());
            preparedStatement.setInt(4, newLiftRide.getLiftID());
            preparedStatement.setInt(5, newLiftRide.getTime());

            // execute insert SQL statement
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

//    public static void main(String[] args) {
//        LiftRideDao liftRideDao = new LiftRideDao();
//        liftRideDao.createLiftRide(new LiftRide(10, 2, 5, 5, 500));
//    }


}