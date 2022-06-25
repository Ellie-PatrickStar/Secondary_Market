package com.example.secondary_market;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class JDBC {
    private final static String driver= "com.mysql.jdbc.Driver";
    private final static String sqlName="root";
    private final static String sqlPassword="LYHWYZZNSB.wan1/";
    private final static String url="jdbc:mysql://123.57.42.220:3306/test?characterEncoding=utf-8";
    private static Connection conn=null;

    public Connection oKHttpjdbc(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName(driver);
                    conn= DriverManager.getConnection(url,sqlName,sqlPassword);
                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        if (conn==null){
            System.out.println("null");
        }
        return conn;
    }
}
