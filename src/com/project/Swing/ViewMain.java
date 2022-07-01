package com.project.Swing;

import com.project.board.Board;
import com.project.board.BoardService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;

public class ViewMain extends JFrame {
    public static void main(String[] args) {
        new ViewMain();
    }

    //ViewMain 안으로 넣으면 오류가 나는데 왜그럴까?
    Board board = new Board();
    BoardService boardService = new BoardService();

    public ViewMain() {
        setTitle("게시판");
        setBounds(0,0,600,350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        ArrayList<Board> arrayList = new ArrayList<>();
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

        JTable table = new JTable();
        table.setModel(new DefaultTableModel(rowDatas, colNames) {
            boolean[] columnEditables = new boolean[] {
                    false, true, false, false , false, false
            };

            public boolean isCellEditable(int row, int column) {
                return columnEditables[column];
            }
        });
        table.getColumnModel().getColumn(0).setResizable(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(45);
        table.getColumnModel().getColumn(2).setResizable(false);
        table.getColumnModel().getColumn(2).setPreferredWidth(200);
        table.getColumnModel().getColumn(3).setResizable(false);
        table.getColumnModel().getColumn(3).setPreferredWidth(164);
        table.getColumnModel().getColumn(4).setResizable(false);
        table.getColumnModel().getColumn(4).setPreferredWidth(140);
        table.getColumnModel().getColumn(5).setResizable(false);
        table.getColumnModel().getColumn(5).setPreferredWidth(100);

        //why? final을 붙여서 만들어야 할까, 그냥 arraylist.get(rowNum)이라고하면 에러가 뜬다.
        ArrayList<Board> finalArrayList = arrayList;
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int rowNum = table.getSelectedRow();

                board = finalArrayList.get(rowNum);

                new Updated(board);

                super.mouseClicked(e);
            }
        });

        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(12,49,560,189);
        getContentPane().add(scrollPane);

        //콤보박스 조건
        JLabel labelSearch = new JLabel("검색조건"   );
        labelSearch.setBounds(186, 20, 56, 15);
        getContentPane().add(labelSearch);

        JComboBox comboBox = new JComboBox();
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"title", "content", "name"}));
        comboBox.setBounds(244, 17, 100, 21);
        getContentPane().add(comboBox);

        JTextField searchValue = new JTextField();
        searchValue.setBounds(350, 17, 125, 21);
        getContentPane().add(searchValue);

        JButton jButton = new JButton("search");
        jButton.setBounds(470, 16, 106, 23);

        //액션리스너
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boardService.searched(String.valueOf(comboBox.getSelectedItem()), searchValue.getText());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

                setVisible(false);
            }
        });
        getContentPane().add(jButton);

        //작성 버튼
        JButton jButton1 = new JButton("게시글 작성");
        jButton1.setBounds(475, 248, 97, 23);

        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Created();
            }
        });
        getContentPane().add(jButton1);

        //갱신 버튼
        JButton jButton2 = new JButton("갱신");
        jButton2.setBounds(200, 248, 97, 23);

        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new ViewMain();
            }
        });
        getContentPane().add(jButton2);

        setVisible(true);
    }

}