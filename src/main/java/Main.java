
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class Main {
    // parametri di connessione al db
    private final static String DB_URL = "jdbc:mysql://localhost:3306/db_nations";
    private final static String DB_USER = "root";
    private final static String DB_PASSWORD = "root";
    // query da esguire
    private final static String SQL_NATIONS = "select c.name as country_name  ,c.country_id as country_id, r.name as region_name , c2.name as continent_name\n" +
            "from countries c \n" +
            "join regions r on c.region_id  =r.region_id \n" +
            "join continents c2 on c2.continent_id =r.continent_id \n" +
            "order by c.name asc;";
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            try (PreparedStatement ps = connection.prepareStatement(SQL_NATIONS);
                 ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("country_id");
                    String name = rs.getString("country_name");
                    String regionName = rs.getString("region_name");
                    String continentName = rs.getString("continent_name");
                    System.out.println("Nation : "+name +"  ---> nation id : "+ id + "  region name : "+regionName+ "  continent name : "+continentName);
                }
            }




        } catch (SQLException exception) {
            System.out.println("An error occurred");
        }

    }
}
