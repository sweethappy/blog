package com.yjh.blob.Service;

import com.yjh.blob.po.Blog;
import com.yjh.blob.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;


public interface BlogService {
    Blog getBlog(Long id);

    Blog getAndConvert(long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blog);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Long TagId,Pageable pageable);

    Page<Blog> listBlog(String query,Pageable pageable);

    Map<String,List<Blog>> archiveBlog();

    Long countBlog();

    Blog sava(Blog blog);

    Blog updateBlog(Long id,Blog blog);

    void delete(Long id);

    List<Blog>  listRecommendBlogTop(Integer size);
}
