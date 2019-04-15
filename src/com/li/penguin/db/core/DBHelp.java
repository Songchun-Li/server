package com.li.penguin.db.core;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DBHelp is used to establish connection to database
 * the parameter for database driver class, url and database user, password
 * are stored in config.properties
 */
public class DBHelp {

    static Properties info = new Properties();

    static {
        //read information from config
        InputStream in = DBHelp.class.getClassLoader().getResourceAsStream("config.properties");
        try {
            //load to info in memory space from files
            info.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Connection getConnection() throws SQLException, ClassNotFoundException {

        Class.forName(info.getProperty("driver"));
        Connection connection = DriverManager.getConnection(info.getProperty("url"), info);
        return connection;
    }

}
