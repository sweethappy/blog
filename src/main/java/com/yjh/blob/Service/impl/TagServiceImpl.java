package com.yjh.blob.Service.impl;

import com.yjh.blob.NotFoundException;
import com.yjh.blob.Service.TagService;
import com.yjh.blob.dao.TagRepository;
import com.yjh.blob.po.Tag;
import com.yjh.blob.po.Type;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    TagRepository tagRepository;

    @Override
    @Transactional
    public Tag saveTag(Tag tag) {
        return tagRepository.save(tag);
    }


    @Override
    @Transactional
    public void deleteType(Long id) {
         tagRepository.delete(id);
    }

    @Override
    @Transactional
    public Tag updataType(Long id, Tag tag) {
        Tag tag1 = tagRepository.findOne(id);
        if(tag1==null){
            throw new NotFoundException("该标签不存在");
        }
        BeanUtils.copyProperties(tag,tag1);
        return tagRepository.save(tag1);
    }

    @Override
    @Transactional
    public Tag getTag(Long id) {
        return tagRepository.findOne(id);
    }

    @Override
    @Transactional
    public Page<Tag> listTag(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public Tag getTagName(String name) {
        return tagRepository.findByName(name);
    }

    @Override
    public List<Tag> listTag() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> listTag(String ids) {
        return tagRepository.findAll(convertToList(ids));
    }

    private List<Long> convertToList(String ids){
        List<Long> list = new ArrayList<>();
        if(!"".equals(ids) && ids!=null){
            String[] idarray = ids.split(",");
            for(int i=0;i<idarray.length;i++){
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC,"blogs.size");
        Pageable pageable= new PageRequest(0,size,sort);
        return tagRepository.findTop(pageable);
    }
}
