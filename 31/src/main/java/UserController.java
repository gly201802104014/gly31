import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/user.ctl")
public class UserController extends HttpServlet {
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获得代表请求对象的json字串
        String user_json = JSONUtil.getJSON(request);
        //将JSON字串解析为User对象
        User userToAdd = JSON.parseObject(user_json,User.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中修改对应密码
        try {
            UserService.getInstance().changePassword(userToAdd);
            message.put("message", "修改密码成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String user_json = JSONUtil.getJSON(request);
        //将JSON字串解析为User对象
        User userToCheck = JSON.parseObject(user_json,User.class);
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //在数据库表中增加Department对象
        try {
            UserService.getInstance().login(userToCheck.getUsername(),userToCheck.getPassword());
            message.put("username",UserService.getInstance().login(userToCheck.getUsername(),userToCheck.getPassword()).getUsername());
            message.put("message","登陆成功");
        } catch (SQLException e) {
            message.put("message", "数据库操作异常导致登陆失败");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常导致登录失败");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        String username = request.getParameter("username");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id值不为空，调用根据id值响应一个用户对象方法，否则，调用根据用户名响应一个用户对象的方法
            if (id_str != null){
                //强制类型转换成int型
                int id = Integer.parseInt(id_str);
                this.responseUser(id,response);
            }else if (username != null){
                this.responseUserByUserName(username,response);
            }
        } catch (SQLException e) {
            message.put("message", "数据库操作异常");
            e.printStackTrace();
        } catch (Exception e) {
            message.put("message", "网络异常");
            e.printStackTrace();
        }
        //响应message到前端
        response.getWriter().println(message);
    }
    //根据id响应一个用户对象
    private void responseUser(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找院系
        User user = UserService.getInstance().find(id);
        String user_json = JSON.toJSONString(user);
        //响应Department_json到前端
        response.getWriter().println(user_json);
    }
    //根据用户名响应一个用户对象
    private void responseUserByUserName(String username, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        //根据id查找院系
        User user = UserService.getInstance().findByUserName(username);
        String user_json = JSON.toJSONString(user);
        //响应Department_json到前端
        response.getWriter().println(user_json);
    }
}
