package com.quiz.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    public static Connection getConnection() {

        Connection con = null;

        try {
            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Create Connection
            con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/quiz_engine",
                "root",
                "netaji"
            );

            System.out.println("Database Connected Successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }
}