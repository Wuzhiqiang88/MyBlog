package com.sao.service;

import com.sao.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface UserService {
    /**
     * 保存、更新用户
     * @param user
     * @return
     */
    User saveOrUpateUser(User user);

    /**
     * 注册用户
     * @param user
     * @return
     */
    User registerUser(User user);
    /**
     * 删除用户
     * @param id
     * @return
     */
    void removeUser(Long id);

    /**
     * 根据id获取用户
     * @param id
     * @return
     */
    User getUserById(Long id);

    User getUserByEmail(String email);
    User getUserByUsername(String username);
    /**
     * 根据用户昵称进行分页模糊查询
     * @param username
     * @param pageable
     * @return
     */
    Page<User> listUsersByUsernameLike(String username, Pageable pageable);



}
