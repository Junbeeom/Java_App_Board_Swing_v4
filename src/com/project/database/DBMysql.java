package com.project.database;

import com.project.board.Common;

import java.sql.*;
import java.io.StringReader;

public class DBMysql {
    Common common = new Common();

    public DBMysql() {
    }

    private String url = "jdbc:mysql://localhost:3306/board";
    private String user = "root";
    private String password = "26905031";
    private Connection conn = null;
    private PreparedStatement pstmt = null;
    private ResultSet resultSet = null;

    //created method to INSERT DB
    public int dbCreated(String tittle, String content, String name) throws SQLException {
        common.COMMON_Result = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

            String sql = "INSERT INTO board(tittle, content, name) VALUES(?, ?, ?)";

            pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            StringReader srTitle = new StringReader(tittle);
            StringReader srName = new StringReader(name);

            pstmt.setCharacterStream(1, srTitle, 20);
            pstmt.setString(2, content);
            pstmt.setCharacterStream(3, srName, 12);

            common.COMMON_Result = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(pstmt != null) {
                pstmt.close();
            }
            if(conn != null) {
                conn.close();
            }
        }

        return common.COMMON_Result;
    }

    public ResultSet dbListed() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

            String sql = "SELECT * FROM board where is_deleted IS FALSE ORDER BY created_ts DESC";

            pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            resultSet = pstmt.executeQuery();
        } catch(Exception e) {
            e.printStackTrace();
        }
       return resultSet;
    }


    //delted method to DELETE DB
    public int dbDeleted(int no) throws SQLException {
        common.COMMON_Result = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

            String sql = "UPDATE board SET deleted_ts = CURRENT_TIMESTAMP(), is_deleted = TRUE WHERE board_no = ?";

            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, no);

            common.COMMON_Result = pstmt.executeUpdate();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(pstmt != null) {
                pstmt.close();
            }

            if(conn != null) {
                conn.close();
            }

        }
        return common.COMMON_Result;
    }

    //updated method to UPDATE DB
    public int dbUpdated(int no, String tittle, String content, String name) throws SQLException {
        common.COMMON_Result = 1;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            String sql = "UPDATE board SET ttitle = ?, content = ?, name = ?, updated_ts = CURRENT_TIMESTAMP() WHERE board_no = ?";
            pstmt = conn.prepareStatement(sql);

            StringReader srTittle = new StringReader(tittle);
            StringReader srContent = new StringReader(content);
            StringReader srName = new StringReader(name);

            pstmt.setCharacterStream(1, srTittle);
            pstmt.setCharacterStream(2, srContent);
            pstmt.setCharacterStream(3, srName);
            pstmt.setInt(4, no);

            common.COMMON_Result = pstmt.executeUpdate();


        } catch(Exception e) {
            e.printStackTrace();

        } finally {
            if(pstmt != null) {
                pstmt.close();
            }
            if(conn != null) {
                conn.close();
            }
        }
        return common.COMMON_Result;
    }

    //search method to SELECT DB
    public ResultSet dbSearched(String type, String searchedValue) throws SQLException {
        common.COMMON_Result = 1;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

            String sql = "SELECT * FROM board WHERE " + type + " like ? AND is_deleted IS FALSE ORDER BY created_ts DESC";
            pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            StringReader searchName = new StringReader(searchedValue);
            pstmt.setCharacterStream(1, searchName);

            resultSet = pstmt.executeQuery();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
