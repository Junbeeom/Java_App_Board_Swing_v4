package com.project.database;

import com.project.Swing.Searched;
import com.project.board.Board;
import com.project.board.BoardService;
import com.project.board.Common;

import java.sql.*;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Scanner;

public class DBMysql {
    ArrayList<Board> swingList = new ArrayList<>();
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
    public int dbCreated(String title, String content, String name) throws SQLException {
        common.result = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

            String sql = "INSERT INTO boardtable(title, content, name) VALUES(?, ?, ?)";

            pstmt = conn.prepareStatement(sql);

            StringReader srTitle = new StringReader(title);
            StringReader srName = new StringReader(name);

            pstmt.setCharacterStream(1, srTitle, 20);
            pstmt.setString(2, content);
            pstmt.setCharacterStream(3, srName, 12);

            common.result = pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return common.result;
    }

    public ArrayList<Board> dbListed() throws SQLException {

        common.result = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

            String sql = "SELECT * FROM boardtable where is_deleted IS FALSE";

            pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            resultSet = pstmt.executeQuery();

            return swingList = listPrint(resultSet);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
       return swingList;
    }


    //delted method to DELETE DB
    public int dbDeleted(int no) throws SQLException {
        int result = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

            String sql = "UPDATE boardtable SET deleted_ts = CURRENT_TIMESTAMP(), is_deleted = TRUE WHERE board_no = ?";

            pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            pstmt.setInt(1, no);

            result = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }

            if (conn != null) {
                conn.close();
            }

        }
        return result;
    }

    //updated method to UPDATE DB
    public int dbUpdated(int no, String title, String content, String name) throws SQLException {
        Scanner sc = new Scanner(System.in);
        BoardService boardService = new BoardService();
        common.result = 1;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

            String sql = "SELECT * FROM boardtable WHERE board_no = ? AND is_deleted IS FALSE";
            pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            pstmt.setInt(1, no);
            resultSet = pstmt.executeQuery();

            if(!resultSet.next()) {
                common.result = 0;
                return  common.result;
            }


            sql = "UPDATE boardtable SET title = ?, content = ?, name = ?, updated_ts = CURRENT_TIMESTAMP() WHERE board_no = ?";
            pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            StringReader srTitle = new StringReader(title);
            StringReader srContent = new StringReader(content);
            StringReader srName = new StringReader(name);

            pstmt.setCharacterStream(1, srTitle);
            pstmt.setCharacterStream(2, srContent);
            pstmt.setCharacterStream(3, srName);
            pstmt.setInt(4, no);

            common.result = pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return common.result;
    }

    //search method to SELECT DB
    public int dbSearched(String type, String searchedValue) throws SQLException {
        ArrayList<Board> swingSearch = new ArrayList<Board>();

        common.result = 1;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);

            String sql = "SELECT * FROM boardtable WHERE " + type + " like ? AND is_deleted IS FALSE";
            pstmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            StringReader searchName = new StringReader(searchedValue);
            pstmt.setCharacterStream(1, searchName);

            resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                Board board = new Board();

                board.setNo(resultSet.getInt("board_no"));
                board.setName(resultSet.getString("name"));
                board.setTitle(resultSet.getString("title"));
                board.setContent(resultSet.getString("content"));
                board.setCreatedTs(resultSet.getString("created_ts"));
                board.setUpdatedTs(resultSet.getString("updated_ts"));

                swingSearch.add(board);
            }

            Searched searchedView = new Searched();
            searchedView.searched(swingSearch);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return common.result;
    }

    //print method to SELECT FROM BOARDTABLE
    public ArrayList<Board> listPrint(ResultSet resultSet) throws SQLException {
        common.result = 1;

        if(!resultSet.next()) {
            System.out.println("지금 여기서? 게시물이 없습니다.");
        } else {
            resultSet.beforeFirst();

            while (resultSet.next()) {
                System.out.println("고유번호 : " + resultSet.getInt("board_no"));
                System.out.println("작 성 자 : " + resultSet.getString("name"));
                System.out.println("제    목 : " + resultSet.getString("title"));
                System.out.println("내    용 : " + resultSet.getString("content"));
                System.out.println("등록일시 : " + resultSet.getString("created_ts"));
                System.out.println("수정일시 : " + resultSet.getString("updated_ts"));
                System.out.println("===============================================");

                Board board = new Board();
                board.setNo(resultSet.getInt("board_no"));
                board.setName(resultSet.getString("name"));
                board.setTitle(resultSet.getString("title"));
                board.setContent(resultSet.getString("content"));
                board.setCreatedTs(resultSet.getString("created_ts"));
                board.setUpdatedTs(resultSet.getString("updated_ts"));

                swingList.add(board);
            }
        }

           if(resultSet.getMetaData().getColumnCount() == 0) {
               common.result = 0;
           }

        return swingList;
    }
}
