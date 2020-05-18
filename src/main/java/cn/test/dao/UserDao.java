package cn.test.dao;

import cn.test.domain.User;

public interface UserDao {
    /**
     * 根据用户名查询用户信息
     * @param [username]
     * @return cn.itcast.travel.domain.User
     */
    public User findByUsername(String username);

    /**
     * 注册用户保存
     * @param [user]
     * @return void
     */
    public void save(User user);
}
