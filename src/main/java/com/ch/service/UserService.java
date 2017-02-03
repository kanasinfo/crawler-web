package com.ch.service;

import com.ch.dao.UserRepository;
import com.ch.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by gefangshuai on 2017/1/31.
 */
@Service
@Transactional(readOnly = true)
public class UserService {
    @Resource
    private UserRepository userRepository;

    public User findByAccount(String account) {
        return userRepository.findByAccount(account);
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    public User findById(String userId) {
        return userRepository.findOne(userId);
    }
}
