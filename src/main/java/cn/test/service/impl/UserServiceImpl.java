package cn.test.service.impl;


import cn.test.dao.UserDao;
import cn.test.dao.impl.UserDaoImpl;
import cn.test.domain.User;
import cn.test.service.UserService;

public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();
    /**
     * 注册用户
     * @param [user]
     * @return boolean
     */
    @Override
    public boolean regist(User user) {
        //1、根据用户名查询用户对象
        User u = userDao.findByUsername(user.getUsername());  //成员变量不能和局部变量同名
        //判断 u 是否为空
        if (u != null){
            return false;
        }
        //2、保存用户信息   return false;巨坑
        userDao.save(user);
        return true;
    }

}
