package cn.test.web.servlet;

import cn.test.domain.ResultInfo;
import cn.test.domain.User;
import cn.test.service.UserService;
import cn.test.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/registerUserServlet")
public class RegisterUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //先来判断验证码是否正确，才做后面的逻辑判断
        // 通过request获取客户端发送过来的验证码   前台验证码input框中name的值check
        String check = request.getParameter("check");
        //通过request获取服务器端的session,接着再从服务器端的session 中获取 服务器生成的 验证码,就是放入HttpSession中的 CHECKCODE_SERVER
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");//由于CHECKCODE_SERVER 是字符串类型的，所以要强转为String类型
        session.removeAttribute(checkcode_server);  //避免用户点后退导致验证码复用，为了保证验证码只能使用一次
        //比较 客户端 发送过来的验证码 与 服务器端 生成的验证码 是否一致
        if (checkcode_server == null ||!checkcode_server.equalsIgnoreCase(check)){
            //验证码不正确，给提示信息
            ResultInfo info = new ResultInfo();
            //验证码错误,注册失败
            info.setFlag(false);
            info.setErrorMsg("验证码错误！");
            //将 info对象 序列化为 json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);

            //将json 数据 响应 写回客户端
            //设置content-type
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(json);

            return;
        }


        //1、获取数据
        Map<String, String[]> map = request.getParameterMap();
        //2、封装对象        需要一个User
        User user  = new User();
        try {
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3、调用service完成注册
        UserService service = new UserServiceImpl();
        boolean flag = service.regist(user);
        ResultInfo info = new ResultInfo();
        //4、响应结果
        if (flag){
            //注册成功
            info.setFlag(true);
        }else{
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("注册失败！");
        }
        //将 info对象 序列化为 json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);

        //将json 数据 响应 写回客户端
        //设置content-type
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
