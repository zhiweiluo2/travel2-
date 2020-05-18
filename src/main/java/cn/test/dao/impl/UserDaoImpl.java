package cn.test.dao.impl;

import cn.test.dao.UserDao;
import cn.test.domain.User;
import cn.test.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

public class UserDaoImpl implements UserDao {

    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public User findByUsername(String username) {
        User user = null;
        try {   //若传递的用户名没有查询到一个user对象，或者 new BeanPropertyRowMapper 没有封装成功，不会返回null，会直接报异常，所以try catch
            //1、定义sql
            String sql = "select * from tab_user where username = ?";
            //2、执行sql  用户名查询只能查询出一个对象，所以用queryForObject()
            user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
        } catch (Exception e) {   //不管出什么异常
            //先外头声明一个 默认值null 的 user,里面给 user 赋值，如果将来出现异常，证明user没有赋上值，然后就直接返回一个 默认值null，避免将来保存失败
        }
        return user;
    }

    @Override
    public void save(User user) {
        //1、定义sql
        String  sql = "insert into tab_user(username,password,name,birthday,sex,telephone,email) values(?,?,?,?,?,?,?)";
        //2、执行sql
        template.update(sql,user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail());
    }
}
