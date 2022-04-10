package com.yjh.blob.web.admin;


import com.yjh.blob.Service.BlogService;
import com.yjh.blob.Service.TypeService;
import com.yjh.blob.Service.UserService;
import com.yjh.blob.po.User;
import com.yjh.blob.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Resource
    private UserService userService;

    @RequestMapping
    public String loginPage(){
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, HttpSession session, RedirectAttributes attributes,
                        Model model,
                        @PageableDefault(size = 3,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog){
        User user = userService.checkUser(username, password);
        if(user!=null){
            session.setAttribute("user",user);
            model.addAttribute("types",typeService.listType());
            model.addAttribute("page",blogService.listBlog(pageable,blog));
            return "admin/blogs";
        }
        else {
            attributes.addAttribute("message","用户密码不正确");
            return "redirect:/admin";
        }
    }
    @GetMapping("/loginout")
    public String loginout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
