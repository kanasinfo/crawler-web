package com.ch.dao;

import com.ch.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Devid on 2016/10/29.
 */
public interface ResourcesRepository extends JpaRepository<Resource, String> {
}
