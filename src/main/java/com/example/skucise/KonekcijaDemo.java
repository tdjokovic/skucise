import java.sql.*;

public class KonekcijaDemo {
    public static void main(String[] args) throws SQLException
    {
        //OVAJ FAJL GOVORI O TOME KAKO SE POVEZATI NA BAZU PODATAKA I OSTVARITI SA NJOM KOMUNIKACIJU

        Connection conn = null;
        Statement stmt = null;

        System.out.println("Connecting...!");

        conn = DriverManager.getConnection(
                "jdbc:mariadb://localhost:3307/skucise",
                "root",
                ""
        );

        System.out.println("Connected.");

        stmt = conn.createStatement();
        String sql;

        System.out.println("Creating table...");

        sql = "CREATE TABLE student (id INTEGER not NULL AUTO_INCREMENT, " +
                " first VARCHAR(30), " +
                " last VARCHAR(30), " +
                " age INTEGER, " +
                " PRIMARY KEY (id))";
        /*
        stmt.execute(sql);
        */

        System.out.println("Inserting row...");

        sql = "INSERT INTO student (first,last,age) VALUES ('Petar','Petrovic',25)";

        //stmt.execute(sql);

        System.out.println("Row inserted.");

        System.out.println("Deleting row...");

        sql = "DELETE FROM student WHERE id = 4";
        //execute update vraca broj izmenjenih redova
        //dakle nula ce da se vrati ako ne postoji ni jedan red koji je obrisan
        //stmt.executeUpdate(sql);

        System.out.println("Row deleted.");


        System.out.println("Updating table...");

        sql = "UPDATE student SET first='Tijana' WHERE id=3";
        stmt.executeUpdate(sql);

        System.out.println("Table updated.");

        System.out.println("Selecting...");

        sql = "SELECT * FROM student";
        ResultSet res = null;
        //executequery vraca selektovano u resultset
        res = stmt.executeQuery(sql);
        int brojRedova = 0;
        while(res.next()){
            brojRedova++;
            System.out.println("Red "+brojRedova);
            System.out.println("Id "+res.getInt("id"));
            System.out.println("First name "+res.getString("first"));
            System.out.println("Last name "+res.getString("last"));
            System.out.println("Age "+res.getInt("age"));

        }

        System.out.println("That's it.");
    }
}
