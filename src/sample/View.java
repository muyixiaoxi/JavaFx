package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;


public class View extends Application {
    //    登录学生学号
    private static int id;
    private static Stage stage;
    //    提示框数目
    private static int number = 0;
    //    增删改查的页面数
    private static int anum = 0;
    private static Student student0;

    //file:/D:/JAVA/id/workspace/JavaFx/image/bg1.jpg

    private static Image img=new Image("file:/D:/JAVA/id/workspace/JavaFx/image/bg1.jpg");
    private static BackgroundImage bImg = new BackgroundImage(img,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            BackgroundSize.DEFAULT);
    private static Background bGround = new Background(bImg);

    @Override
    public void start(Stage stage) throws Exception {
//        将舞台复制给静态属性
        View.stage = stage;
        stage.setTitle("学生信息管理系统");

//        设置窗口大小不可调节
        stage.setResizable(false);
//        登录
        login();
        stage.show();
    }

    //     登录界面 
    static void login() {
        //        新建布局
        GridPane gp = new GridPane();
//        设置居中方式
        gp.setAlignment(Pos.CENTER);
//        调整空隙
        gp.setHgap(10);
        gp.setVgap(20);
//        新建文本标签
        Label l1 = new Label("学号");
        TextField idt = new TextField();

//        用户密码
        Label l2 = new Label("密码");

//        字体
        Font fon1 = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20);

        l1.setFont(fon1);
        l2.setFont(fon1);
//        新建密码框
        PasswordField pwd = new PasswordField();


//        按钮的创建

        Button b1 = new Button("登录");
        Button b2 = new Button("注册");

