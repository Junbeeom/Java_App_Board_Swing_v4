package com.project.Swing;

import com.project.board.Board;
import com.project.board.BoardService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class ViewMain extends JFrame {
    Board board = new Board();
    BoardService boardService = new BoardService();

    JFrame jFrame = new JFrame();
    JPanel jPanel = new JPanel();
    JScrollPane jScrollPane = new JScrollPane();

    String header[] = {"no", "title", "content", "name", "created_ts", "updated_ts"};
    String contents[][] = {
            {"1", "중국집", "오늘은 짜장면을 먹었다.", "조준범", "19시30분", "없음"},
            {"2", "치킨", "허니콤보를 먹어 말아?", "민해주", "20시 30분", "없음"}
    };


    public ViewMain() {
        Gui_init();
    }

    public void Gui_init() {
        setTitle("게시판");
        setBounds(0,0,600,350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        ArrayList<Board> arrayList = null;
        try {
            arrayList = boardService.listed();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] colNames = {"고유번호","작성자","제목","내용","등록일시","수정일시"};
        Object[][] rowDatas;
        rowDatas = new Object[arrayList.size()][colNames.length];

        for(int i = 0; i < arrayList.size(); i++) {
            rowDatas[i] = new Object[] {
                    arrayList.get(i).getNo(),
                    arrayList.get(i).getName(),
                    arrayList.get(i).getTitle(),
                    arrayList.get(i).getContent(),
                    arrayList.get(i).getCreatedTs(),
                    arrayList.get(i).getUpdatedTs()
            };
        }


        JTable table = new JTable(rowDatas, colNames);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(12,49,560,189);
        getContentPane().add(scrollPane);

        JLabel jLabel = new JLabel("검색조건"   );
        jLabel.setBounds(186, 20, 56, 15);
        getContentPane().add(jLabel);

        JComboBox comboBox = new JComboBox();
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"title", "content", "name"}));
        comboBox.setBounds(244, 17, 100, 21);
        getContentPane().add(comboBox);

        JTextField jTextField = new JTextField();
        jTextField.setBounds(350, 17, 125, 21);
        getContentPane().add(jTextField);
        //추가해야함
        //jTextField.setColumns(10);

        JButton jButton = new JButton("search");
        jButton.setBounds(470, 16, 106, 23);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //로직 작성
            }
        });
        getContentPane().add(jButton);


        JButton jButton1 = new JButton("게시글 작성");
        jButton1.setBounds(475, 248, 97, 23);
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Created();
            }
        });
        getContentPane().add(jButton1);



        setVisible(true);





    }

    public static void main(String[] args) {
        new ViewMain();

    }

}
