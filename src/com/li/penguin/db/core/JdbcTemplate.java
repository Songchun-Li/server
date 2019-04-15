package com.li.penguin.db.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTemplate {
    //Template design
    //query for read, update for CUD

    /**
     * Execute query operation
     * @param psCreator prepared statement creator
     * @param callbackHandler result set operator
     * @throws DataAcessException
     */
    public void query(PreparedStatementCreator psCreator,
                      RowCallbackHandler callbackHandler) throws DataAcessException {
        //
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DBHelp.getConnection();
            preparedStatement = psCreator.createPreparedStatement(connection);// override in implementation
            resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet);
            //go through result set
            while (resultSet.next()){
                callbackHandler.processRow(resultSet);
                // This method will be rewritten in design
            }

        } catch(SQLException e) {
            throw new DataAcessException("SQLException in JdbcTemplate", e);
        } catch (ClassNotFoundException e) {
            throw new DataAcessException("ClassNotFoundException in JdbcTemplate", e);
        } finally{
            // releasing resources
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new DataAcessException("Unable to close DB connection in JdbcTemplate", e);
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new DataAcessException("Unable to release prepared statement in JdbcTemplate", e);
                }
            }

            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    throw new DataAcessException("Unable to close result set in JdbcTemplate", e);
                }
            }

        }
    }

    /**
     * Update database
     * @param psCreator prepared statement creator
     * @throws DataAcessException
     */
    public int update(PreparedStatementCreator psCreator) throws DataAcessException {
        //
        int rowAffected;
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DBHelp.getConnection(); // This method will be rewritten in design
            preparedStatement = psCreator.createPreparedStatement(connection);
            rowAffected = preparedStatement.executeUpdate();


        } catch(SQLException e) {
            throw new DataAcessException("SQLException in JdbcTemplate", e);
        } catch (ClassNotFoundException e) {
            throw new DataAcessException("ClassNotFoundException in JdbcTemplate", e);
        } finally{
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    throw new DataAcessException("Unable to close DB connection in JdbcTemplate", e);
                }
            }

            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    throw new DataAcessException("Unable to release prepared statement in JdbcTemplate", e);
                }
            }


        }

        return rowAffected;
    }



}
