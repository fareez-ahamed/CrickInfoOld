/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package crickinfo;


import java.io.*;
import java.sql.*;

/**
 *
 * @author Dev
 */
public class Test1 {

    public static void main(String args[])throws Exception
    {
        String temp;
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        Connection con = DriverManager.getConnection("jdbc:odbc:blazers");
        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("select * from players where status='P'");
        while(rs.next())
        {
            System.out.println(rs.getString("name"));
        }
    }
}
