package sample;

import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class Connect {
    /**
     * URL地址
     */
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/ycx?useSSL=false";
    /**
     * 登录数据库服务器的账号
     */
    private static final String USER = "root";
    /**
     * 登录数据库服务器的密码
     */
    private static final String PASSWORD = "15194538986a";

    /**
     * 返回数据库连接对象
     *
     * @return
     */
    public static Connection getConn() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭资源
     *
     * @param rs
     * @param stat
     * @param conn
     */
    public static void close(ResultSet rs, Statement stat, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (stat != null) {
                stat.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 封装通用的更新操作（即通过该方法实现对弈任意数据表的insert，update，delete操作）
     *
     * @param sql    需要执行的SQL语句
     * @param params 执行SQL语句时需要传递进去参数
     * @return 执行结果
     */
    public static boolean exeUpdate(String sql, Object... params) {
        //获取连接对象
        Connection conn = getConn();
        PreparedStatement ps = null;
        try {
            //获取预编译对象
            ps = conn.prepareStatement(sql);
            //执行参数赋值操作
            if (Objects.nonNull(params)) {
                //循环将所有的参数赋值
                for (int i = 0; i < params.length; i++) {
                    ps.setObject(i + 1, params[i]);
                }
            }
            //执行更新
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            //关闭资源
            close(null, ps, conn);
        }
        return false;
    }

    //    学生登录
    public static int login(int id, String pwd) throws Exception {

        String sql = "select * from student where id=? and password=?";
        PreparedStatement pstmt = getConn().prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.setString(2, pwd);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {

            return rs.getInt("id");
        } else {

            return 0;
        }
    }

    //    管理员登录
    public static boolean maglogin(String name, String pwd) throws Exception {

        String sql = "select * from admin_login_k where admin_id=? and admin_pass=?";
        PreparedStatement pstmt = getConn().prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.setString(2, pwd);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return true;
        } else {
            return false;
        }
    }

    //    增加
    public static boolean add(String name, String gender, String clas, String address,
                              String age, String phone, String password) throws Exception {
        String sql = "insert into student(name,gender,clas,address,age,phone,password) values (?,?,?,?,?,?,?)";
        return exeUpdate(sql, name, gender, clas, address, age, phone, password);

    }

    //    修改
    public static boolean update(String name, String gender, String clas, String address,
                                 String age, String phone, String password, int id) throws Exception {
        String sql = "update student set name=?,gender=?,clas=?,address=?,age=?,phone=?,password=? where id=?";
        return exeUpdate(sql, name, gender, clas, address, age, phone, password, id);

    }

    //    删除
    public static boolean delete(int id) {
        String sql = "delete from student where id=?";
        return exeUpdate(sql, id);
    }

    //    学生成绩修改
    public static boolean magupdate(int id, int language, int math, int english, int physics, int chemistry,
                                    int biology) throws Exception {
        String sql1 = "update score set language=?,math=?,english=? where student_id=?";
        String sql2 = "update elective set physics=?,chemistry=?,biology=? where student_id=?";
        return exeUpdate(sql1, language, math, english, id) && exeUpdate(sql2, physics, chemistry, biology, id);

    }


    //        模糊查询
    public static void inquire(ArrayList<Student> list, String name) throws Exception {

        String sql = "select * from student where name like ?";
        PreparedStatement pstmt = getConn().prepareStatement(sql);
        pstmt.setString(1, "%" + name + "%");
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()) {
            Student student = new Student();
            student.setId(rs.getInt("id"));
            student.setName(rs.getString("name"));
            student.setGender(rs.getString("gender"));
            student.setClas(rs.getString("clas"));
            student.setAddress(rs.getString("address"));
            student.setAge(rs.getString("age"));
            student.setPhone(rs.getString("phone"));
            student.setPassword(rs.getString("password"));
            list.add(student);
        }
        //        释放资源
        rs.close();
        pstmt.close();
    }

    //        模糊查询--学生成绩
    public static void maginquire(ArrayList<Student> list, String s, int a) throws Exception {

        String sql = "";
        if (a == 1) {   //名字模糊查询
            sql = "select id,name,clas,language,math,english,physics,chemistry,biology,language+math+english+physics+chemistry+biology 'score' from student,score,elective where id=score.student_id and id=elective.student_id and name like?;";
        } else if (a == 2) {  //班级模糊查询
            sql = "select id,name,clas,language,math,english,physics,chemistry,biology,language+math+english+physics+chemistry+biology 'score' from student,score,elective where id=score.student_id and id=elective.student_id and clas like?;";
        }
        PreparedStatement pstmt = getConn().prepareStatement(sql);
        pstmt.setString(1, "%" + s + "%");
        ResultSet rs = pstmt.executeQuery();
        while(rs.next()){
            Student student = new Student();
            student.setId(rs.getInt("id"));
            student.setName(rs.getString("name"));
            student.setClas(rs.getString("clas"));
            student.setLanguage(rs.getInt("language"));
            student.setMath(rs.getInt("math"));
            student.setEnglish(rs.getInt("english"));
            student.setPhysics(rs.getInt("physics"));
            student.setChemistry(rs.getInt("chemistry"));
            student.setBiology(rs.getInt("biology"));
            student.setScore(rs.getInt("score"));
            list.add(student);
        }
        //        释放资源
        rs.close();
        pstmt.close();
    }

    //        模糊查询--学生成绩
    public static void maginquire(ArrayList<Student> list, String name, String clas) throws Exception {


        //名字+班级
        String sql = "select id,name,clas,language,math,english,physics,chemistry,biology,language+math+english+physics+chemistry+biology 'score' from student,score,elective where id=score.student_id and id=elective.student_id and  name like? and  clas like?;";

        PreparedStatement pstmt = getConn().prepareStatement(sql);
        pstmt.setString(1, "%" + name + "%");
        pstmt.setString(2, "%" + clas + "%");
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            Student student = new Student();
            student.setId(rs.getInt("id"));
            student.setName(rs.getString("name"));
            student.setClas(rs.getString("clas"));
            student.setLanguage(rs.getInt("language"));
            student.setMath(rs.getInt("math"));
            student.setEnglish(rs.getInt("english"));
            student.setPhysics(rs.getInt("physics"));
            student.setChemistry(rs.getInt("chemistry"));
            student.setBiology(rs.getInt("biology"));
            student.setScore(rs.getInt("score"));
            list.add(student);
        }
        //        释放资源
        rs.close();
        pstmt.close();
    }

    //        学号查询
    public static void inquire(ArrayList<Student> list, int id) throws Exception {

        String sql = "select * from student where id=?";
        PreparedStatement pstmt = getConn().prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            Student student = new Student();
            student.setId(rs.getInt("id"));
            student.setName(rs.getString("name"));
            student.setGender(rs.getString("gender"));
            student.setClas(rs.getString("clas"));
            student.setAddress(rs.getString("address"));
            student.setAge(rs.getString("age"));
            student.setPhone(rs.getString("phone"));
            list.add(student);
        }
        //        释放资源
        rs.close();
        pstmt.close();
    }

    //    成绩查询--学生
    public static void score(Student student, int id) throws Exception {

        String sql = "select id,name,clas,language,math,english,physics,chemistry,biology,language+math+english+physics+chemistry+biology 'score' from student,score,elective where id=score.student_id and id=elective.student_id and id=?;";
        PreparedStatement pstmt = getConn().prepareStatement(sql);
        pstmt.setInt(1, id);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            student.setLanguage(rs.getInt("language"));
            student.setMath(rs.getInt("math"));
            student.setEnglish(rs.getInt("english"));
            student.setLanguage(rs.getInt("language"));
            student.setMath(rs.getInt("math"));
            student.setEnglish(rs.getInt("english"));
            student.setPhysics(rs.getInt("physics"));
            student.setChemistry(rs.getInt("chemistry"));
            student.setBiology(rs.getInt("biology"));
            student.setScore(rs.getInt("score"));
        }
        //        释放资源
        rs.close();
        pstmt.close();
    }


    //   学生类初始化
    public static void student(ArrayList<Student> list) throws Exception {
        String sql = "select * from student ";
        PreparedStatement pstmt = getConn().prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {

//            获取数据
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String gender = rs.getString("gender");
            String clas = rs.getString("clas");
            String address = rs.getString("address");
            String age = rs.getString("age");
            String phone = rs.getString("phone");
            String password=rs.getString("password");

//            封装数据
            Student student = new Student();
            student.setId(id);
            student.setName(name);
            student.setGender(gender);
            student.setClas(clas);
            student.setAddress(address);
            student.setAge(age);
            student.setPhone(phone);
            student.setPassword(password);

            list.add(student);
        }
//        释放资源
        rs.close();
        pstmt.close();
    }

    //   学生类成绩初始化--管理员
    public static void magstudent(ArrayList<Student> list) throws Exception {
        String sql = "select id,name,clas,language,math,english,physics,chemistry,biology,language+math+english+physics+chemistry+biology 'score' from student,score,elective where id=score.student_id and id=elective.student_id; ";
        PreparedStatement pstmt = getConn().prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {

//            获取数据
            int id = rs.getInt("id");
            String name = rs.getString("name");
            String clas = rs.getString("clas");
            int language = rs.getInt("language");
            int math = rs.getInt("math");
            int english = rs.getInt("english");
            int physics = rs.getInt("physics");
            int chemistry = rs.getInt("chemistry");
            int biology = rs.getInt("biology");
            int score = rs.getInt("score");

//            封装数据
            Student student = new Student();
            student.setId(id);
            student.setName(name);
            student.setClas(clas);
            student.setLanguage(language);
            student.setMath(math);
            student.setEnglish(english);
            student.setPhysics(physics);
            student.setChemistry(chemistry);
            student.setBiology(biology);
            student.setScore(score);

            list.add(student);
        }
//        释放资源
        rs.close();
        pstmt.close();
    }


}
