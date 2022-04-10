package com.yjh.blob.dao;

import com.yjh.blob.po.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog> {
    @Query("select  t from  Blog t where t.recommend=True")
    List<Blog> findRecommendTop(Pageable pageable);

    @Query("select t from Blog t where t.title like ?1 or t.content like ?1")
    Page<Blog> findByQuery(String query,Pageable page);


    @Query("select function('date_format',t.updateTime,'%Y') as year from Blog t group by function('date_format',t.updateTime,'%Y') order by year DESC ")
    List<String> findGropYear();

    @Query("select b from Blog b where function('date_format',b.updateTime,'%Y') = ?1 ")
    List<Blog> findByYear(String year);


    @Transactional
    @Modifying
    @Query("update Blog b set b.views = b.views+1 where b.id=?1")
    int updateViews(Long id);

}
