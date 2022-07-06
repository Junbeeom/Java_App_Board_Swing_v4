package com.project.board;

import com.project.Swing.View;
import com.project.database.DBMysql;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BoardService {
    DBMysql dbMysql = new DBMysql();
    Common common = new Common();

    public BoardService() {}

    //등록
    public int registered(String userTitle, String userContent, String userName) throws SQLException {
        common.COMMON_Result = dbMysql.dbCreated(userTitle, userContent, userName);

        if(common.COMMON_Result == 1) {
            // 정상등록
            return common.COMMON_Result;
        }
        return common.COMMON_Result;
    }

    //조회
    public ResultSet listed() throws SQLException {
        ResultSet resultSet = null;
         resultSet = dbMysql.dbListed();

        if(resultSet.next() == false) {
            System.out.println("조회를 실패하였습니다.");
            resultSet.beforeFirst();
        }
        return resultSet;
    }

    //검색
    public void searched(String type, String searchValue) throws SQLException {
        StringBuilder sb = new StringBuilder("%%");
        sb.insert(1, searchValue);

        ResultSet resultSet = dbMysql.dbSearched(type, sb.toString());

        if(resultSet.next() == false) {
            System.out.println("조회를 실패하였습니다.");
            resultSet.beforeFirst();
        }

        new View().searchedView(resultSet);
    }

    //수정
    public int modified(int number, String title, String content, String name) throws SQLException {
        common.COMMON_Result = dbMysql.dbUpdated(number, title, content, name);

        if(common.COMMON_Result == 1) {
            //정상 수정
            return common.COMMON_Result;
        }
        return common.COMMON_Result;
    }

    //삭제
    public int deleted(int number) throws SQLException {
        common.COMMON_Result = dbMysql.dbDeleted(number);
        if(common.COMMON_Result == 1) {
            //정상 삭제
            return common.COMMON_Result;
        }
        return common.COMMON_Result;
    }
}