package com.yjh.blob.web.admin;

import com.yjh.blob.Service.TagService;
import com.yjh.blob.po.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TagController {

    @Resource
    private TagService tagService;

    @GetMapping("/tags")
    public String tags(@PageableDefault(size = 3,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable, Model model){
        model.addAttribute("page",tagService.listTag(pageable));
        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String input(Model model){
        model.addAttribute("tag",new Tag());
        return "admin/tags-input";
    }

    @PostMapping("/tags")
    public  String post(@Valid Tag tag, BindingResult bindingResult, RedirectAttributes redirectAttributes){
        Tag tag1 = tagService.getTagName(tag.getName());
        if(tag1!=null){
            bindingResult.rejectValue("name","nameError","不能添加重复的标签");
        }
        if(bindingResult.hasErrors()){
            return "admin/tags-input";
        }
        Tag saveTag = tagService.saveTag(tag);
        if(saveTag==null){
            redirectAttributes.addFlashAttribute("message","新增失败");
        }
        else {
            redirectAttributes.addFlashAttribute("message","新增成功");
        }
        return "redirect:/admin/tags";
    }

    @GetMapping("tags/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes redirectAttributes){
        tagService.deleteType(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/tags";
    }
    @GetMapping("tags/{id}/input")
    public String edit(@PathVariable Long id,Model model){
        model.addAttribute("tag",tagService.getTag(id));
        return "admin/tags-input";
    }

    @PostMapping("tags/{id}")
    public String edit(@Valid Tag tag,BindingResult bindingResult,@PathVariable Long id,RedirectAttributes redirectAttributes){
        Tag tag1 = tagService.getTagName(tag.getName());
        if(tag1!=null){
            bindingResult.rejectValue("message","nameError","不能重复新增标签");
        }
        if(bindingResult.hasErrors()){
            return "admin/tags-input";
        }
        Tag tag2 = tagService.updataType(id, tag);
        if(tag2==null){
            redirectAttributes.addFlashAttribute("message","更新失败");
        }
        else {
            redirectAttributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/tags";
    }




}
