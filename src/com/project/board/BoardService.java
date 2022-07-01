package com.project.board;

import com.project.database.DBMysql;
import static com.project.board.Common.*;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

public class BoardService {
    public BoardService() {}

    DBMysql dbMysql = new DBMysql();
    Common common = new Common();

    //등록
    public void registered(String userTitle, String userContent, String userName) throws SQLException {
        common.result = dbMysql.dbCreated(userTitle, userContent, userName);

        if(common.result == 1) {
            System.out.println("\n" + userName + "님의 게시글 등록이 완료 되었습니다.");
        } else {
            System.out.println("등록 실패 했습니다.");
        }
    }

    //조회
    public ArrayList<Board> listed() throws SQLException {

        ArrayList<Board> arrayList = dbMysql.dbListed();

        if(common.result == 0) {
            System.out.println("조회를 실패하였습니다.");
        }
        return arrayList;
    }

    //검색
    public void searched(String type, String searchValue) throws SQLException {
    searchValue = common.validation(type, searchValue);
        searchValue = "%" + searchValue + "%";
        common.result = dbMysql.dbSearched(type, searchValue);

        if(common.result == 0) {
            System.out.println("조회를 실패하였습니다.");
        }
    }

    //수정
    public void modified(int number, String title, String content, String name) throws SQLException {

        common.result = dbMysql.dbUpdated(number, title, content, name);

        if(common.result == 0) {
            System.out.println("게시글이 없습니다.");
        } else {
            System.out.println("게시글 수정이 완료되었습니다.");
        }
    }


    //삭제
    public void deleted(int number) throws SQLException {

        common.result = dbMysql.dbDeleted(number);

        if(common.result == 1) {
            System.out.println("게시글이 삭제되었습니다.");
        } else {
            System.out.println("존재하지 않는 게시글입니다.");
        }
    }
}