package com.yjh.blob.Service.impl;

import com.yjh.blob.NotFoundException;
import com.yjh.blob.Service.BlogService;
import com.yjh.blob.dao.BlogRepository;
import com.yjh.blob.po.Blog;
import com.yjh.blob.po.Type;
import com.yjh.blob.utils.MarkdownUtils;
import com.yjh.blob.utils.MyBeanUtils;
import com.yjh.blob.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Transient;
import javax.persistence.criteria.*;
import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;
    @Override
    public Blog getBlog(Long id) {
        return blogRepository.findOne(id);
    }

    @Transactional
    @Override
    public Blog getAndConvert(long id) {
        Blog blog = blogRepository.findOne(id);
        if(blog==null){
            throw new NotFoundException("博客不存在");
        }
        Blog b = new Blog();
        BeanUtils.copyProperties(blog,b);
        String content = b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        blogRepository.updateViews(id);
        return b;
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blog) {

        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(!"".equals(blog.getTitle()) && blog.getTitle()!=null){
                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }
                if(blog.getTypeId()!=null){
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blog.getTypeId()));
                }
                if(blog.isRecommend()){
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }
                query.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Long TagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Join tags = root.join("tags");
                return cb.equal(tags.get("id"),TagId);
            }
        },pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }



    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query,pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> gropYear = blogRepository.findGropYear();
        Map<String,List<Blog>> map = new HashMap<>();
        for(String year : gropYear){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Transactional
    @Override
    public Blog sava(Blog blog) {
        if(blog.getId()==null){
            blog.setUpdateTime(new Date());
            blog.setCreateTime(new Date());
            blog.setViews(0);
        }
        else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save( blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog one = blogRepository.findOne(id);
        if(one==null){
            throw new NotFoundException("该博客不存在");
        }
        BeanUtils.copyProperties(blog,one, MyBeanUtils.getNullPrepertyNames(blog));
        one.setUpdateTime(new Date());
        return  blogRepository.save(one);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        blogRepository.delete(id);
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");
        Pageable pageable = new PageRequest(0,size,sort);
        return blogRepository.findRecommendTop(pageable);
    }
}
