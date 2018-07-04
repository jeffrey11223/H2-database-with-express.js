import java.sql.*;
public class h2db {
    public static void main(String[] a) throws Exception {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "root", "0000");
        Statement stmt = conn.createStatement();
        ResultSet rs;

        stmt.execute("CREATE TABLE IF NOT EXISTS DATA(ID INT NOT NULL AUTO_INCREMENT, OWNER VARCHAR(255), ITEM VARCHAR(255), AMOUNT INT)");
        stmt.execute("CREATE TABLE IF NOT EXISTS USER(ACCOUNT VARCHAR(255), PASSWORD VARCHAR(255))");


        switch(a[0]){
            case "login":
                rs = stmt.executeQuery("SELECT * FROM USER WHERE ACCOUNT = '" + a[1] + "'");
                while (rs.next()) {
                  System.out.print(rs.getString("PASSWORD"));
                }
                break;
            case "register":
                rs = stmt.executeQuery("SELECT * FROM USER WHERE ACCOUNT = '" + a[1] + "'");
                if (!rs.next()){
                    stmt.execute("INSERT INTO USER(ACCOUNT, PASSWORD) VALUES('" + a[1] + "','" + a[2] + "')");
                }else System.out.print("used");
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