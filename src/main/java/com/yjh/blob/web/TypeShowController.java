package com.yjh.blob.web;


import com.yjh.blob.Service.BlogService;
import com.yjh.blob.Service.TypeService;
import com.yjh.blob.po.Type;
import com.yjh.blob.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@PageableDefault(size = 6, sort = {"updateTime"},direction = Sort.Direction.DESC)Pageable pageable, Model model,
                        @PathVariable Long id){
        List<Type> types = typeService.listTypeTop(1000);
        if(id == -1){
            id = types.get(0).getId();
        }
        BlogQuery b = new BlogQuery();
        b.setTypeId(id);
        model.addAttribute("page",blogService.listBlog(pageable,b));
        model.addAttribute("types",types);
        model.addAttribute("activeTypeId",id);
        return "types";
    }
}
