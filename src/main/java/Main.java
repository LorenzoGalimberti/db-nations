
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import java.text.DecimalFormat;

public class Main {
    // parametri di connessione al db
    private final static String DB_URL = "jdbc:mysql://localhost:3306/db_nations";
    private final static String DB_USER = "root";
    private final static String DB_PASSWORD = "root";
    // query da esguire
    private final static String SQL_STATS= "select c.country_id, c.name as CountryName, group_concat(l.language) as Languages, s.year as LatestYear, s.population AS LatestPopulation, s.gdp as LatestGdp\n" +
            "from country_languages cl\n" +
            "join countries c on c.country_id = cl.country_id\n" +
            "join languages l on l.language_id = cl.language_id\n" +
            "join (\n" +
            "    select country_id, max(year) as year\n" +
            "    from country_stats\n" +
            "    group by country_id\n" +
            ") max_stats on c.country_id = max_stats.country_id\n" +
            "join country_stats s on c.country_id = s.country_id and max_stats.year = s.year\n" +
            "where c.country_id = ? group by c.country_id, c.name, s.year, s.population;";
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

        // QUERY PER LE STATISTICHE
            System.out.print("Inserisci l 'ID della ricerca : ");
            int choiche= scan.nextInt();
            scan.nextLine();
            try (PreparedStatement ps = conn.prepareStatement(SQL_STATS)){
                ps.setInt(1,choiche);
                try (ResultSet rs = ps.executeQuery()){

                    while (rs.next()){
                        int id= rs.getInt("country_id");
                        String name = rs.getString("CountryName");
                        String languages=rs.getString("Languages");
                        int maxYear= rs.getInt("LatestYear");
                        int latestPopulation= rs.getInt("LatestPopulation");
                        double latestGdp= rs.getDouble("LatestGdp");

                        System.out.println("Details for country : "+name);
                        System.out.println("Languages : "+languages);
                        System.out.println("Most recent stats ");
                        System.out.println("Year : " +maxYear);
                        System.out.println("Population : "+ latestPopulation);
                        System.out.println("GDP : "+latestGdp);

                    }
                }
            } catch (SQLException e){
                System.out.println("Errore");
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
