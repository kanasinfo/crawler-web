package com.ch.common.support;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Devid on 2016/11/11.
 */
@Transactional(readOnly = true)
public abstract class SupportService<T, ID extends Serializable> {
    protected SupportRepository<T, ID> supportRepository;

    public List<T> findAll(){
        return supportRepository.findAll();
    }

    public T findOne(ID id) {
        return supportRepository.findOne(id);
    }

    @Transactional
    public T save(T t) {
        return supportRepository.save(t);
    }

    public List<T> findAll(Sort sort) {
        return supportRepository.findAll(sort);
    }

    public Page<T> findAll(Pageable pageable){
        return supportRepository.findAll(pageable);
    }

    @Transactional
    public void delete(ID id) {
        supportRepository.delete(id);
    }

    public long count(){
        return supportRepository.count();
    }


}
