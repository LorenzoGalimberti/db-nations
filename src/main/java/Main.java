
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

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
            "where c.name like ? order by c.name asc;";
    public static void main(String[] args) {
        Scanner scan= new Scanner(System.in);
        System.out.print("Inserisci il termine di ricerca della nazione : ");
        String search = scan.nextLine();
        try(Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)){
            try (PreparedStatement ps = conn.prepareStatement(SQL_NATIONS)) {
                ps.setString(1, "%"+search+"%");
                try (ResultSet rs = ps.executeQuery()) {

                    boolean contiene = false;

                    while (rs.next()) {
                        int id = rs.getInt("country_id");
                        String name = rs.getString("country_name");
                        String regionName = rs.getString("region_name");
                        String continentName = rs.getString("continent_name");

                        if (!contiene) {
                            System.out.println("Nazioni trovate:");
                            contiene = true; // Impostiamo a true quando troviamo il primo risultato
                        }

                        System.out.println("Nation: " + name + " ---> nation id : " + id + "  region name : " + regionName + "  continent name : " + continentName);
                    }

                    if (!contiene) {
                        System.out.println("Nessuna nazione trovata per i termini di ricerca.");
                    }


                }
            } catch (SQLException exception) {
                System.out.println("Errore .");
            }


        } catch(SQLException e){
            System.out.println("OOPS an error occurred");
        }

        /*
        * try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, choice);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {


                } else {
                    System.out.println("Evento non trovato.");
                }
            }
        } catch (SQLException exception) {
            System.out.println("Errore durante la prenotazione dell'evento.");
        }*/
        scan.close();
    }
}
