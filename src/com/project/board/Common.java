package com.project.board;

import javax.swing.*;

public class Common extends JFrame {
    public static int COMMON_Result = 0;
    public static final String BOARD_NAME = "name";
    public static final String BOARD_TITLE = "title";
    public static final String BOARD_CONTENT = "content";

    public boolean validation(String type, String value) {
        switch (type) {
            case BOARD_NAME:
                //이름 유효성 체크
                String isKoreanCheck = "^[가-힣]*$";
                String isAlaphaCheck = "^[a-zA-Z]*$";

                if (value.matches(isKoreanCheck) || value.matches(isAlaphaCheck)) {
                    return true;
                } else {
                    return false;
                }

            case BOARD_TITLE:
                //제목 유효성 체크
                if (value.length() <= 12) {
                    return true;
                } else {
                    return false;
                }

            case BOARD_CONTENT:
                //내용 유효성 체크
                if (value.length() <= 200) {
                    return true;
                } else {
                    return false;
                }
            default:
                break;
        }
        return true;
    }
}
