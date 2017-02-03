package com.ch.service;

import com.ch.dao.LikeRepository;
import com.ch.model.Like;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by gefangshuai on 2017/1/31.
 */
@Service
@Transactional(readOnly = true)
public class LikeService {
    @Resource
    private LikeRepository likeRepository;

    @Transactional
    public Like save(Like like) {
        return likeRepository.save(like);
    }
}