        //        单选框：学生，老师
        ToggleGroup group = new ToggleGroup();
        RadioButton student = new RadioButton("学生");
        RadioButton manager = new RadioButton("管理员");
        HBox h1 = new HBox();
        student.setToggleGroup(group);
        manager.setToggleGroup(group);

//        添加单选按钮监听
        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals(student)) {   //单选按钮为学生
                l1.setText("学号");
                b1.setOnAction(actionEvent -> {
                    try {
                        if (Connect.login(Integer.valueOf(idt.getText()), pwd.getText()) > 0) {
                            id=Integer.valueOf(idt.getText());
                            homepage();
                        } else {
                            if (number == 0) {
                                tips("登陆失败");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                //        注册按钮事件
                b2.setOnAction(actionEvent -> {
                    BorderPane bp = new BorderPane();
                    add(bp);
                });
//                设置”注册“按钮可见
                b2.setVisible(true);
            } else if (newValue.equals(manager)) {
//                设置”注册“按钮不可见
                b2.setVisible(false);
                l1.setText("姓名");
                b1.setOnAction(actionEvent -> {
                    try {
                        if (Connect.maglogin(idt.getText(), pwd.getText())) {
                            maghomepage();
                        } else {
                            if (number == 0) {
                                tips("登录失败");
                            }
                        }
                        ;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

            }
        });
        h1.getChildren().addAll(student, manager);

//        背景图片

        gp.setBackground(bGround);

        //        添加画板
        gp.add(l1, 0, 0);
        gp.add(idt, 1, 0);
        gp.add(l2, 0, 1);
        gp.add(pwd, 1, 1);
        gp.add(b1, 0, 2);
        gp.add(b2, 1, 2);
        gp.add(h1, 1, 3);
        Scene sc = new Scene(gp, 400, 300);
        stage.setScene(sc);
    }



    //    学生操作界面
    static void homepage() {
//        创建文本
        Label l5 = new Label("学生查询系统");
        Button l1 = new Button("个人信息");
        Button l3 = new Button("成绩查询");
        Button l4 = new Button("返回登录");
//        设置字体颜色
        l1.setTextFill(Color.BLUE);
        l3.setTextFill(Color.BLUE);
        l4.setTextFill(Color.BLUE);
//        设置字体大小
        Font fon1 = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 30);
        Font fon2 = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20);
        l5.setFont(fon1);
        l1.setFont(fon2);
        l3.setFont(fon2);
        l4.setFont(fon2);

//        学生信息查询
        l1.setOnAction(actionEvent -> {
            query();
        });
//        成绩查询
        l3.setOnAction(actionEvent -> {
            try {
                score(id);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
//        返回登陆
        l4.setOnAction(actionEvent -> {
            login();
        });

//        添加面板
        VBox bv = new VBox(50, l5, l1, l3, l4);
        bv.setAlignment(Pos.CENTER);
        bv.setBackground(bGround);
        Scene sc = new Scene(bv, 500, 600);
        stage.setScene(sc);
    }

    //    管理员操作界面
    static void maghomepage() {
//        创建文本
        Label l5 = new Label("学生查询系统");
        Button l1 = new Button("学生信息管理");
        Button l2 = new Button("学生成绩管理");
        Button l3 = new Button("返回登陆");
//        设置字体颜色
        l1.setTextFill(Color.BLUE);
        l2.setTextFill(Color.BLUE);
        l3.setTextFill(Color.BLUE);

//        设置字体大小
        Font fon1 = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 30);
        Font fon2 = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20);
        l5.setFont(fon1);
        l1.setFont(fon2);
        l2.setFont(fon2);
        l3.setFont(fon2);

//        学生信息管理
        l1.setOnAction(actionEvent -> {
            magquery();
        });

//        学生成绩管理
        l2.setOnAction(actionEvent -> {
            magscore();
        });
//        返回登陆
        l3.setOnAction(actionEvent -> {
            login();
        });

//        添加面板
        VBox bv = new VBox(50, l5, l1, l2, l3);
        bv.setAlignment(Pos.CENTER);
        bv.setBackground(bGround);
        Scene sc = new Scene(bv, 500, 600);
        stage.setScene(sc);
    }


    //    学生信息查询界面
    static void query() {
        BorderPane bp = new BorderPane();
//        顶部
        FlowPane fp1 = new FlowPane();

        Button b3 = new Button("首页");
        b3.setOnAction(actionEvent -> {
            homepage();
        });
        fp1.getChildren().addAll(b3);
        fp1.setHgap(10);
//        设置上下之间的间隙内间距
        fp1.setPadding(new Insets(10));
        bp.setTop(fp1);


//        中部————表格控件
        ArrayList<Student> students = new ArrayList<>();
        init(bp, students, id);
//        添加场景
        Scene sc = new Scene(bp);
        stage.setScene(sc);
    }

    //    学生信息管理
    static void magquery() {
        BorderPane bp = new BorderPane();
//        顶部
        FlowPane fp1 = new FlowPane();
        Label l1 = new Label("姓名");
//        Label l2 =new Label("班级");
        TextField f1 = new TextField();
//        TextField f2 = new TextField();
        Button b1 = new Button("查询");
//        模糊查询
        b1.setOnAction(actionEvent -> {
            ArrayList<Student> students = new ArrayList<>();
            init(bp, students, f1.getText());
        });
        Button b2 = new Button("刷新");
        b2.setOnAction(actionEvent -> {
            ArrayList<Student> students = new ArrayList<>();
            init(bp, students);
        });
        fp1.getChildren().addAll(l1, f1, b1, b2);
        fp1.setHgap(10);
//        设置上下之间的间隙内间距
        fp1.setPadding(new Insets(10));
        bp.setTop(fp1);

//        底部
        FlowPane fp2 = new FlowPane();
        Button b3 = new Button("增加");
        Button b4 = new Button("删除");
        Button b5 = new Button("修改");
        Button b6 = new Button("退出");
//        增加
        b3.setOnAction(actionEvent -> {
            if (anum == 0) {
                add(bp);

            }
        });
//        删除
        b4.setOnAction(actionEvent -> {
            if (anum == 0) {
                delete(bp);
            }
        });
//        修改
        b5.setOnAction(actionEvent -> {
            if (anum == 0) {
                alter(bp);
            }
        });
        b6.setOnAction((actionEvent -> {
            maghomepage();
        }));
        fp2.getChildren().addAll(b3, b4, b5, b6);
        fp2.setHgap(10);
        fp2.setPadding(new Insets(10));
        bp.setBottom(fp2);

//        中部————表格控件
        ArrayList<Student> students = new ArrayList<>();
        init(bp, students);
//        添加场景
        Scene sc = new Scene(bp);
        stage.setScene(sc);
    }

    //    学生查看成绩界面
    static void score(int id) throws Exception {

        Student student = new Student();
        Connect.score(student, id);
        Label t1 = new Label("  语文:  " + student.getLanguage());
        Label t2 = new Label("  数学:  " + student.getMath());
        Label t3 = new Label("  英语:  " + student.getEnglish());
        Label t4 = new Label("  物理:  " + student.getPhysics());
        Label t5 = new Label("  化学:  " + student.getChemistry());
        Label t6 = new Label("  生物:  " + student.getBiology());
        Label t7 = new Label("  总分:  " + student.getScore());

//        字体
        Font fon2 = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20);
//        设置字体
        t1.setFont(fon2);
        t2.setFont(fon2);
        t3.setFont(fon2);
        t4.setFont(fon2);
        t5.setFont(fon2);
        t6.setFont(fon2);
        t7.setFont(fon2);


        Button b1 = new Button("返回");
        b1.setOnAction(actionEvent -> {
            homepage();
        });

        VBox vb = new VBox(t1, t2, t3, t4, t5, t6, t7, b1);
        vb.setAlignment(Pos.CENTER);
        vb.setSpacing(10);
        vb.setBackground(bGround);
        Scene sc = new Scene(vb, 500, 600);
        stage.setScene(sc);
    }

    //    增加界面
    static void add(BorderPane bp) {
        anum++;
        GridPane gp = new GridPane();
//        设置间距
        gp.setVgap(10);
        gp.setHgap(10);
        gp.setAlignment(Pos.CENTER);
//        文本
        Label l1 = new Label("姓名");
        Label l2 = new Label("性别");
        Label l3 = new Label("班级");
        Label l4 = new Label("地址");
        Label l5 = new Label("年龄");
        Label l6 = new Label("电话");
        Label l7 = new Label("密码");
        Label l8=new Label("确认密码");
        Button b1 = new Button("提交");

//        文本框
        TextField t1 = new TextField();
        TextField t2 = new TextField();
        TextField t3 = new TextField();
        TextField t4 = new TextField();
        TextField t5 = new TextField();
        TextField t6 = new TextField();
        PasswordField t7 = new PasswordField();
        PasswordField t8=new PasswordField();

        //        提示文字
        t6.setPromptText("请输入11位号码");

        //监听文本输入的内容
        t2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length()<2&&(newValue.equals("男")||newValue.equals("女"))){
                    t2.setText(newValue);
                }else{
                    t2.setText("");
                }
            }
        });


        b1.setOnAction(actionEvent -> {
            if (number == 0) {   //如果不存在提示框
                try {
                    if (t1.getText().trim().equals("") || t2.getText().trim().equals("") || t3.getText().trim().equals("") || t4.getText().trim().equals("") || t5.getText().trim().equals("") || t6.getText().trim().equals("") || t7.getText().trim().equals("")||!t7.getText().equals(t8.getText())||t6.getText().length()!=11) {
                        tips("添加失败");
                    } else {
                        if (Connect.add(t1.getText(), t2.getText(), t3.getText(), t4.getText(), t5.getText(), t6.getText(), t7.getText())) {
                            tips("添加成功");
                            ArrayList<Student> students = new ArrayList<>();
                            init(bp, students);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//        添加到面板中
        gp.add(l1, 0, 0);
        gp.add(t1, 1, 0);
        gp.add(l2, 0, 1);
        gp.add(t2, 1, 1);
        gp.add(l3, 0, 2);
        gp.add(t3, 1, 2);
        gp.add(l4, 0, 3);
        gp.add(t4, 1, 3);
        gp.add(l5, 0, 4);
        gp.add(t5, 1, 4);
        gp.add(l6, 0, 5);
        gp.add(t6, 1, 5);
        gp.add(l7, 0, 6);
        gp.add(t7, 1, 6);
        gp.add(l8,0,7);
        gp.add(t8,1,7);
        gp.add(b1, 1, 8);

//        添加场景舞台
        Scene sc = new Scene(gp, 300, 300);
        Stage stage1 = new Stage();
        stage1.setResizable(false);
//        窗口关闭时减一
        stage1.setOnCloseRequest(windowEvent -> {
            anum--;
        });
        stage1.setScene(sc);
        stage1.setTitle("添加");
        stage1.show();
    }

    //    修改--管理员成绩修改
    public static void magalter(BorderPane bp) {
        anum++;
        GridPane gp = new GridPane();
//        设置间距
        gp.setVgap(10);
        gp.setHgap(10);
        gp.setAlignment(Pos.CENTER);
//        文本
        Label l0 = new Label("学号");
        Label l1 = new Label("语文");
        Label l2 = new Label("数学");
        Label l3 = new Label("英语");
        Label l4 = new Label("物理");
        Label l5 = new Label("化学");
        Label l6 = new Label("生物");
        Button b1 = new Button("提交");
//        文本框
        TextField t0 = new TextField();
        TextField t1 = new TextField();
        TextField t2 = new TextField();
        TextField t3 = new TextField();
        TextField t4 = new TextField();
        TextField t5 = new TextField();
        TextField t6 = new TextField();

        if(!student0.equals("")){
            t0.setText(Integer.toString(student0.getId()));
            t1.setText(Integer.toString(student0.getLanguage()));
            t2.setText(Integer.toString(student0.getMath()));
            t3.setText(Integer.toString(student0.getEnglish()));
            t4.setText(Integer.toString(student0.getPhysics()));
            t5.setText(Integer.toString(student0.getChemistry()));
            t6.setText(Integer.toString(student0.getBiology()));

//            设置学号不能更改
            t0.setEditable(false);
        }

        b1.setOnAction(actionEvent -> {
            if (number == 0) {
                try {
                    if (t0.getText().trim().equals("") || t1.getText().trim().equals("") || t2.getText().trim().equals("") || t3.getText().trim().equals("") || t4.getText().trim().equals("") || t5.getText().trim().equals("") || t6.getText().trim().equals("")) {
                        tips("修改失败");
                    } else {
                        if (Connect.magupdate(Integer.valueOf(t0.getText()), Integer.valueOf(t1.getText()), Integer.valueOf(t2.getText()), Integer.valueOf(t3.getText()), Integer.valueOf(t4.getText()), Integer.valueOf(t5.getText()), Integer.valueOf(t6.getText()))) {
                            tips("修改成功");
                            ArrayList<Student> students = new ArrayList<>();
                            maginit(bp, students);
                        } else {
                            tips("修改失败");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//        添加到面板中
        gp.add(l0, 0, 0);
        gp.add(t0, 1, 0);
        gp.add(l1, 0, 1);
        gp.add(t1, 1, 1);
        gp.add(l2, 0, 2);
        gp.add(t2, 1, 2);
        gp.add(l3, 0, 3);
        gp.add(t3, 1, 3);
        gp.add(l4, 0, 4);
        gp.add(t4, 1, 4);
        gp.add(l5, 0, 5);
        gp.add(t5, 1, 5);
        gp.add(l6, 0, 6);
        gp.add(t6, 1, 6);
        gp.add(b1, 1, 7);

//        添加场景舞台
        Scene sc = new Scene(gp, 300, 300);
        Stage stage1 = new Stage();
//        窗口关闭时减一
        stage1.setOnCloseRequest(windowEvent -> {
            anum--;
        });
        stage1.setScene(sc);
        stage1.setTitle("修改");
        stage1.show();
    }


    //    修改界面
    static void alter(BorderPane bp) {
        anum++;
        GridPane gp = new GridPane();
//        设置间距
        gp.setVgap(10);
        gp.setHgap(10);
        gp.setAlignment(Pos.CENTER);
//        文本
        Label l0 = new Label("学号");
        Label l1 = new Label("姓名");
        Label l2 = new Label("性别");
        Label l3 = new Label("班级");
        Label l4 = new Label("地址");
        Label l5 = new Label("年龄");
        Label l6 = new Label("电话");
        Label l7 = new Label("密码");


        Button b1 = new Button("提交");
//        文本框
        TextField t0 = new TextField();
        TextField t1 = new TextField();
        TextField t2 = new TextField();
        TextField t3 = new TextField();
        TextField t4 = new TextField();
        TextField t5 = new TextField();
        TextField t6 = new TextField();
        TextField t7 =new TextField();


        //监听文本输入的内容
        t2.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue.length()<2&&(newValue.equals("男")||newValue.equals("女"))){
                    t2.setText(newValue);
                }else{
                    t2.setText("");
                }
            }
        });

        if(student0!=null){
            t0.setText(Integer.toString(student0.getId()));
            t1.setText(student0.getName());
            t2.setText(student0.getGender());
            t3.setText(student0.getClas());
            t4.setText(student0.getAddress());
            t5.setText(student0.getAge());
            t6.setText(student0.getPhone());
            t7.setText(student0.getPassword());

//            设置文本框不可编辑
            t0.setEditable(false);

        }else{

        }

        b1.setOnAction(actionEvent -> {
            if (number == 0) {
                try {
                    if (t0.getText().trim().equals("") || t1.getText().trim().equals("") || t2.getText().trim().equals("") || t3.getText().trim().equals("") || t4.getText().trim().equals("") || t5.getText().trim().equals("") || t6.getText().trim().equals("") || t7.getText().trim().equals("")||t6.getText().length()!=11) {
                        tips("修改失败");
                    } else {
                        if (Connect.update(t1.getText(), t2.getText(), t3.getText(), t4.getText(), t5.getText(), t6.getText(), t7.getText(), Integer.valueOf(t0.getText()))) {
                            tips("修改成功");
                            ArrayList<Student> students = new ArrayList<>();
                            init(bp, students);
                        }
                        ;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//        添加到面板中
        gp.add(l0, 0, 0);
        gp.add(t0, 1, 0);
        gp.add(l1, 0, 1);
        gp.add(t1, 1, 1);
        gp.add(l2, 0, 2);
        gp.add(t2, 1, 2);
        gp.add(l3, 0, 3);
        gp.add(t3, 1, 3);
        gp.add(l4, 0, 4);
        gp.add(t4, 1, 4);
        gp.add(l5, 0, 5);
        gp.add(t5, 1, 5);
        gp.add(l6, 0, 6);
        gp.add(t6, 1, 6);
        gp.add(l7, 0, 7);
        gp.add(t7, 1, 7);
        gp.add(b1, 1, 8);

//        添加场景舞台
        Scene sc = new Scene(gp, 300, 300);
        Stage stage1 = new Stage();
//        窗口关闭时减一
        stage1.setOnCloseRequest(windowEvent -> {
            anum--;
        });
        stage1.setScene(sc);
        stage1.setTitle("修改");
        stage1.show();
    }

    //    删除界面
    static void delete(BorderPane bp) {
        anum++;
        GridPane gp = new GridPane();
//        设置间距
        gp.setVgap(10);
        gp.setHgap(10);
        gp.setAlignment(Pos.CENTER);
//        文本
        Label l0 = new Label("学号");
        Button b1 = new Button("提交");
//        文本框
        TextField t0 = new TextField();

        b1.setOnAction(actionEvent -> {
            if (number == 0) {
                try {
                    if (t0.getText().trim().equals("")) {
                        tips("删除失败");
                    } else {
                        if (Connect.delete(Integer.valueOf(t0.getText()))) {
                            tips("删除成功");
                            ArrayList<Student> students = new ArrayList<>();
                            init(bp, students);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

//        添加到面板中
        gp.add(l0, 0, 0);
        gp.add(t0, 1, 0);
        gp.add(b1, 1, 1);

//        添加场景舞台
        Scene sc = new Scene(gp, 300, 300);
        Stage stage1 = new Stage();
//        窗口关闭时减一
        stage1.setOnCloseRequest(windowEvent -> {
            anum--;
        });
        stage1.setScene(sc);
        stage1.setTitle("删除");
        stage1.show();
    }

    //    提示框
    static void tips(String str) {
//        窗口数+1
        number++;
//        添加文本
        Label l1 = new Label(str);
        Button b1 = new Button("返回");
//        创建面板
        VBox vb = new VBox(l1, b1);
        Scene sc = new Scene(vb, 100, 100);
        vb.setSpacing(10);
        vb.setAlignment(Pos.CENTER);
        Stage stage1 = new Stage();
        stage1.setScene(sc);
//        设置窗口大小不可调节
        stage1.setResizable(false);
        stage1.setOnCloseRequest(windowEvent -> {
            number--;
        });
        b1.setOnAction(actionEvent -> {
            number--;
            stage1.close();
        });
        stage1.setTitle("提示");
        stage1.show();
    }


    //    管理员表格初始化
    static void init(BorderPane bp, ArrayList<Student> students) {
        //        中部————表格控件
        TableView<Student> tv = new TableView<Student>();
//        创建列
        TableColumn c1 = new TableColumn("学号");
        TableColumn c2 = new TableColumn("姓名");
        TableColumn c3 = new TableColumn("性别");
        TableColumn c4 = new TableColumn("班级");
        TableColumn c5 = new TableColumn("地址");
        TableColumn c6 = new TableColumn("年龄");
        TableColumn c7 = new TableColumn("电话");
        TableColumn c8 =new TableColumn("密码");
//

        c1.setMinWidth(100);
        c2.setMinWidth(100);
        c3.setMinWidth(100);
        c4.setMinWidth(100);
        c5.setMinWidth(100);
        c6.setMinWidth(100);
        c7.setMinWidth(100);
        c8.setMinWidth(100);

//        确定数据导入的列
        c1.setCellValueFactory(new PropertyValueFactory<>("id"));
        c2.setCellValueFactory(new PropertyValueFactory<>("name"));
        c3.setCellValueFactory(new PropertyValueFactory<>("gender"));
        c4.setCellValueFactory(new PropertyValueFactory<>("clas"));
        c5.setCellValueFactory(new PropertyValueFactory<>("address"));
        c6.setCellValueFactory(new PropertyValueFactory<>("age"));
        c7.setCellValueFactory(new PropertyValueFactory<>("phone"));
        c8.setCellValueFactory(new PropertyValueFactory<>("password"));


//        鼠标点击后储存该行数据
        tv.setRowFactory( a -> {
            TableRow<Student> row = new TableRow<Student>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    student0 =row.getItem();
                }
            });
            return row ;
        });


        tv.getColumns().addAll(c1, c2, c3, c4, c5, c6, c7,c8);
        //        创建学生集合
        try {
            Connect.student(students);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv.getItems().addAll(students);
        bp.setCenter(tv);
    }

    //    学生成绩管理界面
    static void magscore() {
        BorderPane bp = new BorderPane();
//        顶部
        FlowPane fp1 = new FlowPane();

        Label l1 = new Label("姓名");
        Label l3 = new Label("班级");

        TextField f1 = new TextField();
        TextField f3 = new TextField();

        Button b1 = new Button("查询");
        Button b3 = new Button("刷新");

//        模糊查询
        b1.setOnAction(actionEvent -> {
            if (!f1.getText().trim().equals("") && f3.getText().trim().equals("")) {   //姓名
                ArrayList<Student> students = new ArrayList<>();
                maginit(bp, students, f1.getText(), 1);
            } else if (f1.getText().trim().equals("") && !f3.getText().trim().equals("")) {  //班级
                ArrayList<Student> students = new ArrayList<>();
                maginit(bp, students, f3.getText(), 2);
            } else if (!f1.getText().trim().equals("") && !f3.getText().trim().equals("")) {  //姓名+班级
                ArrayList<Student> students = new ArrayList<>();
                maginit1(bp, students, f1.getText(), f3.getText());
            }
        });
        b3.setOnAction(actionEvent -> {
            ArrayList<Student> students = new ArrayList<>();
            maginit(bp, students);
        });
        fp1.getChildren().addAll(l1, f1, l3, f3, b1, b3);
        fp1.setHgap(10);
//        设置上下之间的间隙内间距
        fp1.setPadding(new Insets(10));
        bp.setTop(fp1);

//        底部
        FlowPane fp2 = new FlowPane();
        Button b5 = new Button("修改");
        Button b6 = new Button("退出");

//        修改
        b5.setOnAction(actionEvent -> {
            if (anum == 0) {
                magalter(bp);
            }
        });
        b6.setOnAction((actionEvent -> {
            maghomepage();
        }));
        fp2.getChildren().addAll(b5, b6);
        fp2.setHgap(10);
        fp2.setPadding(new Insets(10));
        bp.setBottom(fp2);

//        中部————表格控件
        ArrayList<Student> students = new ArrayList<>();
        maginit(bp, students);
//        添加场景
        Scene sc = new Scene(bp);
        stage.setScene(sc);
    }

    //    学生成绩管理表单
    static void maginit(BorderPane bp, ArrayList<Student> students) {
        //        中部————表格控件
        TableView<Student> tv = new TableView<Student>();
//        创建列
        TableColumn c1 = new TableColumn("学号");
        TableColumn c2 = new TableColumn("姓名");
        TableColumn c3 = new TableColumn("班级");
        TableColumn c4 = new TableColumn("语文");
        TableColumn c5 = new TableColumn("数学");
        TableColumn c6 = new TableColumn("英语");
        TableColumn c7 = new TableColumn("物理");
        TableColumn c8 = new TableColumn("化学");
        TableColumn c9 = new TableColumn("生物");
        TableColumn c10 = new TableColumn("总分");

//        表格列宽宽度设置
        c1.setMinWidth(100);
        c2.setMinWidth(100);
        c3.setMinWidth(100);
        c4.setMinWidth(100);
        c5.setMinWidth(100);
        c6.setMinWidth(100);
        c7.setMinWidth(100);
        c8.setMinWidth(100);
        c9.setMinWidth(100);
        c10.setMinWidth(100);

//        确定数据导入的列
        c1.setCellValueFactory(new PropertyValueFactory<>("id"));
        c2.setCellValueFactory(new PropertyValueFactory<>("name"));
        c3.setCellValueFactory(new PropertyValueFactory<>("clas"));
        c4.setCellValueFactory(new PropertyValueFactory<>("language"));
        c5.setCellValueFactory(new PropertyValueFactory<>("math"));
        c6.setCellValueFactory(new PropertyValueFactory<>("english"));
        c7.setCellValueFactory(new PropertyValueFactory<>("physics"));
        c8.setCellValueFactory(new PropertyValueFactory<>("chemistry"));
        c9.setCellValueFactory(new PropertyValueFactory<>("biology"));
        c10.setCellValueFactory(new PropertyValueFactory<>("score"));

        //        鼠标点击后储存该行数据
        tv.setRowFactory( a -> {
            TableRow<Student> row = new TableRow<Student>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (! row.isEmpty()) ) {
                    student0 =row.getItem();
                }
            });
            return row ;
        });

        tv.getColumns().addAll(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10);
        //        创建学生集合
        try {
            Connect.magstudent(students);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv.getItems().addAll(students);

//        双击输出行

        bp.setCenter(tv);
    }

    //    名字或班级   模糊查询表格
    static void maginit(BorderPane bp, ArrayList<Student> students, String s, int a) {
        //        中部————表格控件
        TableView<Student> tv = new TableView<Student>();
//        创建列
        TableColumn c1 = new TableColumn("学号");
        TableColumn c2 = new TableColumn("姓名");
        TableColumn c3 = new TableColumn("班级");
        TableColumn c4 = new TableColumn("语文");
        TableColumn c5 = new TableColumn("数学");
        TableColumn c6 = new TableColumn("英语");
        TableColumn c7 = new TableColumn("物理");
        TableColumn c8 = new TableColumn("化学");
        TableColumn c9 = new TableColumn("生物");
        TableColumn c10 = new TableColumn("总分");
//        表格列宽宽度设置
        c1.setMinWidth(100);
        c2.setMinWidth(100);
        c3.setMinWidth(100);
        c4.setMinWidth(100);
        c5.setMinWidth(100);
        c6.setMinWidth(100);
        c7.setMinWidth(100);
        c8.setMinWidth(100);
        c9.setMinWidth(100);
        c10.setMinWidth(100);

//        确定数据导入的列
        c1.setCellValueFactory(new PropertyValueFactory<>("id"));
        c2.setCellValueFactory(new PropertyValueFactory<>("name"));
        c3.setCellValueFactory(new PropertyValueFactory<>("clas"));
        c4.setCellValueFactory(new PropertyValueFactory<>("language"));
        c5.setCellValueFactory(new PropertyValueFactory<>("math"));
        c6.setCellValueFactory(new PropertyValueFactory<>("english"));
        c7.setCellValueFactory(new PropertyValueFactory<>("physics"));
        c8.setCellValueFactory(new PropertyValueFactory<>("chemistry"));
        c9.setCellValueFactory(new PropertyValueFactory<>("biology"));
        c10.setCellValueFactory(new PropertyValueFactory<>("score"));

        tv.getColumns().addAll(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10);
        //        创建学生集合
        try {
            Connect.maginquire(students, s, a);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv.getItems().addAll(students);
        bp.setCenter(tv);
    }

    //    名字和班级   模糊查询表格
    static void maginit1(BorderPane bp, ArrayList<Student> students, String name, String clas) {
        //        中部————表格控件
        TableView<Student> tv = new TableView<Student>();
//        创建列
        TableColumn c1 = new TableColumn("学号");
        TableColumn c2 = new TableColumn("姓名");
        TableColumn c3 = new TableColumn("班级");
        TableColumn c4 = new TableColumn("语文");
        TableColumn c5 = new TableColumn("数学");
        TableColumn c6 = new TableColumn("英语");
        TableColumn c7 = new TableColumn("物理");
        TableColumn c8 = new TableColumn("化学");
        TableColumn c9 = new TableColumn("生物");
        TableColumn c10 = new TableColumn("总分");
//        表格列宽宽度设置
        c1.setMinWidth(100);
        c2.setMinWidth(100);
        c3.setMinWidth(100);
        c4.setMinWidth(100);
        c5.setMinWidth(100);
        c6.setMinWidth(100);
        c7.setMinWidth(100);
        c8.setMinWidth(100);
        c9.setMinWidth(100);
        c10.setMinWidth(100);

//        确定数据导入的列
        c1.setCellValueFactory(new PropertyValueFactory<>("id"));
        c2.setCellValueFactory(new PropertyValueFactory<>("name"));
        c3.setCellValueFactory(new PropertyValueFactory<>("clas"));
        c4.setCellValueFactory(new PropertyValueFactory<>("language"));
        c5.setCellValueFactory(new PropertyValueFactory<>("math"));
        c6.setCellValueFactory(new PropertyValueFactory<>("english"));
        c7.setCellValueFactory(new PropertyValueFactory<>("physics"));
        c8.setCellValueFactory(new PropertyValueFactory<>("chemistry"));
        c9.setCellValueFactory(new PropertyValueFactory<>("biology"));
        c10.setCellValueFactory(new PropertyValueFactory<>("score"));

        tv.getColumns().addAll(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10);
        //        创建学生集合
        try {
            Connect.maginquire(students, name, clas);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv.getItems().addAll(students);
        bp.setCenter(tv);
    }


    //    名字模糊查询表格
    static void init(BorderPane bp, ArrayList<Student> students, String name) {
        //        中部————表格控件
        TableView<Student> tv = new TableView<Student>();
//        创建列
        TableColumn c1 = new TableColumn("学号");
        TableColumn c2 = new TableColumn("姓名");
        TableColumn c3 = new TableColumn("性别");
        TableColumn c4 = new TableColumn("班级");
        TableColumn c5 = new TableColumn("地址");
        TableColumn c6 = new TableColumn("年龄");
        TableColumn c7 = new TableColumn("电话");
        TableColumn c8 = new TableColumn("密码");
//        表格列宽宽度设置
        c1.setMinWidth(100);
        c2.setMinWidth(100);
        c3.setMinWidth(100);
        c4.setMinWidth(100);
        c5.setMinWidth(100);
        c6.setMinWidth(100);
        c7.setMinWidth(100);
        c8.setMinWidth(100);

//        确定数据导入的列
        c1.setCellValueFactory(new PropertyValueFactory<>("id"));
        c2.setCellValueFactory(new PropertyValueFactory<>("name"));
        c3.setCellValueFactory(new PropertyValueFactory<>("gender"));
        c4.setCellValueFactory(new PropertyValueFactory<>("clas"));
        c5.setCellValueFactory(new PropertyValueFactory<>("address"));
        c6.setCellValueFactory(new PropertyValueFactory<>("age"));
        c7.setCellValueFactory(new PropertyValueFactory<>("phone"));
        c8.setCellValueFactory(new PropertyValueFactory<>("password"));

        tv.getColumns().addAll(c1, c2, c3, c4, c5, c6, c7,c8);
        //        创建学生集合
        try {
            Connect.inquire(students, name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv.getItems().addAll(students);
        bp.setCenter(tv);
    }

    //    id查询后表格
    static void init(BorderPane bp, ArrayList<Student> students, int id) {
        //        中部————表格控件
        TableView<Student> tv = new TableView<Student>();
//        创建列
        TableColumn c1 = new TableColumn("学号");
        TableColumn c2 = new TableColumn("姓名");
        TableColumn c3 = new TableColumn("性别");
        TableColumn c4 = new TableColumn("班级");
        TableColumn c5 = new TableColumn("地址");
        TableColumn c6 = new TableColumn("年龄");
        TableColumn c7 = new TableColumn("电话");
//        表格列宽宽度设置
        c1.setMinWidth(100);
        c2.setMinWidth(100);
        c3.setMinWidth(100);
        c4.setMinWidth(100);
        c5.setMinWidth(100);
        c6.setMinWidth(100);
        c7.setMinWidth(100);

//        确定数据导入的列
        c1.setCellValueFactory(new PropertyValueFactory<>("id"));
        c2.setCellValueFactory(new PropertyValueFactory<>("name"));
        c3.setCellValueFactory(new PropertyValueFactory<>("gender"));
        c4.setCellValueFactory(new PropertyValueFactory<>("clas"));
        c5.setCellValueFactory(new PropertyValueFactory<>("address"));
        c6.setCellValueFactory(new PropertyValueFactory<>("age"));
        c7.setCellValueFactory(new PropertyValueFactory<>("phone"));

        tv.getColumns().addAll(c1, c2, c3, c4, c5, c6, c7);
        //        创建学生集合
        try {
            Connect.inquire(students, id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tv.getItems().addAll(students);
        bp.setCenter(tv);
    }

}
