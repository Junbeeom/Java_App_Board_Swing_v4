package com.project.Swing;

import com.project.board.Board;
import com.project.board.BoardService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Updated extends JFrame {
    private JTextField titleArea;
    private JTextArea contentArea;
    private JTextField nameArea;

    private String userTitle;
    private String userContent;
    private String userName;

    public Updated(Board board) {
        BoardService boardService = new BoardService();

        int num = board.getNo();

        setBounds(new Rectangle(600, 0, 450, 280));
        setTitle("게시글 수정");
        getContentPane().setLayout(null);

        JLabel jLabelTitle = new JLabel("글제목");
        jLabelTitle.setBounds(12, 25, 57, 15);
        getContentPane().add(jLabelTitle);

        titleArea = new JTextField(board.getTitle());
        titleArea.setBounds(81, 22, 340, 21);
        getContentPane().add(titleArea);
        titleArea.setColumns(20);

        JLabel jLabelContent = new JLabel("글내용");
        jLabelContent.setBounds(12, 59, 57, 15);
        getContentPane().add(jLabelContent);

        contentArea = new JTextArea(board.getContent());
        contentArea.setLineWrap(true);
        contentArea.setRows(5);
        contentArea.setBounds(81, 53, 340, 69);
        getContentPane().add(contentArea);

        JLabel jLabelName = new JLabel("작성자");
        jLabelName.setBounds(12, 140, 57, 15);
        getContentPane().add(jLabelName);

        nameArea = new JTextField(board.getName());
        nameArea.setBounds(81, 137, 116, 21);
        getContentPane().add(nameArea);
        nameArea.setColumns(12);

        JButton buttonUpdated = new JButton("글수정");
        buttonUpdated.setBounds(81, 180, 97, 23);

        //액션 리스너
        buttonUpdated.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userTitle = titleArea.getText();
                userContent = contentArea.getText();
                userName = jLabelName.getText();

                try {
                    boardService.modified(num, userTitle, userContent, userName);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }


                setVisible(false);
            }
        });
        getContentPane().add(buttonUpdated);

        JButton jButtonDeletd = new JButton("글삭제");
        jButtonDeletd.setBounds(190, 180, 97, 23);
        jButtonDeletd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    boardService.deleted(num);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });
        getContentPane().add(jButtonDeletd);

        JButton jButtonClose = new JButton("닫기");
        jButtonClose.setBounds(299, 180, 97, 23);

        //액션리스너
        jButtonClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });
        getContentPane().add(jButtonClose);

        setVisible(true);
    }

}
