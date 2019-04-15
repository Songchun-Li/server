package com.li.penguin.display;

import com.li.penguin.db.core.JdbcTemplate;
import com.li.penguin.db.core.PreparedStatementCreator;
import com.li.penguin.db.core.RowCallbackHandler;
import com.li.penguin.display.RFIDBean;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RFIDDAOBean {
    private int maxid;
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();



    public int create(RFIDBean rfidBean){
        int rowAffected;
        final String sql = "insert into rfid (date, intime, duration, type, tag, idnum) values ('"
                +rfidBean.getDate()+"','"+rfidBean.getTime()+"','"+rfidBean.getDuration()+"','"
                +rfidBean.getType()+"','"+rfidBean.getTag()+"','"+rfidBean.getId()+"')";
        System.out.println(sql);
        rowAffected  = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                return preparedStatement;
            }
        });
        return rowAffected;
    }


    public int getMaxID(){
        //无条件查询
        final String sql = "select max(id) from rfid";
        jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                return preparedStatement;
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet res) throws SQLException {
                //遍历结果集
                maxid = res.getInt(1);
            }
        });
        return maxid;
    }

    public RFIDBean getRec() {
        RFIDBean rfidBean = new RFIDBean();
        final String sql = "select lastdate, lasttime from record where id = 1";
        jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                return preparedStatement;
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet res) throws SQLException {
                Date lastDate = res.getDate("lastdate");
                Time lastTime = res.getTime("lasttime");
                rfidBean.setDate(lastDate);
                rfidBean.setTime(lastTime);
            }
        });
        return rfidBean;
    }


    public int updateRec(RFIDBean timeUpdater) {
        int rowAffected;
        final String sql = "update record set lastdate= '"+ timeUpdater.getDate() + "' , lasttime= '" + timeUpdater.getTime() +"' where id = 1";
        System.out.println(sql);
        rowAffected  = jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                return preparedStatement;
            }
        });
        return rowAffected;
    }

    public List<RFIDBean> getNew(RFIDBean timeRec) {
        List<RFIDBean> list = new ArrayList<>();
        final String sql = "select idnum from rfid where intime > '"+timeRec.getTime()+"' AND date >= '"+timeRec.getDate()+"'";
        jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);

                return preparedStatement;
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet res) throws SQLException {
                RFIDBean rfidBean = new RFIDBean();
                String id = res.getString("idnum");
                rfidBean.setId(id);
                list.add(rfidBean);

            }
        });
        return list;
    }


    //
    public List<RFIDBean> getUpdate() {
        List<RFIDBean> list = new ArrayList<>();
        final String sql = "select date, intime, duration, type, tag, idnum from rfid where time > ?";
        jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                return preparedStatement;
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet res) throws SQLException {
                RFIDBean rfidBean = new RFIDBean();
                Date date = res.getDate("date");
                Time time = res.getTime("intime");
                Time duration = res.getTime("duration");
                String type = res.getString("type");
                String tag = res.getString("tag");
                String id = res.getString("idnum");
                rfidBean.setDate(date);
                rfidBean.setTime(time);
                rfidBean.setDuration(duration);
                rfidBean.setType(type);
                rfidBean.setTag(tag);
                rfidBean.setId(id);

                list.add(rfidBean);
            }
        });
        return list;
    }


    public RFIDBean getLatest(int maxID) {
        RFIDBean rfidBean = new RFIDBean();
        final String sql = "select date, intime, duration, type, tag, idnum from rfid where id = ?";
        jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setInt(1,maxID);

                return preparedStatement;
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet res) throws SQLException {
                Date date = res.getDate("date");
                Time time = res.getTime("intime");
                Time duration = res.getTime("duration");
                String type = res.getString("type");
                String tag = res.getString("tag");
                String id = res.getString("idnum");

                rfidBean.setDate(date);
                rfidBean.setTime(time);
                rfidBean.setDuration(duration);
                rfidBean.setType(type);
                rfidBean.setTag(tag);
                rfidBean.setId(id);

            }
        });
        return rfidBean;
    }

}
