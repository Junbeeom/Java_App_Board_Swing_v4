package com.project.Swing;

import com.project.board.BoardService;
import com.project.board.Common;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.ResultSet;
import java.sql.SQLException;

public class View extends JFrame {
    BoardService boardService = new BoardService();

    private JTextField tittleArea;
    private JTextArea contentArea;
    private JTextField nameArea;
    private String userTitle;
    private String userContent;
    private String userName;

    public static void main(String[] args) {
        new View();
    }

    public View() {
        setTitle("게시판");
        setBounds(0,0,600,350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        String[] colNames = {"고유번호","작성자","제목","내용","등록일시","수정일시"};
        Object[][] rowDatas = new Object[0][];

        ResultSet resultSet = null;
        try {
            int size = 0;
            int i = 0;

          resultSet = boardService.listed();

          if(resultSet != null) {
              resultSet.last();
              size = resultSet.getRow();
              resultSet.beforeFirst();
          } else {
              JOptionPane.showMessageDialog(null, "조회에 실패했습니다.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
          }
            rowDatas = new Object[size][colNames.length];

            while(resultSet.next()) {
                rowDatas[i] = new Object[]{
                        resultSet.getInt("board_no"),
                        resultSet.getString("name"),
                        resultSet.getString("tittle"),
                        resultSet.getString("content"),
                        resultSet.getString("created_ts"),
                        resultSet.getString("updated_ts"),
                };
                i++;
            }
        } catch(SQLException e) {
            e.printStackTrace();
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

        ResultSet resultSet1 = resultSet;
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowNum = table.getSelectedRow();

                try {
                    while(resultSet1.absolute(rowNum + 1)) {
                        updatedView(resultSet1);
                        break;
                    }
                } catch(SQLException ex) {
                    ex.printStackTrace();
                }
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
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"tittle", "content", "name"}));
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
                dispose();

                try {
                    boardService.searched(String.valueOf(comboBox.getSelectedItem()), searchValue.getText());
                } catch(SQLException ex) {
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
                createdView();
            }
        });
        getContentPane().add(jButton1);
        setVisible(true);
    }

    public void createdView() {
        JFrame createdFrame = new JFrame();
        Common common = new Common();
        createdFrame.setBounds(new Rectangle(600, 0, 450, 280));
        createdFrame.setTitle("게시글 등록하기");
        createdFrame.setLayout(null);

        JLabel jLabelTitle = new JLabel("제목");
        jLabelTitle.setBounds(12,25,57,15);
        createdFrame.add(jLabelTitle);

        tittleArea = new JTextField("");
        tittleArea.setBounds(81, 22, 340, 21);
        createdFrame.add(tittleArea);
        tittleArea.setColumns(20);

        JLabel jLabelContent = new JLabel("내용");
        jLabelContent.setBounds(12, 59, 57, 15);
        createdFrame.add(jLabelContent);

        contentArea = new JTextArea("");
        contentArea.setLineWrap(true);
        contentArea.setRows(5);
        contentArea.setBounds(81, 53, 340, 69);
        createdFrame.add(contentArea);

        JLabel jLabelUser = new JLabel("작성자");
        jLabelUser.setBounds(12, 140, 57, 15);
        createdFrame.add(jLabelUser);
        tittleArea.setColumns(12);

        nameArea = new JTextField("");
        nameArea.setBounds(81, 137, 116, 21);
        createdFrame.add(nameArea);
        nameArea.setColumns(10);

        JButton jButtonCreated = new JButton("작성완료");
        jButtonCreated.setBounds(81, 180, 116, 23);

        jButtonCreated.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                //제목 유효성 검사
                userTitle = tittleArea.getText();
                if(!common.validation(Common.BOARD_TITLE, userTitle)) {
                    while(true) {
                        userTitle = JOptionPane.showInputDialog(null, "제목은 12글자 이하로 입력해야 합니다.\n다시 입력하세요.", "");

                        if(common.validation(Common.BOARD_TITLE, userTitle)) {
                            break;
                        }
                    }
                }
                userContent = contentArea.getText();

                //내용 유효성 검사
                if(!common.validation(Common.BOARD_CONTENT, userContent)) {
                    while(true) {
                        userContent = JOptionPane.showInputDialog(null, "내용은 200자 이하로 작성할 수 있습니다.\n글자수에 맞게 다시 작성하세요", "");

                        if(common.validation(Common.BOARD_CONTENT, userContent)) {
                            break;
                        }
                    }
                }
                userName = nameArea.getText();

                //이름 유효성 검사
                if(!common.validation(Common.BOARD_NAME, userName)) {
                    while (true) {
                        userName = JOptionPane.showInputDialog(null, "이름을 올바른 형식으로 입력하세요\n한글 및 영어만 가능합니다.", "");

                        if(common.validation(Common.BOARD_NAME, userName)) {
                            break;
                        }
                    }
                }

                try {
                    int result = boardService.registered(userTitle, userContent, userName);

                    if(result == 1) {
                        JOptionPane.showMessageDialog(null, "등록이 완료되었습니다", "INFORMATION_MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, "등록 실패하였습니다.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
                    }
                } catch(SQLException ex) {
                    ex.printStackTrace();
                }
                createdFrame.dispose();
                new View();
            }
        });
        createdFrame.add(jButtonCreated);

        JButton jButtonClose = new JButton("닫기");
        jButtonClose.setBounds(209, 180, 97, 23);

        jButtonClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createdFrame.dispose();
                new View();
            }
        });
        createdFrame.add(jButtonClose);
        createdFrame.setVisible(true);
    }

    public void updatedView(ResultSet resultSet) throws SQLException {
        JFrame updatedFrame = new JFrame();

        int num = resultSet.getInt("board_no");

        updatedFrame.setBounds(new Rectangle(600, 0, 450, 280));
        updatedFrame.setTitle("게시글 수정");
        updatedFrame.setLayout(null);

        JLabel jLabelTitle = new JLabel("글제목");
        jLabelTitle.setBounds(12, 25, 57, 15);
        updatedFrame.add(jLabelTitle);

        tittleArea = new JTextField(resultSet.getString("tittle"));
        tittleArea.setBounds(81, 22, 340, 21);
        updatedFrame.add(tittleArea);
        tittleArea.setColumns(20);

        JLabel jLabelContent = new JLabel("글내용");
        jLabelContent.setBounds(12, 59, 57, 15);
        updatedFrame.add(jLabelContent);

        contentArea = new JTextArea(resultSet.getString("content"));
        contentArea.setLineWrap(true);
        contentArea.setRows(5);
        contentArea.setBounds(81, 53, 340, 69);
        updatedFrame.add(contentArea);

        JLabel jLabelName = new JLabel("작성자");
        jLabelName.setBounds(12, 140, 57, 15);
        updatedFrame.add(jLabelName);

        nameArea = new JTextField(resultSet.getString("name"));
        nameArea.setBounds(81, 137, 116, 21);
        updatedFrame.add(nameArea);
        nameArea.setColumns(12);

        JButton buttonUpdated = new JButton("글수정");
        buttonUpdated.setBounds(81, 180, 97, 23);

        //액션 리스너
        buttonUpdated.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Common common = new Common();

                //제목 유효성 검사
                userTitle = tittleArea.getText();
                if(!common.validation(Common.BOARD_TITLE, userTitle)) {
                    while(true) {
                        userTitle = JOptionPane.showInputDialog(null, "제목은 12글자 이하로 입력해야 합니다.\n다시 입력하세요.", "");

                        if(common.validation(Common.BOARD_TITLE, userTitle)) {
                            break;
                        }
                    }
                }
                userContent = contentArea.getText();

                //내용 유효성 검사
                if(!common.validation(Common.BOARD_CONTENT, userContent)) {
                    while(true) {
                        userContent = JOptionPane.showInputDialog(null, "내용은 200자 이하로 작성할 수 있습니다.\n글자수에 맞게 다시 작성하세요", "");

                        if(common.validation(Common.BOARD_CONTENT, userContent)) {
                            break;
                        }
                    }
                }
                userName = nameArea.getText();

                //이름 유효성 검사
                if(!common.validation(Common.BOARD_NAME, userName)) {
                    while(true) {
                        userName = JOptionPane.showInputDialog(null, "이름을 올바른 형식으로 입력하세요\n한글 및 영어만 가능합니다.", "");

                        if(common.validation(Common.BOARD_NAME, userName)) {
                            break;
                        }
                    }
                }
                try {
                    int result = boardService.modified(num, userTitle, userContent, userName);

                    if(result == 1) {
                        JOptionPane.showMessageDialog(null, "수정이 완료되었습니다", "INFORMATION_MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        new View().revalidate();
                        new View().repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, "수정에 실패하였습니다.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
                        updatedFrame.dispose();
                        new View().revalidate();
                        new View().repaint();
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                setVisible(false);
            }
        });
        updatedFrame.add(buttonUpdated);

        JButton jButtonDeletd = new JButton("글삭제");
        jButtonDeletd.setBounds(190, 180, 97, 23);
        jButtonDeletd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int result = boardService.deleted(num);
                    if(result == 1) {
                        JOptionPane.showMessageDialog(null, "삭제 되었습니다", "INFORMATION_MESSAGE", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        new View().revalidate();
                        new View().repaint();
                    } else {
                        JOptionPane.showMessageDialog(null, "삭제를 실패하였습니다.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
                        updatedFrame.dispose();
                        new View().revalidate();
                        new View().repaint();
                    }
                } catch(SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        updatedFrame.add(jButtonDeletd);

        JButton jButtonClose = new JButton("닫기");
        jButtonClose.setBounds(299, 180, 97, 23);

        //액션리스너
        jButtonClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updatedFrame.dispose();
                View view = new View();
                view.revalidate();

                setVisible(false);
            }
        });
        updatedFrame.add(jButtonClose);
        updatedFrame.setVisible(true);
    }

    public void searchedView(ResultSet resultSet) throws SQLException {
        JFrame searchedFrame = new JFrame();

        searchedFrame.setTitle("검색 결과");
        searchedFrame.setBounds(0,0,600,350);
        searchedFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        searchedFrame.getContentPane().setLayout(null);

        String[] colNames = {"고유번호","작성자","제목","내용","등록일시","수정일시"};
        Object[][] rowDatas = new Object[0][];
        try {
            int size = 0;
            int i = 0;

            if(resultSet != null) {
                resultSet.last();
                size = resultSet.getRow();
                resultSet.beforeFirst();
            } else {
                JOptionPane.showMessageDialog(null, "조회에 실패했습니다.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
            }

            rowDatas = new Object[size][colNames.length];

            while(resultSet.next()) {
                rowDatas[i] = new Object[]{
                        resultSet.getInt("board_no"),
                        resultSet.getString("name"),
                        resultSet.getString("tittle"),
                        resultSet.getString("content"),
                        resultSet.getString("created_ts"),
                        resultSet.getString("updated_ts"),
                };
                i++;
            }
        } catch(SQLException e) {
            e.printStackTrace();
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

        ResultSet resultSet1 = resultSet;
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int rowNum = table.getSelectedRow();

                try {
                    while (resultSet1.absolute(rowNum + 1)) {
                        updatedView(resultSet1);
                        break;
                    }

                } catch(SQLException ex) {
                    ex.printStackTrace();
                }
                super.mouseClicked(e);
            }
        });
        JScrollPane jScrollPane = new JScrollPane();
        jScrollPane.setViewportView(table);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(12,49,560,189);
        searchedFrame.add(scrollPane);

        JLabel jLabel = new JLabel("검색조건"   );
        jLabel.setBounds(186, 20, 56, 15);
        searchedFrame.add(jLabel);

        JComboBox comboBox = new JComboBox();
        comboBox.setModel(new DefaultComboBoxModel(new String[]{"tittle", "content", "name"}));
        comboBox.setBounds(244, 17, 100, 21);
        searchedFrame.add(comboBox);

        JTextField searchValue = new JTextField();
        searchValue.setBounds(350, 17, 125, 21);
        searchedFrame.add(searchValue);

        JButton jButton = new JButton("search");
        jButton.setBounds(470, 16, 106, 23);

        //액션리스너
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    boardService.searched(String.valueOf(comboBox.getSelectedItem()), searchValue.getText());
                } catch(SQLException ex) {
                    ex.printStackTrace();
                }
                setVisible(false);
            }
        });
        searchedFrame.add(jButton);

        JButton jButton1 = new JButton("게시글 작성");
        jButton1.setBounds(475, 248, 97, 23);

        //액션리스너
        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchedFrame.dispose();
                createdView();
            }
        });
        searchedFrame.add(jButton1);

        JButton jButton2 = new JButton("뒤로 가기");
        jButton2.setBounds(12, 248, 97, 23);

        //액션리스너
        jButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchedFrame.dispose();
                new View();
            }
        });
        searchedFrame.add(jButton2);
        searchedFrame.setVisible(true);
    }
}
