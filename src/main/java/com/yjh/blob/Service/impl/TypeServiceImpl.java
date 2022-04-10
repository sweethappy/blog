package com.yjh.blob.Service.impl;

import com.yjh.blob.NotFoundException;
import com.yjh.blob.Service.TypeService;
import com.yjh.blob.dao.TypeRepository;
import com.yjh.blob.po.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {
    @Autowired
    private TypeRepository typeRepository;
    @Transactional
    @Override
    public Type saveType(Type type) {

        return typeRepository.save(type);
    }
    @Transactional
    @Override
    public Type getType(Long id) {
        return typeRepository.findOne(id);
    }
    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable) {
        return typeRepository.findAll(pageable);
    }
    @Transactional
    @Override
    public Type updateType(Long id, Type type) {
        Type one = typeRepository.findOne(id);
        if(one==null){
            throw  new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(type,one);
        return typeRepository.save(one);
    }
    @Transactional
    @Override
    public void deleteType(Long id) {
        typeRepository.delete(id);
    }

    @Override
    public Type getTypeName(String name) {
        return typeRepository.findByName(name);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC,"blogs.size");
        Pageable pageable= new PageRequest(0,size,sort);
        return typeRepository.findTop(pageable);
    }
}
