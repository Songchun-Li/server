package com.li.penguin.display;

import com.li.penguin.db.core.JdbcTemplate;
import com.li.penguin.db.core.PreparedStatementCreator;
import com.li.penguin.db.core.RowCallbackHandler;
import com.li.penguin.interactive.QuizBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PenguinDAOBean {

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    //
    public List<PenguinBean> findAll() {
        List<PenguinBean> list = new ArrayList<>();
        final String sql = "select name, hatchyear, gender, species, personality, funfact, bandcolor1, bandcolor2 from penguin";
        jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                return preparedStatement;
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet res) throws SQLException {
                String name = res.getString("name");
                int hatch_year = res.getInt("hatchyear");
                String gender = res.getString("gender");
                String species = res.getString("species");
                String personality = res.getString("personality");
                String fun_fact = res.getString("funfact");
                String band_color1 = res.getString("bandcolor1");
                String band_color2 = res.getString("bandcolor2");

                PenguinBean penguinBean = new PenguinBean();
                penguinBean.setName(name);
                penguinBean.setGender(gender);
                penguinBean.setHatch_year(hatch_year);
                penguinBean.setSpecies(species);
                penguinBean.setPersonality(personality);
                penguinBean.setFun_fact(fun_fact);
                penguinBean.setBand_color1(band_color1);
                penguinBean.setBand_color2(band_color2);

                list.add(penguinBean);
            }
        });
        return list;
    }

    public List<PenguinBean> getNames() {
        List<PenguinBean> list = new ArrayList<>();
        final String sql = "select name from penguin";
        jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                return preparedStatement;
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet res) throws SQLException {
                String name = res.getString("name");
                PenguinBean penguinBean = new PenguinBean();
                penguinBean.setName(name);
                list.add(penguinBean);
            }
        });
        return list;
    }

    public PenguinBean searchPenguinByName(String penguinName) {
        PenguinBean penguinBean = new PenguinBean();
        //final String penguinNameFinal = penguinName;
        final String sql = "select name, hatchyear, gender, species, personality, funfact, bandcolor1, bandcolor2, appendix from penguin where name = '"+penguinName +"'";
        System.out.println(sql);
        jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                //preparedStatement.setString(1,penguinNameFinal);
                return preparedStatement;
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet res) throws SQLException {
                penguinBean.setName(res.getString("name"));
                penguinBean.setGender(res.getString("gender"));
                penguinBean.setBand_color1(res.getString("bandcolor1"));
                penguinBean.setBand_color2(res.getString("bandcolor2"));
                penguinBean.setHatch_year(res.getInt("hatchyear")); //will be 0 if no record

                if (res.getString("species") != null) {
                    penguinBean.setSpecies(res.getString("species"));
                } else {
                    penguinBean.setSpecies(null);
                }

                if (res.getString("personality") != null) {
                    penguinBean.setPersonality(res.getString("personality"));
                } else {
                    penguinBean.setPersonality(null);
                }

                if (res.getString("funfact") != null) {
                    penguinBean.setFun_fact(res.getString("funfact"));
                } else {
                    penguinBean.setFun_fact(null);
                }

                if (res.getString("appendix") != null) {
                    penguinBean.setAppendix(res.getString("appendix"));
                } else {
                    penguinBean.setAppendix(null);
                }

            }
        });

        return penguinBean;
    }

    public PenguinBean getNamebyID(String penguinID){
        PenguinBean penguinBean = new PenguinBean();
        //final String penguinNameFinal = penguinName;
        final String sql = "select name, penguinid from penguin where penguinid = '"+ penguinID +"'";
        System.out.println(sql);
        jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                return preparedStatement;
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet res) throws SQLException {
                penguinBean.setName(res.getString("name"));
                penguinBean.setId(res.getString("penguinid"));
            }
        });

        return penguinBean;

    }


}
