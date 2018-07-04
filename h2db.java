import java.sql.*;
public class h2db {
    public static void main(String[] a) throws Exception {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "root", "0000");
        Statement stmt = conn.createStatement();
        ResultSet rs;

        switch(a[0]){
            case "login":
                rs = stmt.executeQuery("SELECT * FROM USER WHERE ACCOUNT = '" + a[1] + "'");
                while (rs.next()) {
                  System.out.print(rs.getString("PASSWORD"));
                }
                break;
            case "load":
                rs = stmt.executeQuery("SELECT * FROM DATA WHERE OWNER = '" + a[1] + "'");
                while (rs.next()) {
                  System.out.print(rs.getString("ID") + "," + rs.getString("ITEM") + "," + rs.getString("AMOUNT") + ";");
                }
                break;
            case "add":
                stmt.execute("INSERT INTO DATA(OWNER, ITEM, AMOUNT) VALUES('" + a[1] + "','" + a[2] + "'," + a[3] + ")");
                break;
            case "delete":
                stmt.execute("DELETE FROM DATA WHERE ID = " + a[1]);
                break;
            case "update":
                stmt.execute("UPDATE DATA SET AMOUNT=" + a[1] + " WHERE ID=" + a[2]);
                break;
        }
        conn.close();
    }
}