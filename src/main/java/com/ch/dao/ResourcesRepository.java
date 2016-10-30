package com.ch.dao;

import com.ch.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Devid on 2016/10/29.
 */
public interface ResourcesRepository extends JpaRepository<Resource, String> {
    long countByIsFetchSuccess(boolean isFetchSuccess);

    List<Resource> findByIsFetchSuccess(boolean isFetchSuccess);
}
