package com.li.penguin.db.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 处理结果集的对象
 */
public interface RowCallbackHandler {
    void processRow(ResultSet res) throws SQLException;

}
