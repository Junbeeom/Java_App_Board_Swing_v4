# Java_App_Board_Swing_v4

# 1.프로젝트 개요
### 1.1 프로젝트 목적
- JDK에서 제공하는 Swing을 이용한 Java 게시판 Application 개발

### 1.2 목표 및 의의
#### 1.2.1 Java_App_Board_Swing_v4
- Swing을 이용하여 GUI구현


# 2. 개발 환경
- IntelliJ IDEA(Ultimate Edition), gitHurb


# 3. 사용기술
- Java 11


# 4.프로젝트 설계

### 4.1 main View
<img width="626" alt="스크린샷 2022-09-28 오후 2 21 00" src="https://user-images.githubusercontent.com/103010985/192696703-94fd0401-3b6b-4493-b887-6d729bce5abc.png">

### 4.2 등록하기
<img width="479" alt="스크린샷 2022-09-28 오후 2 21 21" src="https://user-images.githubusercontent.com/103010985/192697541-f3fb415c-8e88-4a00-a669-579d51088c66.png">

### 4.3 수정 및 삭제하기
<img width="532" alt="스크린샷 2022-09-28 오후 2 46 44" src="https://user-images.githubusercontent.com/103010985/192697921-f9a2e0c7-7adb-4697-971b-06b7f0095e1d.png">

### 4.3 검색하기
<img width="306" alt="스크린샷 2022-09-28 오후 2 22 17" src="https://user-images.githubusercontent.com/103010985/192698047-8765609d-b566-48c5-a101-3924825ea620.png">



### 4.1 board Package

<img width="333" alt="스크린샷 2022-09-28 오후 2 50 47" src="https://user-images.githubusercontent.com/103010985/192698248-8e23b5f0-ee16-4bec-9aec-0894b71d5022.png">


### 4.2 database Package
<img width="331" alt="스크린샷 2022-09-28 오후 2 51 23" src="https://user-images.githubusercontent.com/103010985/192698376-dd0a25e2-af6d-41af-b8ca-474ae2372f42.png">

### 4.3 Swing view Package
<img width="934" alt="스크린샷 2022-09-28 오후 2 59 38" src="https://user-images.githubusercontent.com/103010985/192699678-a9633a72-cd2b-45aa-998d-453627fdcf7b.png">

# 5.기본 기능
- 등록 registered 
- 조회 listed
- 검색 searched
- 수정 modified
- 삭제 deleted



# 6.핵심 기능

### 6.1 게시글 등록, 수정 data 입력 받을 시 유효성 체크
<img width="456" alt="스크린샷 2022-09-28 오후 3 03 37" src="https://user-images.githubusercontent.com/103010985/192700162-accbc3ae-01e6-46b4-b306-fd52f68c60a6.png">


```java      
            //등록 actionPerformed
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
        
        //수정 actionPerformed
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
```


# 7.회고

### Java_App_Board_Swing_v4

1. 

2. Mysql connector Library를 추가하고 Connection하기까지 크게 어려움은 없었다. DB에서 Query가 실행되고 나서 사용자가 원하는 서비스가 정상적으로 실행 됐는지에 대한 Message를 던져주고 싶었다. 
DBmysql Class의 query 실행후 결과 값을 반환하는 executeUpdate(), executeQuery()을 활용하여 service단에서 값을 return 받아 if문을 활용하여 서비스 실행 후 결과를 알려주었다.

 




