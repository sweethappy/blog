package com.yjh.blob.web.admin;

import com.yjh.blob.Service.BlogService;
import com.yjh.blob.Service.TagService;
import com.yjh.blob.Service.TypeService;
import com.yjh.blob.po.Blog;
import com.yjh.blob.po.User;
import com.yjh.blob.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;

@Controller
@RequestMapping("/admin")
public class BlogController {


    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final  String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blog(Model model,
                       @PageableDefault(size = 3,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                       BlogQuery blog)
    {
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return "admin/blogs";

    }
    @PostMapping("/blogs/search")
    public String search(Model model,
                         @PageableDefault(size = 3,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         BlogQuery blog)
    {
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return "admin/blogs :: blogList";

    }
    @GetMapping("/blogs/input")
    public String input(Model model){
        model.addAttribute("tags",tagService.listTag() );
        model.addAttribute("types",typeService.listType());
        model.addAttribute("blog",new Blog());
        return "admin/blogs-input";
    }

    @GetMapping("/blogs/{id}/input")
    public String editinput(Model model, @PathVariable Long id){
        model.addAttribute("tags",tagService.listTag() );
        model.addAttribute("types",typeService.listType());
        Blog blog=blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return "admin/blogs-input";
    }

    @PostMapping("/blogs")
    public String post(Blog blog, HttpSession session, RedirectAttributes redirectAttributes){
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog sava ;
        if (blog.getId() == null) {
            sava = blogService.sava(blog);
        }
        else {
            sava = blogService.updateBlog(blog.getId(),blog);
        }
        if(sava == null){
            redirectAttributes.addFlashAttribute("message","操作失败");
        }
        else {
            redirectAttributes.addFlashAttribute("message","操作成功");
        }

        return REDIRECT_LIST;
    }

    @GetMapping("blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes redirectAttributes){
        blogService.delete(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return REDIRECT_LIST;
    }

}
