package com.li.penguin.interactive;

import com.li.penguin.db.core.JdbcTemplate;
import com.li.penguin.db.core.PreparedStatementCreator;
import com.li.penguin.db.core.RowCallbackHandler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizDAOBean {
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    //
    public List<QuizBean> findAll() {
        List<QuizBean> list = new ArrayList<>();
        final String sql = "select question, choice, answer, appendix from quiz";
        jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection conn) throws SQLException {
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                return preparedStatement;
            }
        }, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet res) throws SQLException {
                QuizBean quizBean =  new QuizBean();
                quizBean.setQuestion(res.getString("question"));
                quizBean.setOption(res.getString("choice"));
                quizBean.setAnswer(res.getString("answer"));
                if (res.getString("appendix") != null) {
                    quizBean.setAppendix(res.getString("appendix"));
                } else {
                    quizBean.setAppendix(null);
                }
                list.add(quizBean);
            }
        });
        return list;

    }
}
