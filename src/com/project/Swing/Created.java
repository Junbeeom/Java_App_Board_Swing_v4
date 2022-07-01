package com.project.Swing;

import com.project.board.BoardService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class Created extends JFrame {
    BoardService boardService = new BoardService();

    //공통상수로 뺀다.
    private JTextField titleArea;
    private JTextArea contentArea;
    private JTextField nameArea;

    private String userTitle;
    private String userContent;
    private String userName;

    public Created () {
        setBounds(new Rectangle(600, 0, 450, 280));
        setTitle("게시글 등록하기");
        getContentPane().setLayout(null);

        JLabel jLabelTitle = new JLabel("제목");
        jLabelTitle.setBounds(12,25,57,15);
        getContentPane().add(jLabelTitle);

        titleArea = new JTextField("제목을 작성하세요");
        titleArea.setBounds(81, 22, 340, 21);
        getContentPane().add(titleArea);
        titleArea.setColumns(20);

        JLabel jLabelContent = new JLabel("내용");
        jLabelContent.setBounds(12, 59, 57, 15);
        getContentPane().add(jLabelContent);

        contentArea = new JTextArea("내용을 작성하세요");
        contentArea.setLineWrap(true);
        contentArea.setRows(5);
        contentArea.setBounds(81, 53, 340, 69);
        getContentPane().add(contentArea);

        JLabel jLabelUser = new JLabel("작성자");
        jLabelUser.setBounds(12, 140, 57, 15);
        getContentPane().add(jLabelUser);
        titleArea.setColumns(12);

        nameArea = new JTextField("작성자의 이름을 입력하세요");
        nameArea.setBounds(81, 137, 116, 21);
        getContentPane().add(nameArea);
        nameArea.setColumns(10);

        JButton jButtonCreated = new JButton("작성완료");
        jButtonCreated.setBounds(81, 180, 116, 23);

        jButtonCreated.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                userTitle = titleArea.getText();
                userContent = contentArea.getText();
                userName = nameArea.getText();

                try {
                    boardService.registered(userTitle, userContent, userName);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    System.out.println("swing에서 걸림.");
                }
            }
        });
        getContentPane().add(jButtonCreated);

        JButton jButtonClose = new JButton("닫기");
        jButtonClose.setBounds(209, 180, 97, 23);

        jButtonClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        getContentPane().add(jButtonClose);

        setVisible(true);
    }
}
