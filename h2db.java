import java.sql.*;
public class h2db {
    public static void main(String[] a) throws Exception {
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection("jdbc:h2:~/test", "root", "0000");
        Statement stmt = conn.createStatement();
        PreparedStatement ps;
        ResultSet rs;

        stmt.execute("CREATE TABLE IF NOT EXISTS DATA(ID INT NOT NULL AUTO_INCREMENT, OWNER VARCHAR(255), ITEM VARCHAR(255), AMOUNT INT)");
        stmt.execute("CREATE TABLE IF NOT EXISTS USER(ACCOUNT VARCHAR(255), PASSWORD VARCHAR(255))");


        switch(a[0]){
            case "login":
                ps = conn.prepareStatement("SELECT * FROM USER WHERE ACCOUNT = ?");
                ps.setString(1, a[1]);
                rs = ps.executeQuery();
                //rs = stmt.executeQuery("SELECT * FROM USER WHERE ACCOUNT = '" + a[1] + "'");
                while (rs.next()) {
                  System.out.print(rs.getString("PASSWORD"));
                }
                break;
            case "register":
                ps = conn.prepareStatement("SELECT * FROM USER WHERE ACCOUNT = ?");
                ps.setString(1, a[1]);
                rs = ps.executeQuery();
                //rs = stmt.executeQuery("SELECT * FROM USER WHERE ACCOUNT = '" + a[1] + "'");
                if (!rs.next()){
                    ps = conn.prepareStatement("INSERT INTO USER(ACCOUNT, PASSWORD) VALUES(?,?)");
                    ps.setString(1, a[1]);
                    ps.setString(2, a[2]);
                    ps.execute();
                    //stmt.execute("INSERT INTO USER(ACCOUNT, PASSWORD) VALUES('" + a[1] + "','" + a[2] + "')");
                }else System.out.print("used");
                break;
            case "load":
                ps = conn.prepareStatement("SELECT * FROM DATA WHERE OWNER = ?");
                ps.setString(1, a[1]);
                rs = ps.executeQuery();
                //rs = stmt.executeQuery("SELECT * FROM DATA WHERE OWNER = '" + a[1] + "'");
                while (rs.next()) {
                  System.out.print(rs.getString("ID") + "," + rs.getString("ITEM") + "," + rs.getString("AMOUNT") + ";");
                }
                break;
            case "add":
                ps = conn.prepareStatement("INSERT INTO DATA(OWNER, ITEM, AMOUNT) VALUES(?,?,?)");
                ps.setString(1, a[1]);
                ps.setString(2, a[2]);
                ps.setString(3, a[3]);
                ps.execute();
                //stmt.execute("INSERT INTO DATA(OWNER, ITEM, AMOUNT) VALUES('" + a[1] + "','" + a[2] + "'," + a[3] + ")");
                break;
            case "delete":
                ps = conn.prepareStatement("DELETE FROM DATA WHERE ID = ?");
                ps.setString(1, a[1]);
                ps.execute();
                //stmt.execute("DELETE FROM DATA WHERE ID = " + a[1]);
                break;
            case "update":
                ps = conn.prepareStatement("UPDATE DATA SET AMOUNT=? WHERE ID=?");
                ps.setString(1, a[1]);
                ps.setString(2, a[2]);
                ps.execute();
                //stmt.execute("UPDATE DATA SET AMOUNT=" + a[1] + " WHERE ID=" + a[2]);
                break;
        }
        conn.close();
    }
}