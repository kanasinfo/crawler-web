package com.ch.dao;

import com.ch.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Devid on 2016/12/5.
 */
public interface UserRepository extends JpaRepository<User, String> {
    User findByAccount(String account);
}
