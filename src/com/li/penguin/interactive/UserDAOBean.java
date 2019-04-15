package com.li.penguin.interactive;

import com.li.penguin.db.core.JdbcTemplate;
import com.li.penguin.db.core.PreparedStatementCreator;
import com.li.penguin.db.core.RowCallbackHandler;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOBean {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    //
    public List<UserBean> findAll() {

        List<UserBean> list = new ArrayList<UserBean>();

        String sql = "select name, score, date from leaderboard";
        jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                return preparedStatement;
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet res) throws SQLException {//遍历结果集
                int score = res.getInt("score");
                String name = res.getString("name");
                String date = res.getString("date");
                UserBean userBean = new UserBean();
                userBean.setScore(score);
                userBean.setName(name);
                userBean.setDate(date);
                list.add(userBean);
            }
        });
        return list;
    }

    public int create(UserBean userBean){
        int rowAffected;
        final String sql = "insert into leaderboard (name, score, date) values (?,?,?) ";
        rowAffected  = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1,userBean.getName());
                preparedStatement.setInt(2,userBean.getScore());
                preparedStatement.setString(3, userBean.getDate());
                return preparedStatement;
            }
        });
        return rowAffected;
    }

    public List<UserBean> findByName(String userName) {
        List<UserBean> list = new ArrayList<>();
        final String sql = "select name, score, date from leaderboard where name = '"+ userName + "'";
        System.out.println(sql);
        jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                return preparedStatement;
            }
        },  new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet res) throws SQLException {
                //get answer back
                UserBean userBean = new UserBean();
                int score = res.getInt("score");
                String name = res.getString("name");
                String date = res.getString("date");
                userBean.setScore(score);
                userBean.setName(name);
                userBean.setDate(date);
                list.add(userBean);
            }
        });
        return list;
    }

    public void removeByPk(int userid){
        final String sql = "delete from leaderboard where id = ?";
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1,userid);
                return preparedStatement;
            }
        });
    }
}
